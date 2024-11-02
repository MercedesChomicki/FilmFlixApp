package com.cursokotlin.movieapp.ui

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.cursokotlin.movieapp.databinding.FragmentErrorDialogBinding

class ErrorDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentErrorDialogBinding

    interface Retryable {
        fun retryConnection()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val title = arguments?.getString("title") ?: "Error"
        val message = arguments?.getString("message") ?: "Ha ocurrido un error."

        // Infla el layout personalizado
        binding = FragmentErrorDialogBinding.inflate(layoutInflater)

        // Configura los textos del título y mensaje
        binding.dialogTitle.text = title
        binding.dialogMessage.text = message

        // Configura el botón "Reintentar"
        binding.retryButton.setOnClickListener {
            (activity as? Retryable)?.retryConnection()
            dismiss() // Cierra el diálogo
        }

        // Configura el botón "Cerrar"
        binding.closeAppButton.setOnClickListener {
            activity?.finish()
            dismiss() // Cierra el diálogo
        }

        // Configura el diálogo para que no se cierre al tocar fuera de su área
        isCancelable = false

        // Crea y devuelve el diálogo
        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()
    }

    companion object {
        // Método de fábrica para crear una instancia del diálogo con título y mensaje
        fun newInstance(title: String, message: String): ErrorDialogFragment {
            val fragment = ErrorDialogFragment()
            val args = Bundle().apply {
                putString("title", title)
                putString("message", message)
            }
            fragment.arguments = args
            return fragment
        }
    }
}