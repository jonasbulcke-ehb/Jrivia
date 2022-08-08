package be.ehb.gdt.jrivia.models

import android.os.Parcel
import android.os.Parcelable
import java.util.concurrent.TimeUnit

data class Game(var clues: List<Clue>, var username: String, var numberOfQuestions: Int = 10) :
    Parcelable {
    var time: Long = 0
    val formattedTime
        get(): String {
            val minutes = TimeUnit.MILLISECONDS.toMinutes(time)
            val seconds = TimeUnit.MILLISECONDS.toSeconds(time) - minutes * 60
            return String.format("%02d:%02d", minutes, seconds)
        }

    val score get() = clues.filter { it.isCorrect }.sumOf { it.value }
    val correctQuestions get() = clues.count { it.isCorrect }

    constructor(parcel: Parcel) : this(
        parcel.createTypedArrayList(Clue)!!,
        parcel.readString()!!,
        parcel.readInt()
    ) {
        time = parcel.readLong()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(clues)
        parcel.writeString(username)
        parcel.writeInt(numberOfQuestions)
        parcel.writeLong(time)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Game> {
        override fun createFromParcel(parcel: Parcel): Game {
            return Game(parcel)
        }

        override fun newArray(size: Int): Array<Game?> {
            return arrayOfNulls(size)
        }
    }
}