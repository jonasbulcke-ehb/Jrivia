package be.ehb.gdt.jrivia

import android.app.Application
import be.ehb.gdt.jrivia.room.DailyClueRepository
import be.ehb.gdt.jrivia.room.JriviaRoomDatabase
import be.ehb.gdt.jrivia.room.ScoreRepository

class JriviaApplication : Application() {
    private val database by lazy { JriviaRoomDatabase.getDatabase(this) }
    val scoreRepository by lazy { ScoreRepository(database.scoreDao()) }
    val dailyClueRepository by lazy { DailyClueRepository(database.dailyClueDao()) }
}