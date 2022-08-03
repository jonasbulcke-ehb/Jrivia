package be.ehb.gdt.jrivia.models.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import be.ehb.gdt.jrivia.models.Clue
import be.ehb.gdt.jrivia.models.Game

class GameViewModel : ViewModel() {
    lateinit var game: Game
    var index = 0
        private set
    val currentClue: Clue
        get() = game.clues[index]

    fun moveNext() {
        if (index < Game.NUMBER_OF_QUESTIONS)
            index++

    }

    fun moveBack() {
        if(index > 0)
            index--
    }
}