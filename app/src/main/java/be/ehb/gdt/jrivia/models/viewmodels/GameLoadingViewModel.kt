package be.ehb.gdt.jrivia.models.viewmodels

import androidx.lifecycle.ViewModel
import be.ehb.gdt.jrivia.models.Clue
import be.ehb.gdt.jrivia.retrofit.JServiceCallService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.collections.ArrayList

class GameLoadingViewModel : ViewModel() {
    var username: String = ""
    var numberOfQuestions = 10
    var clues: List<Clue> = ArrayList()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://jservice.io/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val jServiceCallService: JServiceCallService =
        retrofit.create(JServiceCallService::class.java)

    suspend fun fetchClues(updateViewOnSuccess: Runnable, updateViewOnFailure: Runnable) {
        val cluesCall: Call<List<Clue>>
        withContext(Dispatchers.IO) {
            cluesCall = jServiceCallService.getRandomClues(numberOfQuestions)
        }
        cluesCall.enqueue(
            object : Callback<List<Clue>> {
                override fun onResponse(call: Call<List<Clue>>, response: Response<List<Clue>>) {
                    if (response.code() == 200) {
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