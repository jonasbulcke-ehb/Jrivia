package be.ehb.gdt.jrivia.room

import androidx.annotation.WorkerThread
import be.ehb.gdt.jrivia.models.DailyClue
import kotlinx.coroutines.flow.Flow

class DailyClueRepository(private val dailyClueDao: DailyClueDao) {

    val allClues: List<DailyClue> = dailyClueDao.getAllClues()
    val lastClue: DailyClue? = dailyClueDao.getLastClue()

    fun getLastClues(numberOfClues: Int = 30): Flow<List<DailyClue>> =
        dailyClueDao.getLastClues(numberOfClues)

//    fun insert(dailyClue: DailyClue): Boolean {
//        val alreadyExists = dailyClueDao.doesClueAlreadyExist(dailyClue.id)
//        if (!alreadyExists) {
//            dailyClueDao.insertClue(dailyClue)
//            return true
//        }
//        return false
//    }

    fun insert(dailyClue: DailyClue) {
        dailyClueDao.insertClue(dailyClue)
    }

    @WorkerThread
    suspend fun update(dailyClue: DailyClue) {
        dailyClueDao.updateClue(dailyClue)
    }
}