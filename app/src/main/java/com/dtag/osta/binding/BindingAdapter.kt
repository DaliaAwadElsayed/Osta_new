package com.dtag.osta.binding;

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso

class BindingAdapter {
    companion object {
        @JvmStatic
        @BindingAdapter("imageSrc")
        fun setImageSrc(imageView: ImageView, imageUrl: String?) {
            if (imageUrl != null) {
                Picasso.get().load(imageUrl).into(imageView)
            }
        }
    }
}