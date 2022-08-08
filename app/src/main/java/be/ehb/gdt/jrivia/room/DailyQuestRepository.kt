package be.ehb.gdt.jrivia.room

import androidx.annotation.WorkerThread
import be.ehb.gdt.jrivia.models.DailyQuest

class DailyQuestRepository(private val dailyQuestDao: DailyQuestDao) {
//
    @WorkerThread
    suspend fun getLastQuest(): DailyQuest = dailyQuestDao.getLastQuest()

    @WorkerThread
    suspend fun getLastQuests(numberOfQuests: Int = 30): List<DailyQuest> =
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