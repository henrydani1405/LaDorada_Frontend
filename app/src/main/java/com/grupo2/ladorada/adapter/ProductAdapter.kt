package com.grupo2.ladorada.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.grupo2.ladorada.CatalogFragment
import com.grupo2.ladorada.databinding.ItemProductBinding
import com.grupo2.ladorada.dto.Product
import com.grupo2.ladorada.R
import com.grupo2.ladorada.utils.MsgService

class ProductAdapter(
    private val productList: List<Product>,
    private val clickListener: ProductClickListener
) :

    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    interface ProductClickListener {
        fun onAddToCartClicked(productId: Int)
        fun onDetailsClicked(productId: Int)
    }

    inner class ProductViewHolder(val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {

        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {

        val product = productList[position]
        with(holder.binding) {
            holder.binding.txtProductName.text = product.name
            holder.binding.txtProductPrice.text = "S/ ${product.price}"
            holder.binding.imgProduct.load(product.image) {
                placeholder(R.drawable.product_placeholder)
                error(R.drawable.product_placeholder)
                crossfade(true)
            }
            holder.binding.btnAddCart.setOnClickListener {
                clickListener.onAddToCartClicked(product.idProduct) // <--- Llama al Fragment
            }
            holder.binding.btnDetails.setOnClickListener {
                clickListener.onDetailsClicked(product.idProduct) // <--- Llama al Fragment
            }
        }
    }

    override fun getItemCount() = productList.size
}