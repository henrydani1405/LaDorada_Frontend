package com.grupo2.ladorada

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.grupo2.ladorada.adapter.CarritoAdapter
import com.grupo2.ladorada.databinding.FragmentCartBinding
import com.grupo2.ladorada.dto.Product
import com.grupo2.ladorada.utils.CartManager
import com.grupo2.ladorada.utils.LoadingService
import com.grupo2.ladorada.utils.MsgService
import com.grupo2.ladorada.utils.TokenManager

class CartFragment : Fragment(), CarritoAdapter.CarritoClickListener {
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private lateinit var msgBox: MsgService
    private lateinit var token: TokenManager
    private lateinit var loader: LoadingService
    private lateinit var cartManager: CartManager
    private lateinit var adapter: CarritoAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        msgBox = MsgService(requireContext())
        token = TokenManager(requireContext())
        loader = LoadingService(requireContext())
        cartManager = CartManager(requireContext())
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        loadCarrito();
        return binding.root
    }
    fun loadCarrito(){
        val currentCart = cartManager.getCart()
        //val totalItems = currentCart.sumOf { it.quantity }
        val total = currentCart.sumOf { it.product.price * it.quantity }

        binding.txtSubtotal.text = "Subtotal : S/. ${total}"

        adapter = CarritoAdapter(currentCart, this@CartFragment)
        if (adapter == null) {
            loadCarrito() // Cargar datos por primera vez
        } else {
            binding.carritoProductos.layoutManager = GridLayoutManager(requireContext(), 1)
            binding.carritoProductos.adapter = adapter
        }

        if (token.isLoggedIn()){
            binding.btnPagar.visibility = View.VISIBLE
            binding.btnLogin.visibility = View.GONE
        }else{
            binding.btnPagar.visibility = View.GONE
            binding.btnLogin.visibility = View.VISIBLE
        }

        binding.btnPagar.setOnClickListener {
            val checkOutFragment = CheckOutFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.frame_container, checkOutFragment)
                .addToBackStack(null)
                .commit()
        }

        binding.btnLogin.setOnClickListener {
            val loginFragment = LoginFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.frame_container, loginFragment)
                .addToBackStack(null)
                .commit()
        }

    }
    override fun onAddToCartClicked(product: Product, add: Int){
        cartManager.addToCart(product,add)
        binding.carritoProductos.refreshDrawableState()
        loadCarrito()
    }
}