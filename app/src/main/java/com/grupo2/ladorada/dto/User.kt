package com.grupo2.ladorada.dto

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id_user") val idUser: Int,
    @SerializedName("id_role") val idRole: Int,
    @SerializedName("id_country") val idCountry: Int,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("email") val email: String,
    @SerializedName("phone") val phone: String?,
    @SerializedName("avatar") val avatar: String?,
    @SerializedName("is_active") val isActive: Boolean,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("country") val country: Country,
    @SerializedName("role") val role: Role,
    @SerializedName("addresses") val addresses: List<Address>
)
