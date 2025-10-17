package com.grupo2.ladorada.dto

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    @SerializedName("status") val status: Int? = null,
    @SerializedName("success") val success: Boolean = false,
    @SerializedName("locale") val locale: String? = null,
    @SerializedName("message") val message: String? = null,
    @SerializedName("errors") val errors: Map<String, List<String>>? = null,
    @SerializedName("data") val data: T? = null
)

data class TokenData(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("token_type") val tokenType: String,
    @SerializedName("id_user") val idUser: Int,
    @SerializedName("expires_at") val expiresAt: String
)