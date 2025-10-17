package com.grupo2.ladorada

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.grupo2.ladorada.databinding.FragmentProfileBinding
import com.grupo2.ladorada.dto.User
import com.grupo2.ladorada.utils.ApiClient
import com.grupo2.ladorada.utils.LoadingService
import com.grupo2.ladorada.utils.MsgService
import com.grupo2.ladorada.utils.TokenManager
import coil.load
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var msgBox: MsgService
    private lateinit var token: TokenManager
    private lateinit var loader: LoadingService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        msgBox = MsgService(requireContext())
        token = TokenManager(requireContext())
        loader = LoadingService(requireContext())
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        loadingData();
        return binding.root
    }

    fun loadingData() {
        var idUser: String = token.getString("IdUser").toString();
        lifecycleScope.launch {
            loader.show("Obteniendo data del Usuario...")
            val response = ApiClient.getData<User>("user/" + idUser)

            if (response.success) {
                var user = response.data!!
                println(user.avatar);
                binding.imgAvatar.load(user.avatar) {
                    placeholder(R.drawable.user_placeholder)
                    error(R.drawable.user_placeholder)
                    crossfade(true)
                    allowHardware(false)
                }

                binding.lblEmail.text = user.email
                binding.lblIdUser.text = "Id User : " + user.idUser
                binding.txtFirstName.setText(user.firstName)
                binding.txtLastName.setText(user.lastName)
                binding.txtPhone.setText(user.phone)
                binding.acPais.setText(user.country.name)

                for (address in user.addresses) {
                    if (address.type == "envio") {
                        binding.txtAddresShipping.setText(address.addressLine)
                        binding.txtAECity.setText(address.city)
                        binding.txtAEState.setText(address.state)
                    }
                    if (address.type == "facturacion") {
                        binding.txtAddressBilling.setText(address.addressLine)
                        binding.txtABCity.setText(address.city)
                        binding.txtABState.setText(address.state)
                    }
                }

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
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // evita memory leaks
    }

}