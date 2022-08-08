package be.ehb.gdt.jrivia.models

import android.os.Parcel
import android.os.Parcelable

data class Clue(
    val id: Int,
    val question: String,
    var answer: String,
    val value: Int = 100
) : Parcelable {
    var guess: String? = ""
    val isCorrect get() = answer.lowercase() == guess?.lowercase()

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt()
    ) {
        guess = parcel.readString()
    }

    override fun toString() = "{id:${id},question:${question},answer:${answer},value:${value}"


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(question)
        parcel.writeString(answer)
        parcel.writeInt(value)
        parcel.writeString(guess)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Clue> {
        override fun createFromParcel(parcel: Parcel): Clue {
            return Clue(parcel)
        }

        override fun newArray(size: Int): Array<Clue?> {
            return arrayOfNulls(size)
        }
    }
}