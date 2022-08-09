package be.ehb.gdt.jrivia.viewmodels

import androidx.lifecycle.*
import be.ehb.gdt.jrivia.models.DailyQuest
import be.ehb.gdt.jrivia.room.DailyQuestRepository
import kotlinx.coroutines.launch

class DailyQuestViewModel(private val repository: DailyQuestRepository) : ViewModel() {
    lateinit var lastQuest: DailyQuest

    fun getLastQuest(): LiveData<DailyQuest> = repository.getLastQuest().asLiveData()

    fun getQuestsOfLastMonth(): LiveData<List<DailyQuest>> = repository.getLastQuests().asLiveData()

    fun updateLastQuest() = viewModelScope.launch {
        repository.update(lastQuest)
    }

}