package com.grupo2.ladorada.dto
import com.google.gson.annotations.SerializedName

data class Country(
    @SerializedName("id_country") val idCountry: Int,
    @SerializedName("name") val name: String,
    @SerializedName("iso2") val iso2: String?,
    @SerializedName("iso3") val iso3: String?,
    @SerializedName("call_code") val callCode: String?,
    @SerializedName("subregion") val subregion: String?,
    @SerializedName("region") val region: String?,
    @SerializedName("imagen") val imagen: String?,
    @SerializedName("latitude") val latitude: Double?,
    @SerializedName("longitude") val longitude: Double?,
    @SerializedName("geonameid") val geonameId: Int?,
    @SerializedName("currency_name") val currencyName: String?,
    @SerializedName("currency_symbol") val currencySymbol: String?,
    @SerializedName("currency_code") val currencyCode: String?,
    @SerializedName("is_active") val isActive: Boolean,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("updated_at") val updatedAt: String?
)
//{
//    override fun toString(): String {
//        return name
//    }
//}