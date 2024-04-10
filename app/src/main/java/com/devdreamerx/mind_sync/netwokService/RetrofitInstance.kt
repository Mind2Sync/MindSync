package com.devdreamerx.mind_sync.netwokService

import com.devdreamerx.mind_sync.apiService.PredictionApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "https://codeharmonyapi.azurewebsites.net/"

    val predictionApi: PredictionApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PredictionApiService::class.java)
    }
}