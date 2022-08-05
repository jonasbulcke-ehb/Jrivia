package be.ehb.gdt.jrivia.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.concurrent.TimeUnit

@Entity(tableName = "scores")
data class Score(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val username: String,
    val value: Int,
    val time: Long,
    val correctNumberOfQuestions: Int,
    val totalNumberOfQuestions: Int
) {
    val formattedTime
        get(): String {
            val minutes = TimeUnit.MILLISECONDS.toMinutes(time)
            val seconds = TimeUnit.MILLISECONDS.toSeconds(time) - minutes * 60
            return String.format("%02d:%02d", minutes, seconds)
        }

    constructor(game: Game) : this(
        0,
        game.username,
        game.score,
        game.time,
        game.correctQuestions,
        game.numberOfQuestions
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Score

        if (id != other.id) return false
        if (username != other.username) return false
        if (value != other.value) return false
        if (time != other.time) return false
        if (correctNumberOfQuestions != other.correctNumberOfQuestions) return false
        if (totalNumberOfQuestions != other.totalNumberOfQuestions) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + username.hashCode()
        result = 31 * result + value
        result = 31 * result + time.hashCode()
        result = 31 * result + correctNumberOfQuestions
        result = 31 * result + totalNumberOfQuestions
        return result
    }
}
