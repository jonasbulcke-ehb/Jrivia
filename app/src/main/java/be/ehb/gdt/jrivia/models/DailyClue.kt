package be.ehb.gdt.jrivia.models

import android.icu.text.DateFormat
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit

@Entity(tableName = "daily_clues")
data class DailyClue(
    @PrimaryKey val id: Int,
    val question: String,
    val answer: String,
    val value: Int
) {
    var dateInMillis: Long = System.currentTimeMillis()
    var isSolved = false

    val date
        get(): String {
            val formatter =
                SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT, Locale("nl", "BE"))
            return formatter.format(dateInMillis)
        }

    fun isFromToday(): Boolean {
        val calendarDate = Calendar.getInstance()
        calendarDate.timeInMillis = dateInMillis
        val calendarNow = Calendar.getInstance()
        calendarNow.timeInMillis = System.currentTimeMillis()

        return calendarDate.get(Calendar.DATE) == calendarNow.get(Calendar.DATE)
    }

    fun trySolve(guess: String): Boolean {
        if (!isSolved) {
            isSolved = answer.lowercase() == guess.lowercase()
        }
        return isSolved
    }
}