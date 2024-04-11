package com.devdreamerx.mind_sync.apiService

import com.devdreamerx.mind_sync.model.PredictionResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part


interface PredictionApiService {

    @Multipart
    @POST("/")
    fun getPrediction(
        @Part("name") name: String,
        @Part("age") age: Int,
        @Part("sex") sex: String,
        @Part("country") country: String,
        @Part image: MultipartBody.Part?
    ): Call<PredictionResponse>
}

