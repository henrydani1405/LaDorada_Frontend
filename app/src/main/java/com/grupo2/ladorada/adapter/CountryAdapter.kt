package com.grupo2.ladorada.adapter
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import coil.load
import com.grupo2.ladorada.dto.Country
import com.grupo2.ladorada.R

class CountryAdapter(context: Context, private val countries: List<Country>) :
    ArrayAdapter<Country>(context, 0, countries) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, convertView, parent)
    }

    private fun createView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_country, parent, false)

        val imgFlag = view.findViewById<ImageView>(R.id.imgFlag)
        val txtName = view.findViewById<TextView>(R.id.txtCountryName)
        val country = countries[position]
        imgFlag.load(country.imagen) {
            placeholder(R.drawable.product_placeholder)
            error(R.drawable.product_placeholder)
            crossfade(true)
        }
        txtName.text = country.name
        return view
    }
}
