package com.cursokotlin.movieapp.ui

import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import com.cursokotlin.movieapp.ddl.models.ErrorState

open class BaseActivity : AppCompatActivity() {

    protected fun setLoadingState(isLoading: Boolean, progressBar: ProgressBar, contentLayout: View, cardView: CardView?) {
        contentLayout.isVisible = !isLoading
        if(cardView != null){
            cardView.isVisible = !isLoading
        }
        progressBar.isVisible = isLoading
    }

    protected fun showErrorDialog(errorState: ErrorState) {
        val dialog = ErrorDialogFragment.newInstance(errorState.title, errorState.message)
        dialog.show(supportFragmentManager, "ErrorDialog")
    }

}