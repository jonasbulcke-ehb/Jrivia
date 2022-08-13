package be.ehb.gdt.jrivia.widgets

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import be.ehb.gdt.jrivia.R
import be.ehb.gdt.jrivia.activities.DailyQuestsActivity
import be.ehb.gdt.jrivia.models.DailyQuest
import be.ehb.gdt.jrivia.room.DailyQuestRepository
import be.ehb.gdt.jrivia.room.JriviaRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.M)
class DailyQuestWidget : AppWidgetProvider() {
    private val job = SupervisorJob()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + job)

    override fun onReceive(context: Context, intent: Intent?) {
        super.onReceive(context, intent)

        val appWidgetManager = AppWidgetManager.getInstance(context)
        val ids = appWidgetManager.getAppWidgetIds(
            ComponentName(context, DailyQuestWidget::class.java)
        )

        val repository = DailyQuestRepository(
            JriviaRoomDatabase.getDatabase(context).dailyQuestDao()
        )

        coroutineScope.launch {
            repository.getLastQuest().collect { dailyQuest: DailyQuest? ->

                ids.forEach {
                    updateAppWidget(context, appWidgetManager, it, dailyQuest)
                }
            }
        }
    }

    override fun onDisabled(context: Context?) {
        super.onDisabled(context)
        job.cancel()
    }

}

@RequiresApi(Build.VERSION_CODES.M)
@SuppressLint("RemoteViewLayout")
internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int,
    dailyQuest: DailyQuest?
) {
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.widget_daily_quest)

    if (dailyQuest?.isFromToday() == true) {
        views.setTextViewText(R.id.widgetDailyTextView, dailyQuest.question)
    }

    PendingIntent.getActivity(
        context,
        0,
        Intent(context, DailyQuestsActivity::class.java).apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) },
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    ).also {
        views.setOnClickPendingIntent(R.id.widgetLayout, it)
    }

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}