package com.grupo2.ladorada.dto


import com.google.gson.annotations.SerializedName

data class Order(
    @SerializedName("id_order") val idOrder: Int,
    @SerializedName("id_user") val idUser: Int,
    @SerializedName("id_status") val idStatus: Int,
    @SerializedName("id_coupon") val idCoupon: Int?, // Es opcional, puede ser nulo
    @SerializedName("subtotal") val subtotal: Double,
    @SerializedName("shipping_cost") val shippingCost: Double,
    @SerializedName("tax_amount") val taxAmount: Double,
    @SerializedName("total_amount") val totalAmount: Double,
    @SerializedName("discount_amount") val discountAmount: Double,
    @SerializedName("placed_at") val placedAt: String,
    @SerializedName("shipped_at") val shippedAt: String?, // Puede ser nulo si aún no se envía
    @SerializedName("image") val image: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,

    // Objetos anidados
    @SerializedName("user") val user: User,
    @SerializedName("orderStatus") val orderStatus: OrderStatus,
    @SerializedName("orderDetails") val orderDetails: List<OrderDetail>?
)