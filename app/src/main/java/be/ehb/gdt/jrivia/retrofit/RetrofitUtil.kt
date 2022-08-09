package be.ehb.gdt.jrivia.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Bundles all the retrofit logic in one class
 */
class RetrofitUtil {
    companion object {
        private val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://jservice.io/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        private val jServiceCallService: JServiceCallService =
            retrofit.create(JServiceCallService::class.java)

        fun getCallService(): JServiceCallService = jServiceCallService
    }
}