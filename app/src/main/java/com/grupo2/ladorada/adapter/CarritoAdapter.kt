package com.grupo2.ladorada.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.grupo2.ladorada.databinding.ItemCarritoBinding
import com.grupo2.ladorada.dto.Product
import com.grupo2.ladorada.R
import com.grupo2.ladorada.dto.CartItem

class CarritoAdapter(
    private val productList: List<CartItem>,
    private val clickListener: CarritoClickListener
) :

    RecyclerView.Adapter<CarritoAdapter.CarritoViewHolder>() {

    interface CarritoClickListener {
        fun onAddToCartClicked(product:Product,add:Int)
    }

    inner class CarritoViewHolder(val binding: ItemCarritoBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarritoViewHolder {

        val binding = ItemCarritoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CarritoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarritoViewHolder, position: Int) {

        val carrito = productList[position]
        with(holder.binding) {
            holder.binding.txtProductName.text = carrito.product.name
            holder.binding.txtProductPrice.text = "Precio : S/ ${carrito.product.price}"
            holder.binding.txtQuantity.text = "Cantidad : ${carrito.quantity}"
            holder.binding.txtTotal.text = "Total : S/ ${carrito.product.price * carrito.quantity}"
            holder.binding.imgProduct.load(carrito.product.image) {
                placeholder(R.drawable.product_placeholder)
                error(R.drawable.product_placeholder)
                crossfade(true)
            }
            holder.binding.btnPlus.setOnClickListener {
                clickListener.onAddToCartClicked(carrito.product,1    ) // <--- Llama al Fragment
            }
            holder.binding.btnMinus.setOnClickListener {
                clickListener.onAddToCartClicked(carrito.product,-1    ) // <--- Llama al Fragment
            }
        }
    }
    override fun getItemCount() = productList.size
}