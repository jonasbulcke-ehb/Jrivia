package be.ehb.gdt.jrivia.room

import androidx.room.*
import be.ehb.gdt.jrivia.models.DailyClue
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyClueDao {

    @Query("SELECT * FROM daily_clues ORDER BY dateInMillis DESC")
    fun getAllClues(): List<DailyClue>

    @Query("SELECT * FROM daily_clues ORDER BY dateInMillis DESC LIMIT :numberOfClues")
    fun getLastClues(numberOfClues: Int): Flow<List<DailyClue>>

    @Query("SELECT * FROM daily_clues ORDER BY dateInMillis DESC LIMIT 1")
    fun getLastClue(): DailyClue?

//    @Query("SELECT EXISTS(SELECT id FROM daily_clues WHERE id = :id)")
//    fun doesClueAlreadyExist(id: Int): Boolean

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertClue(clue: DailyClue)

    @Update
    suspend fun updateClue(clue: DailyClue)
}