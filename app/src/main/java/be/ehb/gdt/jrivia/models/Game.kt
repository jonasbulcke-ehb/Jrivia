package be.ehb.gdt.jrivia.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

data class Game(val clues: List<Clue>) : Comparable<Game> {
    private var startTime: Date? = null
    private var endTime: Date? = null

    val time: Long? =
        startTime?.time?.seconds?.let { endTime?.time?.seconds?.minus(it)?.inWholeSeconds }

    val score = clues.filter { it.isCorrect() }.sumOf { it.value }

    fun start() {
        if (startTime == null) {
            startTime = Calendar.getInstance().time
        }
    }

    fun end() {
        if (endTime == null) {
            endTime = Calendar.getInstance().time
        }
    }

    override fun compareTo(other: Game): Int {
        val scoreCompared = this.score.compareTo(other.score)
        return if (scoreCompared == 0) other.time?.let { this.time?.compareTo(it) }
            ?: 0 else scoreCompared

    }

    companion object {
        const val NUMBER_OF_QUESTIONS = 15
    }
}