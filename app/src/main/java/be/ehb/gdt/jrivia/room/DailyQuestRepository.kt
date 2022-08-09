package be.ehb.gdt.jrivia.room

import androidx.annotation.WorkerThread
import be.ehb.gdt.jrivia.models.DailyQuest
import kotlinx.coroutines.flow.Flow

class DailyQuestRepository(private val dailyQuestDao: DailyQuestDao) {
    fun getLastQuest(): Flow<DailyQuest> = dailyQuestDao.getLastQuest()

    fun getLastQuests(numberOfQuests: Int = 30): Flow<List<DailyQuest>> =
        dailyQuestDao.getLastQuests(numberOfQuests)

    @WorkerThread
    suspend fun count() = dailyQuestDao.countQuests()

    @WorkerThread
    suspend fun insert(dailyQuest: DailyQuest) {
        dailyQuestDao.insertQuest(dailyQuest)
    }

    @WorkerThread
    suspend fun update(dailyQuest: DailyQuest) {
        dailyQuestDao.updateQuest(dailyQuest)
    }
}