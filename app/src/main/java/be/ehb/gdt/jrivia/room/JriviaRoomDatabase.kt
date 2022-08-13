package be.ehb.gdt.jrivia.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import be.ehb.gdt.jrivia.models.DailyQuest
import be.ehb.gdt.jrivia.models.Score
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Score::class, DailyQuest::class], version = 3, exportSchema = false)
abstract class JriviaRoomDatabase : RoomDatabase() {
    abstract fun scoreDao(): ScoreDao
    abstract fun dailyQuestDao(): DailyQuestDao

    companion object {
        @Volatile
        private var INSTANCE: JriviaRoomDatabase? = null

        fun getDatabase(context: Context): JriviaRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    JriviaRoomDatabase::class.java,
                    "jrivia_database"
                )
//                    .addCallback(JriviaDatabaseCallback(scope))
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }

    private class JriviaDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let {
                scope.launch { insertFirstDailyQuest(it.dailyQuestDao()) }
            }
        }

        suspend fun insertFirstDailyQuest(dao: DailyQuestDao) {
            if(dao.countQuests() > 0) {
                return
            }
            val quest = DailyQuest(1,"It recently moved ahead of Chicago to become our 2nd largest", "Los Angeles", 100)
            quest.dateInMillis = System.currentTimeMillis() - 23 * 60 * 60 * 1000
            dao.insertQuest(quest)
        }
    }
}