package be.ehb.gdt.jrivia

import android.app.Application
import be.ehb.gdt.jrivia.room.DailyQuestRepository
import be.ehb.gdt.jrivia.room.JriviaRoomDatabase
import be.ehb.gdt.jrivia.room.ScoreRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class JriviaApplication : Application() {
    private val database by lazy { JriviaRoomDatabase.getDatabase(this) }
    val scoreRepository by lazy { ScoreRepository(database.scoreDao()) }
    val dailyQuestRepository by lazy { DailyQuestRepository(database.dailyQuestDao()) }
}