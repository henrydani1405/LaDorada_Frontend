package com.grupo2.ladorada.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.grupo2.ladorada.databinding.ItemOrderBinding
import com.grupo2.ladorada.dto.Order
import com.grupo2.ladorada.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Locale

class OrdersAdapter(
    private val orders: List<Order>,
    private val clickListener: OrdersClickListener
) :

    RecyclerView.Adapter<OrdersAdapter.OrderViewHolder>() {

    interface OrdersClickListener {
        fun onViewFactura(url: String?)
    }

    inner class OrderViewHolder(val binding: ItemOrderBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {

        val binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {

        val Order = orders[position]

        val apiFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val displayFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val fechaEnvio = Order.shippedAt?.let { dateString ->
            try {
                val dateObject = apiFormat.parse(dateString)
                dateObject?.let { displayFormat.format(it) } ?: "Error de formato"

            } catch (e: ParseException) {
                "Error al leer fecha"
            } catch (e: Exception) {
                "Error desconocido"
            }
        } ?: "Pendiente"



        with(holder.binding) {
            println(Order)
            val productCount = Order.orderDetails?.size ?: 0
            holder.binding.txtNro.text = "Nro de Pedido : ${Order.idOrder}"
            holder.binding.txtTotal.text = "Total : S/ ${Order.totalAmount}"
            holder.binding.txtQuantity.text = "Productos : ${productCount}"
            holder.binding.txtfecha.text = "Fecha : ${fechaEnvio}"

            holder.binding.btnViewFactura.setOnClickListener {
                clickListener.onViewFactura(Order.image ) // <--- Llama al Fragment
            }
        }
    }

    override fun getItemCount() = orders.size
}