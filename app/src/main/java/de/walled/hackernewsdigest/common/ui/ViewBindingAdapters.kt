package de.walled.hackernewsdigest.common.ui

import android.databinding.BindingAdapter
import android.view.View

@BindingAdapter("visible")
fun setVisible(view: View, isVisible: Boolean) {
    if (isVisible) {
        view.visibility = View.VISIBLE
    } else {
        view.visibility = View.GONE
    }
}
