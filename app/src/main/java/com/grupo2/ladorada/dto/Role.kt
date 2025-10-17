package com.grupo2.ladorada.dto

import com.google.gson.annotations.SerializedName

data class Role(
    @SerializedName("id_role") val idRole: Int,
    @SerializedName("role_name") val roleName: String,
    @SerializedName("description") val description: String
)