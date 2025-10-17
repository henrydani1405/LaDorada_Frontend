package com.grupo2.ladorada.dto

import com.google.gson.annotations.SerializedName

data class Brand(
    @SerializedName("id_brand") val idBrand: Int,
    @SerializedName("brand_name") val brandName: String,
    @SerializedName("description") val description: String?
)
