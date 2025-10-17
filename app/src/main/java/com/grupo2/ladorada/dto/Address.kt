package com.grupo2.ladorada.dto

import com.google.gson.annotations.SerializedName

data class Address(
    @SerializedName("id_address") val idAddress: Int,
    @SerializedName("id_user") val idUser: Int,
    @SerializedName("address_line") val addressLine: String,
    @SerializedName("city") val city: String?,
    @SerializedName("state") val state: String?,
    @SerializedName("postal_code") val postalCode: String?,
    @SerializedName("address_type") val addressType: String?,
    @SerializedName("is_primary") val isPrimary: Boolean,
    @SerializedName("type") val type: String?,
    @SerializedName("id_country") val idCountry: Int,
    @SerializedName("id_order") val idOrder: Int?,
    @SerializedName("country") val country: Country
)