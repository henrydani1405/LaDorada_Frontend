package com.grupo2.ladorada

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.grupo2.ladorada.adapter.OrdersAdapter
import com.grupo2.ladorada.databinding.FragmentOrdersBinding
import com.grupo2.ladorada.dto.Order
import com.grupo2.ladorada.utils.ApiClient
import com.grupo2.ladorada.utils.CartManager
import com.grupo2.ladorada.utils.LoadingService
import com.grupo2.ladorada.utils.MsgService
import com.grupo2.ladorada.utils.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OrdersFragment : Fragment(), OrdersAdapter.OrdersClickListener {
    private var _binding: FragmentOrdersBinding? = null
    private val binding get() = _binding!!
    private lateinit var msgBox: MsgService
    private lateinit var token: TokenManager
    private lateinit var loader: LoadingService
    private lateinit var cartManager: CartManager

    // Estado del Fragment: Usamos variables opcionales para persistir el estado al volver.
    private var adapter: OrdersAdapter? = null
    private var orders: List<Order>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        msgBox = MsgService(requireContext())
        token = TokenManager(requireContext())
        loader = LoadingService(requireContext())
        cartManager = CartManager(requireContext())
        _binding = FragmentOrdersBinding.inflate(inflater, container, false)

        if ( orders == null) {
            loadingOrders()
        } else {
            setupRecyclerView()
        }
        return binding.root
    }

    private fun setupRecyclerView() {
        if (orders != null) {
            var total = orders?.size
            binding.txtCount.text = "Ordenes : $total"
            adapter = OrdersAdapter(orders!!, this@OrdersFragment)
            binding.listaOrders.layoutManager = GridLayoutManager(requireContext(), 1)
            binding.listaOrders.adapter = adapter
        }
    }

    fun loadingOrders() {
        lifecycleScope.launch {

            loader.show("Obteniendo Tus Ordenes...")
            var idUser = token.getString("IdUser").toString()
            val response = ApiClient.getData<List<Order>>("order?id_user=${idUser}")
            println(idUser)
            println(response)


            if (response.success && response.data != null) {
                orders = response.data
                setupRecyclerView()
            } else {
                withContext(Dispatchers.Main) {
                    msgBox.showDialog(
                        response.message ?: "Error desconocido al cargar",
                        MsgService.MessageType.ERROR
                    )
                }
            }
            loader.hide()
        }
    }

    override fun onViewFactura(url: String?) {
        println("URL recibida para factura: $url")
        var fullUrl = url
        if (fullUrl != null && !fullUrl.startsWith("http")) {
            fullUrl = "https://$fullUrl"
        }

        if (fullUrl != null && fullUrl.isNotEmpty()) {
            try {
                val webpage: Uri = Uri.parse(fullUrl)
                val intent = Intent(Intent.ACTION_VIEW, webpage)
                intent.setDataAndType(webpage, "application/pdf")
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

                if (intent.resolveActivity(requireContext().packageManager) != null) {
                    startActivity(intent)
                } else {
                    startActivity(Intent(Intent.ACTION_VIEW, webpage))
                }

            } catch (e: Exception) {
                msgBox.showDialog(
                    "Error al procesar la URL: ${e.message}",
                    MsgService.MessageType.ERROR
                )
            }
        } else {
            msgBox.showDialog("La URL de la factura está vacía", MsgService.MessageType.ERROR)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        adapter = null
        orders = null
    }
}