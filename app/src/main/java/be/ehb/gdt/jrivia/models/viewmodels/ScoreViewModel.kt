package be.ehb.gdt.jrivia.models.viewmodels

import androidx.lifecycle.*
import be.ehb.gdt.jrivia.models.Score
import be.ehb.gdt.jrivia.room.ScoreRepository
import kotlinx.coroutines.launch

class ScoreViewModel(private val repository: ScoreRepository) : ViewModel() {
    var showAll = false
    var numberOfQuestions: Int = 0

    val allScores: LiveData<List<Score>> = repository.allScores.asLiveData()

    fun getScoresByNumberOfQuestions(numberOfQuestions: Int) =
        repository.getScoresByNumberOfQuestions(numberOfQuestions).asLiveData()

    fun insert(score: Score) = viewModelScope.launch {
        repository.insert(score)
    }

    fun delete(score: Score) = viewModelScope.launch {
        repository.delete(score)
    }
}

class ScoreViewModelFactory(private val repository: ScoreRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScoreViewModel::class.java))
            @Suppress("UNCHECKED_CAST")
            return ScoreViewModel(repository) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}