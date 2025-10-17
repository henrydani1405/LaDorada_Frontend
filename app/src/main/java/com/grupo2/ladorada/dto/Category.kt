package com.grupo2.ladorada.dto

import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("id_category") val idCategory: Int,
    @SerializedName("category_name") val categoryName: String,
    @SerializedName("description") val description: String?
)
