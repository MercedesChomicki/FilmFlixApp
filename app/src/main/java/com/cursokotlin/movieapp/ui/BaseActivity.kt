package com.cursokotlin.movieapp.ui

import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.viewbinding.ViewBinding
import com.cursokotlin.movieapp.databinding.ActivityMainBinding
import com.cursokotlin.movieapp.databinding.ErrorLayoutBinding

open class BaseActivity : AppCompatActivity() {

    protected fun setLoadingState(isLoading: Boolean, progressBar: ProgressBar, contentLayout: View) {
        contentLayout.isVisible = !isLoading
        progressBar.isVisible = isLoading
    }

    protected fun showErrorState(binding: ViewBinding, message: String, errorLayout: ErrorLayoutBinding, colorId: Int) {
        updateBackgroundColor(binding, colorId)
        errorLayout.root.visibility = View.VISIBLE
        errorLayout.errorMessage.text = message
        if(binding is ActivityMainBinding)
            binding.recyclerView.visibility = View.GONE
    }

    protected fun hideErrorState(binding: ViewBinding, errorLayout: ErrorLayoutBinding, colorId: Int ) {
        // Resetea el fondo y oculta la vista de error si no hay error
        updateBackgroundColor(binding, colorId)
        errorLayout.root.visibility = View.GONE
    }

    private fun updateBackgroundColor(binding: ViewBinding, colorId: Int) {
        val color = ContextCompat.getColor(this, colorId)
        if(binding is ActivityMainBinding)
            binding.mainLayout.background = ColorDrawable(color)
    }
}