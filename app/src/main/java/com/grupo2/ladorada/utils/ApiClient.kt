package com.grupo2.ladorada.utils

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.grupo2.ladorada.dto.ApiResponse
import okhttp3.ResponseBody
import retrofit2.Response

object ApiClient {

    val gson = Gson()
    val service = RetrofitInstance.apiService

    suspend inline fun <reified T> postData(endpoint: String, body: Any): ApiResponse<T> {
        return handleResponse(service.postData(endpoint, body))
    }

    suspend inline fun <reified T> getData(endpoint: String): ApiResponse<T> {
        return handleResponse(service.getData(endpoint))
    }

    inline fun <reified T> handleResponse(response: Response<ResponseBody>): ApiResponse<T> {
        return try {
            val raw = if (response.isSuccessful)
                response.body()?.string()
            else
                response.errorBody()?.string()

            val code = response.code()
            val parsed = try {
                gson.fromJson<ApiResponse<T>>(raw, object : TypeToken<ApiResponse<T>>() {}.type)
            } catch (e: JsonSyntaxException) {
                null
            }

            if (parsed != null) {
                // --- construir mensaje combinado ---
                val baseMessage = parsed.message ?: response.message()
                val errorDetails = parsed.errors?.flatMap { (key, list) ->
                    list.map { msg -> "$key → $msg" }
                }?.joinToString(" | ")

                val combinedMessage = if (!errorDetails.isNullOrBlank()) {
                    "$baseMessage. Detalles: $errorDetails"
                } else {
                    baseMessage
                }

                parsed.copy(
                    status = code,
                    message = combinedMessage
                )
            } else {
                ApiResponse(
                    status = code,
                    success = response.isSuccessful,
                    message = response.message(),
                    data = null
                )
            }

        } catch (e: Exception) {
            ApiResponse(
                status = 500,
                success = false,
                message = e.localizedMessage ?: "Error inesperado o de conexión",
                data = null
            )
        }
    }
}
