package be.ehb.gdt.jrivia.models.viewmodels

import androidx.lifecycle.*
import be.ehb.gdt.jrivia.models.DailyQuest
import be.ehb.gdt.jrivia.room.DailyQuestRepository

class DailyQuestViewModel(private val repository: DailyQuestRepository) : ViewModel() {
    fun getQuestsOfLastMonth(): LiveData<List<DailyQuest>> = liveData {
        val data = repository.getLastQuests()
        emit(data)
    }
}