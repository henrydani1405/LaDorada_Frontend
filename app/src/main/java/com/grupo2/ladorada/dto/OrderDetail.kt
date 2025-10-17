package com.grupo2.ladorada.dto

import com.google.gson.annotations.SerializedName

data class OrderDetail(
    @SerializedName("id_order_detail") val idOrderDetail: Int,
    @SerializedName("id_order") val idOrder: Int,
    @SerializedName("id_product") val idProduct: Int,
    @SerializedName("id_variant") val idVariant: Int?, // Es nulo en el ejemplo
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("unit_price") val unitPrice: Double, // Usamos Double para precios
    @SerializedName("line_total") val lineTotal: Double  // Usamos Double para totales
)
