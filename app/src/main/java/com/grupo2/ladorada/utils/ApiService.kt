package com.grupo2.ladorada.utils

import com.grupo2.ladorada.dto.ApiResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // 🔹 Petición GET genérica (la respuesta se maneja como ApiResponse<Any>)
    @GET
    suspend fun getData(@Url endpoint: String): Response<ResponseBody>

    // 🔹 Petición POST genérica
    @POST
    suspend fun postData(
        @Url endpoint: String,
        @Body body: Any
    ): Response<ResponseBody>
}