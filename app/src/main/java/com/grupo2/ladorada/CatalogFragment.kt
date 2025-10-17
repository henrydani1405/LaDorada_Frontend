package com.grupo2.ladorada

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.grupo2.ladorada.adapter.ProductAdapter
import com.grupo2.ladorada.databinding.FragmentCatalogBinding
import com.grupo2.ladorada.dto.Product
import com.grupo2.ladorada.utils.ApiClient
import com.grupo2.ladorada.utils.CartManager
import com.grupo2.ladorada.utils.LoadingService
import com.grupo2.ladorada.utils.MsgService
import com.grupo2.ladorada.utils.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CatalogFragment : Fragment(), ProductAdapter.ProductClickListener {

    // Variables de Binding (Correcto)
    private var _binding: FragmentCatalogBinding? = null
    private val binding get() = _binding!!

    // Servicios (lateinit es aceptable aquí si se inicializan en onCreateView)
    private lateinit var msgBox: MsgService
    private lateinit var token: TokenManager
    private lateinit var loader: LoadingService
    private lateinit var cartManager: CartManager

    // Estado del Fragment: Usamos variables opcionales para persistir el estado al volver.
    private var adapter: ProductAdapter? = null
    private var products: List<Product>? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 1. Inicialización de Servicios (Necesario en cada onCreateView)
        msgBox = MsgService(requireContext())
        token = TokenManager(requireContext())
        loader = LoadingService(requireContext())
        cartManager = CartManager(requireContext())
        _binding = FragmentCatalogBinding.inflate(inflater, container, false)

        // 2. Lógica de Carga Condicional: Evita llamar a la API si ya tenemos los datos.
        if (products == null) {
            loadingProducts()
        } else {
            // Si la vista se recrea (al volver), simplemente re-establece la UI
            setupRecyclerView()
        }

        return binding.root
    }

    private fun setupRecyclerView() {
        // Función dedicada a configurar el RecyclerView
        // Usamos '!!' porque ya hemos comprobado que 'products' no es nulo.
        adapter = ProductAdapter(products!!, this@CatalogFragment)
        binding.recyclerProducts.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerProducts.adapter = adapter
    }


    fun loadingProducts(){
        lifecycleScope.launch {
            loader.show("Obteniendo Catálogo de Productos...")
            val response = ApiClient.getData<List<Product>>("product")

            if (response.success && response.data != null) {
                products = response.data // Guardamos los datos cargados
                setupRecyclerView()      // Configuramos el RecyclerView con los nuevos datos
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

    // --- Implementaciones de ProductAdapter.ProductClickListener ---

    override fun onAddToCartClicked(productId: Int) {
        val productToAdd = findProductById(productId)
        if (productToAdd != null) {
            cartManager.addToCart(productToAdd)
            val currentCart = cartManager.getCart()
            val totalItems = currentCart.sumOf { it.quantity }
            msgBox.showDialog("¡Producto agregado! Total en carrito: $totalItems", MsgService.MessageType.SUCCESS)
        } else {
            msgBox.showDialog("Error: Producto no encontrado.", MsgService.MessageType.ERROR)
        }
    }

    override fun onDetailsClicked(productId: Int) {
        loader.show("Cargando detalles del producto...")
        navigateToProductDetails(productId)
    }

    // --- Funciones de Utilidad ---

    private fun navigateToProductDetails(productId: Int) {
        val productFragment = ProductFragment.newInstance(productId)
        parentFragmentManager.beginTransaction()
            .replace(R.id.frame_container, productFragment)
            .addToBackStack(null)
            .commit()
        loader.hide(); // Ocultar el loader después de la transición
    }

    private fun findProductById(productId: Int): Product? {
        // Usamos '?' para asegurar que no se rompa si 'products' es nulo
        return products?.find { it.idProduct == productId }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // CLAVE: Evitar memory leaks
        adapter = null
        products = null
    }
}