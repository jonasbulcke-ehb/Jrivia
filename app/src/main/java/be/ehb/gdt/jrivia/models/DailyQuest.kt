package be.ehb.gdt.jrivia.models

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

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

    fun isFromToday(): Boolean {
        return System.currentTimeMillis() - dateInMillis < 24 * 60 * 60 * 1000
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