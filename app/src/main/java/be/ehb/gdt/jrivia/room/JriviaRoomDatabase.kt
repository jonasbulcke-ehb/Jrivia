package be.ehb.gdt.jrivia.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import be.ehb.gdt.jrivia.models.DailyClue
import be.ehb.gdt.jrivia.models.Score

@Database(entities = [Score::class, DailyClue::class], version = 2, exportSchema = false)
abstract class JriviaRoomDatabase : RoomDatabase() {
    abstract fun scoreDao(): ScoreDao
    abstract fun dailyClueDao(): DailyClueDao

    companion object {
        @Volatile
        private var INSTANCE: JriviaRoomDatabase? = null

        fun getDatabase(context: Context): JriviaRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    JriviaRoomDatabase::class.java,
                    "jrivia_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}