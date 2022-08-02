package be.ehb.gdt.jrivia.activities

data class Clue(val id: Int, val question: String, val answer: String, val value: Int) {
    var guess: String = ""

    fun isCorrect(): Boolean = answer == guess
}