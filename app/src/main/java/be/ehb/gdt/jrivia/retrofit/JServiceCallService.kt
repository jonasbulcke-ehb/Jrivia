package be.ehb.gdt.jrivia.retrofit

import be.ehb.gdt.jrivia.models.Clue
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface JServiceCallService {
    @GET("random")
    fun getRandomClues(@Query("count") count: Int): Call<List<Clue>>
}