package com.app.nasa.unofficial.binding

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.app.nasa.unofficial.R
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso

object BindingAdapters {
    @BindingAdapter("visibleGone")
    @JvmStatic
    fun showHide(view: View, show: Boolean) {
        view.visibility = if (show) View.VISIBLE else View.GONE
    }

    @BindingAdapter("imageUrl")
    @JvmStatic
    fun loadImage(view: ImageView, imageUrl: String?) {
        Picasso.get()
            .load(imageUrl)
            .tag("image")
            .placeholder(R.drawable.imgbg)
            .networkPolicy(NetworkPolicy.OFFLINE)
            .into(view, object : Callback {
                override fun onSuccess() {

                }

                override fun onError(e: Exception?) {
                    Picasso.get().load(imageUrl).placeholder(R.drawable.imgbg).into(view)
                }

            })
    }
}
