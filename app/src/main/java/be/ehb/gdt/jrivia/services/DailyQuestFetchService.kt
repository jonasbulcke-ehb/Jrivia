package be.ehb.gdt.jrivia.services

import android.app.*
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
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
import be.ehb.gdt.jrivia.room.DailyQuestDao
import be.ehb.gdt.jrivia.room.DailyQuestRepository
import be.ehb.gdt.jrivia.room.JriviaRoomDatabase
import be.ehb.gdt.jrivia.util.IntentExtraNames
import be.ehb.gdt.jrivia.widgets.DailyQuestWidget
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.util.jar.Manifest

class DailyQuestFetchService : Service() {
    private val scope = CoroutineScope(Dispatchers.IO)
    private val dailyQuestRepository by lazy {
        DailyQuestRepository(
            JriviaRoomDatabase.getDatabase(applicationContext).dailyQuestDao()
        )
    }

    private var serviceLooper: Looper? = null
    private var serviceHandler: ServiceHandler? = null

    private inner class ServiceHandler(looper: Looper) : Handler(looper) {
        override fun handleMessage(msg: Message) {
            scope.launch {
                var lastQuest: DailyQuest? = dailyQuestRepository.getLastQuest().firstOrNull()
                if (lastQuest == null || !lastQuest.isFromToday()) {
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
                                    sendNotification(lastQuest!!)
                                    sendBroadcasts()
                                } else
                                    Log.e("DAILY_CLUE_FETCH", "Unable to fetch a new daily clue")
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

    fun sendNotification(dailyQuest: DailyQuest) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Android versions lower then Oreo will not receive notifications
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    NOTIFICATION_CHANNEL_ID, "Daily Quest", NotificationManager.IMPORTANCE_DEFAULT
                )
            )

            val intent = Intent(
                applicationContext,
                DailyQuestsActivity::class.java
            ) // Intent to open the dailyQuestsActivity when the notification gets clicked

            val pendingIntent = PendingIntent.getActivity(
                applicationContext, 0, intent, PendingIntent.FLAG_IMMUTABLE
            )

            val builder = NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_lightbulb_24)
                .setContentTitle(getString(R.string.new_quest_available))
                .setContentText(dailyQuest.question)
                .setContentIntent(pendingIntent)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setStyle(NotificationCompat.BigTextStyle().bigText(dailyQuest.question))
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            notificationManager.notify(0, builder.build())
        }
    }

    fun sendBroadcasts() {
        // update Widget broadcast
        Intent()
            .apply { action = "android.appwidget.action.APPWIDGET_UPDATE" }
            .also { sendBroadcast(it) }
        // update DailyQuestsTodayFragment
        Intent()
            .apply { action = UPDATE_TODAY_QUEST_VIEW }
            .also { sendBroadcast(it) }
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

    companion object {
        const val UPDATE_TODAY_QUEST_VIEW =
            "be.ehb.gdt.jrivia.Services.DailyQuestFetchService.UPDATE_TODAY_QUEST_VIEW"
        const val NOTIFICATION_CHANNEL_ID = "daily_quest"
    }


}
