package be.ehb.gdt.jrivia.room

import androidx.room.*
import be.ehb.gdt.jrivia.models.DailyQuest
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyQuestDao {

    @Query("SELECT * FROM daily_clues ORDER BY dateInMillis DESC LIMIT :numberOfQuests")
    fun getLastQuests(numberOfQuests: Int): Flow<List<DailyQuest>>

    @Query("SELECT * FROM daily_clues ORDER BY dateInMillis DESC LIMIT 1")
    fun getLastQuest(): Flow<DailyQuest>

    @Query("SELECT count(*) FROM daily_clues")
    suspend fun countQuests() : Int

    @Insert
    suspend fun insertQuest(clue: DailyQuest)

    @Update
    suspend fun updateQuest(clue: DailyQuest)
}