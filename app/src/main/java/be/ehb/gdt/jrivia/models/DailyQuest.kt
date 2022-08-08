package be.ehb.gdt.jrivia.models

import android.annotation.SuppressLint
import android.icu.text.DateFormat
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit

@Entity(tableName = "daily_clues")
data class DailyQuest(
    @PrimaryKey val id: Int,
    val question: String,
    val answer: String,
    val value: Int,
    var dateInMillis: Long = System.currentTimeMillis()
) : Parcelable {
    var guesses: Int = 0
    var isSolved = false
    val formattedDate: String
        @SuppressLint("SimpleDateFormat")
        get() = SimpleDateFormat("dd MMMM").format(dateInMillis)

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readLong()
    ) {
        guesses = parcel.readInt()
        isSolved = parcel.readByte() != 0.toByte()
    }

    init {
        Log.d("DATE", dateInMillis.toString())
        Log.d("DATE", question)
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

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(question)
        parcel.writeString(answer)
        parcel.writeInt(value)
        parcel.writeLong(dateInMillis)
        parcel.writeInt(guesses)
        parcel.writeByte(if (isSolved) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DailyQuest> {
        override fun createFromParcel(parcel: Parcel): DailyQuest {
            return DailyQuest(parcel)
        }

        override fun newArray(size: Int): Array<DailyQuest?> {
            return arrayOfNulls(size)
        }
    }
}