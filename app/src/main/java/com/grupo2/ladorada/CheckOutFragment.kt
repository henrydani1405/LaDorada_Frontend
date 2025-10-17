package com.grupo2.ladorada

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.grupo2.ladorada.databinding.FragmentCheckOutBinding
import com.grupo2.ladorada.databinding.FragmentOrdersBinding
import com.grupo2.ladorada.dto.Coupon
import com.grupo2.ladorada.dto.Order
import com.grupo2.ladorada.dto.TokenData
import com.grupo2.ladorada.utils.ApiClient
import com.grupo2.ladorada.utils.CartManager
import com.grupo2.ladorada.utils.LoadingService
import com.grupo2.ladorada.utils.MsgService
import com.grupo2.ladorada.utils.TokenManager
import kotlinx.coroutines.launch

class CheckOutFragment : Fragment() {
    private var _binding: FragmentCheckOutBinding? = null
    private val binding get() = _binding!!
    private lateinit var msgBox: MsgService
    private lateinit var token: TokenManager
    private lateinit var loader: LoadingService
    private lateinit var cartManager: CartManager

    private var Cupon: Coupon? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        msgBox = MsgService(requireContext())
        token = TokenManager(requireContext())
        loader = LoadingService(requireContext())
        cartManager = CartManager(requireContext())
        _binding = FragmentCheckOutBinding.inflate(inflater, container, false)
        funcionalidades()
        ShowData()
        findCoupon()
        Pagar()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // CLAVE: Evitar memory leaks
    }

    fun funcionalidades() {
        binding.FieldsTC.isVisible = false
        binding.fieldQR.isVisible = false
        binding.payTc.setOnClickListener {
            binding.FieldsTC.isVisible = true
            binding.fieldQR.isVisible = false
        }
        binding.payPlin.setOnClickListener {
            binding.FieldsTC.isVisible = false
            binding.fieldQR.isVisible = true
        }
        binding.payEntrega.setOnClickListener {
            binding.FieldsTC.isVisible = false
            binding.fieldQR.isVisible = false
        }
    }

    fun ShowData(coupon:Coupon? = null) {
        val currentCart = cartManager.getCart()
        var igv = 0.18;
        var subtotal = currentCart.sumOf { it.product.price * it.quantity }
        var impuesto = subtotal * igv
        var descuento = 0.00
        if (coupon != null) {
            if (coupon.discountType == "percentage") {
                descuento = (subtotal * coupon.discountValue) / 100
            } else {
                descuento = coupon.discountValue
            }
            msgBox.showDialog(
                "Cupón aplicado con éxito "+coupon.code,
                MsgService.MessageType.SUCCESS
            )
        }
        var total = subtotal + impuesto - descuento

        binding.txtSubtotal.text = String.format("%.2f", subtotal)
        binding.txtIgv.text = String.format("%.2f", impuesto)
        binding.txtDescuento.text = String.format("%.2f", descuento)
        binding.txtTotal.text = String.format("%.2f", total)
    }

    fun findCoupon() {
        binding.btnApplyDiscount.setOnClickListener {

            val codDesc = binding.editDiscountCode.text.toString()
            if (!codDesc.isEmpty()) {
                loader.show("Buscando Cupon...")
                lifecycleScope.launch {
                    val response = ApiClient.getData<Coupon>("coupon/${codDesc}")
                    loader.hide()
                    if (response.success) {
                        val coupon = response.data
                        if (coupon != null) {
                            Cupon = coupon
                            ShowData(coupon)
                        }
                    } else {
                        msgBox.showDialog(
                            response.message ?: "Error desconocido",
                            MsgService.MessageType.ERROR
                        )
                    }
                }
                //
            }
        }
    }
    fun Pagar(){
        binding.btnConfirmOrder.setOnClickListener {
            loader.show("Realizando el pago...")
            val currentCart = cartManager.getCart()
            val idUser = token.getString("IdUser").toString()
            val items = currentCart.map { car ->
                mapOf(
                    "id_product" to car.product.idProduct,
                    "quantity" to car.quantity
                )
            }
            val body = mapOf(
                "id_user" to idUser,
                "id_coupon" to Cupon?.idCoupon,
                "items" to items
            )
            lifecycleScope.launch {
                val response = ApiClient.postData<Order>("order", body)
                loader.hide()
                if (response.success) {
                    val order = response.data
                    if (order != null) {
                        msgBox.showDialog(
                            "Orden realizada con éxito con Nro: " + order.idOrder,
                            MsgService.MessageType.SUCCESS
                        )
                        cartManager.clearCart()
                        goOrder()
                    } else {
                        msgBox.showDialog(
                            response.message ?: "Error desconocido",
                            MsgService.MessageType.ERROR
                        )
                    }
                }
            }
        }
    }

    fun goOrder(){
        val ordersFragment = OrdersFragment()
        parentFragmentManager.beginTransaction()
            .replace(R.id.frame_container, ordersFragment)
            .addToBackStack(null)
            .commit()
    }
}