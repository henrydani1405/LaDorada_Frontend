package com.grupo2.ladorada

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.grupo2.ladorada.databinding.ActivityMainBinding
import com.grupo2.ladorada.databinding.FragmentCatalogBinding
import com.grupo2.ladorada.utils.TokenManager


class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    val homeFragment = HomeFragment()
    val catalogFragment = CatalogFragment()
    val cartFragment = CartFragment()
    val profileFragment = ProfileFragment()
    val loginFragment= LoginFragment()
    val ordersFragment = OrdersFragment()

    val registerFragment = RegisterFragment()
    private lateinit var token: TokenManager



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        token = TokenManager(this)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        token = TokenManager(this) //
        val nav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        updateBottomNavigation(token.isLoggedIn())

        // ✅ Mostrar el primer fragmento por defecto
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_container, homeFragment)
            .commit()

        nav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    loadFragment(homeFragment)
                    true
                }
                R.id.catalog -> {
                    loadFragment(catalogFragment)
                    true
                }
                R.id.cart -> {
                    loadFragment(cartFragment)
                    true
                }
                R.id.user -> {
                    if (token.isLoggedIn()) loadFragment(profileFragment)
                    else loadFragment(loginFragment)
                    true
                }
                R.id.orders -> {
                    loadFragment(ordersFragment)
                    true
                }
                else -> false
            }
        }

        binding.btnClose.setOnClickListener {
            token.clearSession()
            updateBottomNavigation(false)
            loadFragment(LoginFragment())
        }

        //loadConytry()


    }





    fun updateBottomNavigation(isLoggedIn: Boolean) {
        val nav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val ordersItem = nav.menu.findItem(R.id.orders)
        if (token.isLoggedIn()) {
            ordersItem.isVisible = true
            binding.btnClose.isVisible = true
        } else {
            ordersItem.isVisible = false
            binding.btnClose.isVisible = false
        }
    }
    fun loadFragment(fragment: androidx.fragment.app.Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_container, fragment)
            .commit()
    }

}