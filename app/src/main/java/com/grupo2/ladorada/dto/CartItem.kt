package com.grupo2.ladorada.dto

import java.io.Serializable

data class CartItem(
    val product: Product, // Usa tu dataclass original 'Product'
    var quantity: Int = 1 // Nuevo campo: Cantidad del producto
) : Serializable
