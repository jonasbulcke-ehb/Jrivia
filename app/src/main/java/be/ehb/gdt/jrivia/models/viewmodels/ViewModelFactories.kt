package be.ehb.gdt.jrivia.models.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import be.ehb.gdt.jrivia.room.DailyQuestRepository
import be.ehb.gdt.jrivia.room.ScoreRepository

class ScoreViewModelFactory(private val scoreRepository: ScoreRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScoreViewModel::class.java))
            @Suppress("UNCHECKED_CAST")
            return ScoreViewModel(scoreRepository) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class DailyClueViewModelFactory(private val dailyQuestRepository: DailyQuestRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DailyQuestViewModel::class.java))
            @Suppress("UNCHECKED_CAST")
            return DailyQuestViewModel(dailyQuestRepository) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}