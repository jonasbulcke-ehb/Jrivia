package be.ehb.gdt.jrivia.services

import android.app.Service
import android.content.Intent
import android.os.*
import android.util.Log
import androidx.lifecycle.asLiveData
import be.ehb.gdt.jrivia.models.DailyClue
import be.ehb.gdt.jrivia.retrofit.RetrofitUtil
import be.ehb.gdt.jrivia.room.DailyClueRepository
import be.ehb.gdt.jrivia.room.JriviaRoomDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import retrofit2.Call
import retrofit2.Response

class DailyClueFetchService : Service() {
    //    private val dailyClueRepository = (application as JriviaApplication).dailyClueRepository
    private val dailyClueRepository by lazy {
        DailyClueRepository(
            JriviaRoomDatabase.getDatabase(
                applicationContext
            ).dailyClueDao()
        )
    }

    private var serviceLooper: Looper? = null
    private var serviceHandler: ServiceHandler? = null

    private inner class ServiceHandler(looper: Looper) : Handler(looper) {
        override fun handleMessage(msg: Message) {
            var lastClue: DailyClue? = dailyClueRepository.lastClue
            if (lastClue?.isFromToday() == false) {
                RetrofitUtil.getCallService().getRandomDailyClue().enqueue(
                    object : retrofit2.Callback<List<DailyClue>> {
                        override fun onResponse(
                            call: Call<List<DailyClue>>,
                            response: Response<List<DailyClue>>
                        ) {
                            if (response.code() == 200) {
                                lastClue = response.body()?.get(0)
                                dailyClueRepository.insert(lastClue!!)
                                Log.d("DAILY_CLUE_FETCHED", lastClue!!.question)
                            } else {
                                Log.e("DAILY_CLUE_FETCH", "Unable to fetch a new daily clue")
                            }
                        }

                        override fun onFailure(call: Call<List<DailyClue>>, t: Throwable) {
                            Log.e("DAILY_CLUE_FETCH", "Unable to fetch a new daily clue", t)
                        }
                    }
                )
            }
            stopSelf(msg.arg1)
        }
    }

    override fun onCreate() {
        HandlerThread("FetchNewDailyClue", Process.THREAD_PRIORITY_BACKGROUND).apply {
            start()

            serviceLooper = looper
            serviceHandler = ServiceHandler(looper)
        }
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        serviceHandler?.obtainMessage()?.also {
            it.arg1 = startId
            serviceHandler?.sendMessage(it)
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}