package com.grupo2.ladorada.dto

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("id_product") val idProduct: Int,
    @SerializedName("id_category") val idCategory: Int,
    @SerializedName("id_brand") val idBrand: Int,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("detail") val detail: String?,
    @SerializedName("sku") val sku: String?,
    @SerializedName("price") val price: Double,
    @SerializedName("stock") val stock: Int,
    @SerializedName("image") val image: String?,
    @SerializedName("is_active") val isActive: Boolean,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("updated_at") val updatedAt: String?,
    @SerializedName("category") val category: Category?,
    @SerializedName("brand") val brand: Brand?
)
