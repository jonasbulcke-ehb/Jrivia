package be.ehb.gdt.jrivia.models.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import be.ehb.gdt.jrivia.models.DailyClue
import be.ehb.gdt.jrivia.retrofit.RetrofitUtil
import be.ehb.gdt.jrivia.room.DailyClueRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DailyClueViewModel(private val repository: DailyClueRepository): ViewModel() {
    val cluesOfLastMonth: LiveData<List<DailyClue>> = repository.getLastClues().asLiveData()

    private suspend fun fetchDailyClue(): DailyClue? {
        var dailyClue: DailyClue? = null
        val dailyClueCall: Call<DailyClue>? = null
//        withContext(Dispatchers.IO) {
//            dailyClueCall = RetrofitUtil.getCallService().getRandomDailyClue()
//        }
        dailyClueCall?.enqueue(
            object : Callback<DailyClue> {
                override fun onResponse(call: Call<DailyClue>, response: Response<DailyClue>) {
                    if(response.code() == 200) {
                        dailyClue = response.body()
                    }
                }

                override fun onFailure(call: Call<DailyClue>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            }
        )
        return null
    }
}