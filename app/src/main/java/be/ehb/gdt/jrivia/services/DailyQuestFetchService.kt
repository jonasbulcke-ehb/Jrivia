package be.ehb.gdt.jrivia.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.*
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import be.ehb.gdt.jrivia.R
import be.ehb.gdt.jrivia.activities.DailyQuestsActivity
import be.ehb.gdt.jrivia.models.DailyQuest
import be.ehb.gdt.jrivia.retrofit.RetrofitUtil
import be.ehb.gdt.jrivia.room.DailyQuestRepository
import be.ehb.gdt.jrivia.room.JriviaRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class DailyQuestFetchService : Service() {
    private val scope = CoroutineScope(Dispatchers.IO)
    private val dailyQuestRepository by lazy {
        DailyQuestRepository(
            JriviaRoomDatabase.getDatabase(
                applicationContext, scope
            ).dailyQuestDao()
        )
    }

    private var serviceLooper: Looper? = null
    private var serviceHandler: ServiceHandler? = null

    private inner class ServiceHandler(looper: Looper) : Handler(looper) {
        override fun handleMessage(msg: Message) {
            scope.launch {
                var lastQuest: DailyQuest? = dailyQuestRepository.getLastQuest().firstOrNull()
                if (lastQuest?.isFromToday() == false) {
                    RetrofitUtil.getCallService().getRandomDailyClue().enqueue(
                        object : retrofit2.Callback<List<DailyQuest>> {
                            override fun onResponse(
                                call: Call<List<DailyQuest>>,
                                response: Response<List<DailyQuest>>
                            ) {
                                if (response.code() == 200) {
                                    lastQuest = response.body()!![0]
                                    lastQuest?.dateInMillis = System.currentTimeMillis()
                                    scope.launch { dailyQuestRepository.insert(lastQuest!!) }
                                    val notificationManager =
                                        applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        notificationManager.createNotificationChannel(
                                            NotificationChannel(
                                                "daily_clue",
                                                "Daily Clue",
                                                NotificationManager.IMPORTANCE_DEFAULT
                                            )
                                        )
                                    }

                                    val intent =
                                        Intent(applicationContext, DailyQuestsActivity::class.java)
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        val pendingIntent = PendingIntent.getActivity(
                                            applicationContext,
                                            0,
                                            intent,
                                            PendingIntent.FLAG_IMMUTABLE
                                        )
                                        val builder = NotificationCompat.Builder(
                                            applicationContext, "daily_clue"
                                        )
                                            .setSmallIcon(R.drawable.ic_launcher_foreground)
                                            .setContentTitle("New daily clue available")
                                            .setContentText(lastQuest!!.question)
                                            .setContentIntent(pendingIntent)
                                            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                                            .setStyle(
                                                NotificationCompat.BigTextStyle()
                                                    .bigText(lastQuest!!.question)
                                            )
                                            .setAutoCancel(true)
                                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                        notificationManager.notify(0, builder.build())
                                    }
                                } else {
                                    Log.e("DAILY_CLUE_FETCH", "Unable to fetch a new daily clue")
                                }
                            }

                            override fun onFailure(call: Call<List<DailyQuest>>, t: Throwable) {
                                Log.e("DAILY_CLUE_FETCH", "Unable to fetch a new daily clue", t)
                            }
                        }
                    )
                }

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