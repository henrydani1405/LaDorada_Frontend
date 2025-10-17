// Archivo: CartManager.kt

package com.grupo2.ladorada.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.grupo2.ladorada.dto.Product
// Importa tu nueva dataclass
import com.grupo2.ladorada.dto.CartItem

class CartManager(context: Context) {
    private val PREFS_NAME = "CartPrefs"
    private val KEY_CART = "ShoppingCart"
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    // ----------------------------------------------------
    // FUNCIÓN PRINCIPAL: AGREGAR O ACTUALIZAR PRODUCTO
    // ----------------------------------------------------
    fun addToCart(newProduct: Product ,add:Int=1) {
        // 1. Obtener la lista actual del carrito
        val currentCart = getCart()

        // 2. Buscar si el producto ya existe
        val existingItem = currentCart.find { it.product.idProduct == newProduct.idProduct }

        if (existingItem != null) {
            // 3. El producto existe: Aumentar la cantidad en 1
            existingItem.quantity += add
            if (existingItem.quantity <= 0) {
                currentCart.remove(existingItem)
            }
        } else {
            // 4. El producto es nuevo: Añadirlo con cantidad 1
            currentCart.add(CartItem(newProduct, quantity = 1))
        }

        // 5. Guardar la lista modificada en SharedPreferences
        saveCart(currentCart)
    }

    // ----------------------------------------------------
    // FUNCIONES INTERNAS (Lectura y Escritura)
    // ----------------------------------------------------
    fun getCart(): MutableList<CartItem> {
        val json = prefs.getString(KEY_CART, null)

        return if (json != null) {

            val type = object : TypeToken<MutableList<CartItem>>() {}.type
            gson.fromJson(json, type)
        } else {
            mutableListOf()
        }
    }

    // Función para guardar la lista del carrito en SharedPreferences
    private fun saveCart(cart: List<CartItem>) {
        val json = gson.toJson(cart)
        prefs.edit().putString(KEY_CART, json).apply()
    }

    // Función de utilidad: Borrar todo el carrito (opcional)
    fun clearCart() {
        prefs.edit().remove(KEY_CART).apply()
    }
}