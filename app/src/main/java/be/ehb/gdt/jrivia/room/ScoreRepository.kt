package be.ehb.gdt.jrivia.room

import androidx.annotation.WorkerThread
import be.ehb.gdt.jrivia.models.Score

class ScoreRepository(private val scoreDao: ScoreDao) {

    val allScores = scoreDao.getAllScores()

    fun getScoresByNumberOfQuestions(numberOfQuestions: Int) =
        scoreDao.getScoresByTotalNumberOfQuestions(numberOfQuestions)

    @WorkerThread
    suspend fun insert(score: Score) {
        scoreDao.insertScore(score)
    }

    @WorkerThread
    suspend fun delete(score: Score) {
        scoreDao.deleteScore(score)
    }
}