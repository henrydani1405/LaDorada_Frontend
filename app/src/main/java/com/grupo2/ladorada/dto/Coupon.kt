package com.grupo2.ladorada.dto

import com.google.gson.annotations.SerializedName

data class Coupon(
    @SerializedName("id_coupon") val idCoupon: Int,
    @SerializedName("code") val code: String,
    @SerializedName("discount_type") val discountType: String,
    @SerializedName("discount_value") val discountValue: Double,
    @SerializedName("expires_at") val expiresAt: String?,
    @SerializedName("is_active") val isActive: Boolean,
    @SerializedName("created_at") val createdAt: String?
)