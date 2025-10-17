package com.grupo2.ladorada.utils

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.grupo2.ladorada.R

class LoadingService(private val context: Context) {

    private var dialog: AlertDialog? = null

    fun show(message: String = "Cargando...") {
        if (dialog?.isShowing == true) return

        val builder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.dialog_loading, null)
        val textView = view.findViewById<android.widget.TextView>(R.id.txtMessage)
        textView.text = message

        builder.setView(view)
        builder.setCancelable(false)
        dialog = builder.create()
        dialog?.show()
    }

    fun hide() {
        dialog?.dismiss()
        dialog = null
    }
}
