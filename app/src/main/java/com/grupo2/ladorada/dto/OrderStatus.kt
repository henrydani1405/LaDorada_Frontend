package com.grupo2.ladorada.dto

import com.google.gson.annotations.SerializedName

data class OrderStatus(
    @SerializedName("id_status") val idStatus: Int,
    @SerializedName("status_name") val statusName: String
)