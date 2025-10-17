package com.grupo2.ladorada

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import coil.load
import com.grupo2.ladorada.adapter.ProductAdapter
import com.grupo2.ladorada.databinding.FragmentCatalogBinding
import com.grupo2.ladorada.databinding.FragmentProductBinding
import com.grupo2.ladorada.dto.Product
import com.grupo2.ladorada.utils.ApiClient
import com.grupo2.ladorada.utils.CartManager
import com.grupo2.ladorada.utils.LoadingService
import com.grupo2.ladorada.utils.MsgService
import com.grupo2.ladorada.utils.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductFragment : Fragment() {
    private var _binding: FragmentProductBinding? = null
    private val binding get() = _binding!!
    private lateinit var msgBox: MsgService
    private lateinit var token: TokenManager
    private lateinit var loader: LoadingService
    private lateinit var cartManager: CartManager

    private lateinit var product: Product

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        msgBox = MsgService(requireContext())
        token = TokenManager(requireContext())
        loader = LoadingService(requireContext())
        cartManager = CartManager(requireContext())
        _binding = FragmentProductBinding.inflate(inflater, container, false)
        loadData();
        return binding.root
    }

    fun loadData(){
        lifecycleScope.launch {
            loader.show("Obteniendo dellate de Productos...")
            val idProduct = arguments?.getInt(ARG_PRODUCT_ID).toString()
            val response = ApiClient.getData<Product>("product/"+idProduct)
            if (response.success) {
                product = response.data!!
                binding.imgProduct.load(product.image) {
                    placeholder(R.drawable.product_placeholder)
                    error(R.drawable.product_placeholder)
                    crossfade(true)
                    allowHardware(false)
                }
                binding.txtProductName.text = product.name
                binding.txtProductPrice.text = "Precio : S/ " + product.price.toString()
                binding.txtDescrip.text = product.description
                binding.txtDetail.text = product.detail
            } else {
                withContext(Dispatchers.Main) {
                    msgBox.showDialog(
                        response.message ?: "Error desconocido",
                        MsgService.MessageType.ERROR
                    )
                }
            }
            loader.hide()
        }
        binding.btnAddCart.setOnClickListener {
            onAddToCartClicked()
        }
    }

    fun onAddToCartClicked() {
        val productToAdd = product
        if (productToAdd != null) {
            cartManager.addToCart(productToAdd)
            val currentCart = cartManager.getCart()
            val totalItems = currentCart.sumOf { it.quantity }
            msgBox.showDialog("¡Producto agregado! Total en carrito: $totalItems", MsgService.MessageType.SUCCESS)
        } else {
            msgBox.showDialog("Error: Producto no encontrado.", MsgService.MessageType.ERROR)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // evita memory leaks
    }

    companion object {
        private const val ARG_PRODUCT_ID = "idProduct"

        // Función de fábrica para crear la instancia con argumentos
        fun newInstance(idProduct: Int): ProductFragment {
            val fragment = ProductFragment()
            val args = Bundle().apply {
                putInt(ARG_PRODUCT_ID, idProduct)
            }
            fragment.arguments = args
            return fragment
        }
    }


}