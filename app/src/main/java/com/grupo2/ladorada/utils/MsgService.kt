package com.grupo2.ladorada.utils


import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import com.grupo2.ladorada.R


class  MsgService (private val context: Context) {
    enum class MessageType { SUCCESS, ERROR, WARNING, INFO }

    fun showDialog(message: String, type: MessageType = MessageType.INFO) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_message, null)

        val icon = dialogView.findViewById<ImageView>(R.id.icon)
        val title = dialogView.findViewById<TextView>(R.id.title)
        val msg = dialogView.findViewById<TextView>(R.id.message)

        // Personalización según el tipo
        when (type) {
            MessageType.SUCCESS -> {
                icon.setImageResource(R.drawable.ic_success)
                title.text = "Éxito"
                title.setTextColor(Color.parseColor("#2E7D32"))
            }
            MessageType.ERROR -> {
                icon.setImageResource(R.drawable.ic_error)
                title.text = "Error"
                title.setTextColor(Color.parseColor("#C62828"))
            }
            MessageType.WARNING -> {
                icon.setImageResource(R.drawable.ic_warning)
                title.text = "Advertencia"
                title.setTextColor(Color.parseColor("#F9A825"))
            }
            MessageType.INFO -> {
                icon.setImageResource(R.drawable.ic_info)
                title.text = "Información"
                title.setTextColor(Color.parseColor("#1565C0"))
            }
        }

        msg.text = message

        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .setPositiveButton("Aceptar", null)
            .create()

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        dialog.show()
    }
}