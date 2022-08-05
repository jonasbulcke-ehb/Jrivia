package be.ehb.gdt.jrivia.room

import androidx.room.*
import be.ehb.gdt.jrivia.models.Score
import kotlinx.coroutines.flow.Flow

@Dao
interface ScoreDao {
    @Query("SELECT * FROM scores ORDER BY value DESC, time, totalNumberOfQuestions DESC, correctNumberOfQuestions DESC")
    fun getAllScores(): Flow<List<Score>>

    @Query("SELECT * FROM scores WHERE totalNumberOfQuestions = :numberOfQuestions ORDER BY value DESC, time, correctNumberOfQuestions DESC")
    fun getScoresByTotalNumberOfQuestions(numberOfQuestions: Int): Flow<List<Score>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertScore(score: Score)

    @Delete
    suspend fun deleteScore(score: Score)

}