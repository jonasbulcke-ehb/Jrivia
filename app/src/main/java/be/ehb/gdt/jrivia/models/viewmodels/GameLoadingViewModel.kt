package be.ehb.gdt.jrivia.models.viewmodels

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.ehb.gdt.jrivia.R
import be.ehb.gdt.jrivia.models.Clue
import be.ehb.gdt.jrivia.retrofit.RetrofitUtil
import be.ehb.gdt.jrivia.util.IntentExtraNames
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList
import kotlin.streams.toList

class GameLoadingViewModel(application: Application) : AndroidViewModel(application) {
    private val context = application.applicationContext

    private val sharedPref =
        context.getSharedPreferences(PREFERENCE_USERNAME_KEY, Context.MODE_PRIVATE)
    var username: String = sharedPref.getString(USERNAME_KEY, "")!!
    var numberOfQuestions = 10
    var clues: List<Clue> = ArrayList()

    fun fetchClues(updateViewOnSuccess: Runnable, updateViewOnFailure: Runnable) {
        viewModelScope.launch {
            val cluesCall = RetrofitUtil.getCallService().getRandomClues(numberOfQuestions)
            cluesCall.enqueue(
                object : Callback<List<Clue>> {
                    override fun onResponse(
                        call: Call<List<Clue>>,
                        response: Response<List<Clue>>
                    ) {
                        if (response.code() == 200) {
//                        @Suppress("UNCHECKED_CAST")
                            clues = response.body()!!
                            clues.forEach {
                                if (it.answer.contains("<i>")) {
                                    it.answer = it.answer.replace("<i>", "").replace("</i>", "")
                                }
                            }
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

    fun saveUsername() {
        with(sharedPref.edit()) {
            putString(USERNAME_KEY, username)
            Log.d(USERNAME_KEY, username)
            apply()
        }
    }

    companion object {
        const val PREFERENCE_USERNAME_KEY =
            "be.ehb.gdt.jrivia.models.viewModels.GameLoadingViewModel.PREFERENCE_USERNAME_KEY"
        const val USERNAME_KEY = "username"

    }

}