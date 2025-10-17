package com.grupo2.ladorada

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.grupo2.ladorada.databinding.FragmentLoginBinding;
import com.grupo2.ladorada.dto.TokenData
import com.grupo2.ladorada.utils.ApiClient
import com.grupo2.ladorada.utils.LoadingService
import com.grupo2.ladorada.utils.MsgService
import com.grupo2.ladorada.utils.TokenManager
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
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
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        funcionalidades();
        return binding.root
    }

    fun funcionalidades(){
        token.clearSession();

        binding.lknRegister.setOnClickListener {
            (activity as? MainActivity)?.loadFragment(RegisterFragment())
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.txtEmail.text.toString()
            val password = binding.txtPassword.text.toString()

            lifecycleScope.launch {
                loader.show("Accediendo...")
                val body = mapOf("email" to email, "password" to password)
                val response = ApiClient.postData<TokenData>("auth/login", body)

                if (response.success == true) {
                    val tokenData = response.data
                    if (tokenData != null) {
                        token.saveToken(tokenData.accessToken)
                        token.saveString("IdUser", tokenData.idUser.toString())
                        (activity as? MainActivity)?.updateBottomNavigation(token.isLoggedIn())
                        (activity as? MainActivity)?.loadFragment(ProfileFragment())
                    }
                } else {
                    msgBox.showDialog(response.message ?: "Error desconocido", MsgService.MessageType.ERROR)
                }
                loader.hide()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // evita memory leaks
    }

}