package be.ehb.gdt.jrivia.models.viewmodels

import androidx.lifecycle.ViewModel
import be.ehb.gdt.jrivia.models.Clue
import be.ehb.gdt.jrivia.models.Game

class GameViewModel : ViewModel() {
    lateinit var game: Game
    var pauseOffSet: Long = 0
    var index = 0
        private set
    val currentClue: Clue
        get() = game.clues[index]

    fun moveNext() {
        if (index < game.numberOfQuestions)
            index++

    }

    fun moveBack() {
        if(index > 0)
            index--
    }
}