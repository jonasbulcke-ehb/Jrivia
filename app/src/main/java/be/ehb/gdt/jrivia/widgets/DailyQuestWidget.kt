package be.ehb.gdt.jrivia.widgets

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.lifecycle.asLiveData
import be.ehb.gdt.jrivia.R
import be.ehb.gdt.jrivia.activities.DailyQuestsActivity
import be.ehb.gdt.jrivia.room.DailyQuestRepository
import be.ehb.gdt.jrivia.room.JriviaRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

/**
 * Implementation of App Widget functionality.
 */
class DailyQuestWidget : AppWidgetProvider() {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        appWidgetIds.forEach {
            updateAppWidget(context, appWidgetManager, it)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

@SuppressLint("RemoteViewLayout")
internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.widget_daily_quest)

    val pendingIntent = PendingIntent.getActivity(
        context,
        0,
        Intent(context, DailyQuestsActivity::class.java),
        PendingIntent.FLAG_IMMUTABLE
    )

    val repository =
        DailyQuestRepository(
            JriviaRoomDatabase.getDatabase(context, CoroutineScope(Dispatchers.IO)).dailyQuestDao()
        )

    repository.getLastQuest().asLiveData().observeForever {
        Log.d("WIDGET", it.question)
        views.setTextViewText(R.id.widgetDailyTextView, it.question)
//        views.setOnClickPendingIntent(R.id.widgetLayout, pendingIntent)
    }

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}