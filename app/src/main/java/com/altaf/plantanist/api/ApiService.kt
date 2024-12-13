package com.altaf.plantanist.api

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @Multipart
    @POST("predict")
    suspend fun predictPlant(
        @Header("token") token: String,
        @Part image: MultipartBody.Part
    ): Response<Prediction>

    @GET("history")
    suspend fun getHistory(
        @Header("token") token: String
    ): Response<Predictions>
}