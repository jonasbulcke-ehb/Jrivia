package be.ehb.gdt.jrivia.models.viewmodels

import androidx.lifecycle.ViewModel
import be.ehb.gdt.jrivia.models.Clue
import be.ehb.gdt.jrivia.retrofit.RetrofitUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList

class GameLoadingViewModel : ViewModel() {
    var username: String = ""
    var numberOfQuestions = 10
    var clues: List<Clue> = ArrayList()

    suspend fun fetchClues(updateViewOnSuccess: Runnable, updateViewOnFailure: Runnable) {
        val cluesCall: Call<List<Clue>>
        withContext(Dispatchers.IO) {
            cluesCall = RetrofitUtil.getCallService().getRandomClues(numberOfQuestions)
        }
        cluesCall.enqueue(
            object : Callback<List<Clue>> {
                override fun onResponse(call: Call<List<Clue>>, response: Response<List<Clue>>) {
                    if (response.code() == 200) {
                        @Suppress("UNCHECKED_CAST")
                        clues = response.body()!!
                        updateViewOnSuccess.run()
                    } else updateViewOnFailure.run()
                }

                override fun onFailure(call: Call<List<Clue>>, t: Throwable) {
                    updateViewOnFailure.run()
                }
            }
        )
    }


}