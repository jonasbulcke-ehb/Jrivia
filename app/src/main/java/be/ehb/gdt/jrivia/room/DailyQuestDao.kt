package be.ehb.gdt.jrivia.room

import androidx.room.*
import be.ehb.gdt.jrivia.models.DailyQuest

@Dao
interface DailyQuestDao {

//    @Query("SELECT * FROM daily_clues ORDER BY dateInMillis DESC")
//    fun getAllClues(): List<DailyClue>

    @Query("SELECT * FROM daily_clues ORDER BY dateInMillis DESC LIMIT :numberOfQuests")
    suspend fun getLastQuests(numberOfQuests: Int): List<DailyQuest>

    @Query("SELECT * FROM daily_clues ORDER BY dateInMillis DESC LIMIT 1")
    suspend fun getLastQuest(): DailyQuest

    @Query("SELECT count(*) FROM daily_clues")
    suspend fun countQuests() : Int


    @Insert
    suspend fun insertQuest(clue: DailyQuest)

    @Update
    suspend fun updateQuest(clue: DailyQuest)
}