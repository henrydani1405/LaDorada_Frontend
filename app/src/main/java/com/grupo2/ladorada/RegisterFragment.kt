package com.grupo2.ladorada

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.grupo2.ladorada.adapter.CountryAdapter
import com.grupo2.ladorada.databinding.FragmentCheckOutBinding
import com.grupo2.ladorada.databinding.FragmentRegisterBinding
import com.grupo2.ladorada.dto.Country
import com.grupo2.ladorada.dto.Coupon
import com.grupo2.ladorada.dto.Product
import com.grupo2.ladorada.dto.TokenData
import com.grupo2.ladorada.utils.ApiClient
import com.grupo2.ladorada.utils.CartManager
import com.grupo2.ladorada.utils.LoadingService
import com.grupo2.ladorada.utils.MsgService
import com.grupo2.ladorada.utils.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var msgBox: MsgService
    private lateinit var loader: LoadingService

    private var country: Country? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        msgBox = MsgService(requireContext())
        loader = LoadingService(requireContext())
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        loadCountry()
        saveAcc()
        return binding.root
    }

    fun loadCountry() {

        lifecycleScope.launch {
            loader.show("Obteniendo Lista de Paises...")
            val response = ApiClient.getData<List<Country>>("country")

            if (response.success) {
                val countries = response.data!!
                val adapter = CountryAdapter(requireContext(), countries)

                binding.inputPais.setAdapter(adapter)
                binding.inputPais.setOnItemClickListener { _, _, position, _ ->
                    country = countries[position]
                    binding.inputPais.setText(country?.name, false)
                }

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

    fun saveAcc() {
        binding.btnCrearCuenta.setOnClickListener {
            if (binding.checkTerminos.isChecked) {
                val body = mapOf(
                    "first_name" to binding.inputNombre.text.toString(),
                    "last_name" to binding.inputApellido.text.toString(),
                    "email" to binding.inputEmail.text.toString(),
                    "password" to binding.inputPassword.text.toString(),
                    "id_country" to country?.idCountry.toString(),
                    "phone" to binding.inputTelefono.text.toString(),
                    "address_line" to binding.inputDireccion.text.toString(),
                    "city" to binding.inputCiudad.text.toString(),
                    "state" to binding.inputDistrito.text.toString()
                )
                lifecycleScope.launch {
                    loader.show("Creando Cuenta...")
                    val response = ApiClient.postData<TokenData>("auth/register", body)
                    println(response)
                    if (response.success == true) {

                        msgBox.showDialog(
                            response.message ?: "Error desconocido",
                            MsgService.MessageType.SUCCESS
                        )
                        (activity as? MainActivity)?.loadFragment(LoginFragment())
                    } else {
                        msgBox.showDialog(
                            response.message ?: "Error desconocido",
                            MsgService.MessageType.ERROR
                        )
                    }
                    loader.hide()
                }
            } else {
                msgBox.showDialog(
                    "Debes aceptar los términos y condiciones",
                    MsgService.MessageType.WARNING
                )
            }
        }
    }
}