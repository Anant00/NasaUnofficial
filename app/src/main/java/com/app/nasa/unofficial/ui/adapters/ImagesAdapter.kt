package com.app.nasa.unofficial.ui.adapters

import androidx.recyclerview.widget.DiffUtil
import com.app.nasa.unofficial.R
import com.app.nasa.unofficial.api.apimodel.NasaImages

class ImagesAdapter : DataBindingAdapter<NasaImages>(DiffCallBack()) {

    class DiffCallBack : DiffUtil.ItemCallback<NasaImages>() {
        override fun areItemsTheSame(oldItem: NasaImages, newItem: NasaImages): Boolean {
            return oldItem.date == newItem.date
        }

        override fun areContentsTheSame(oldItem: NasaImages, newItem: NasaImages): Boolean {
            return oldItem.date == newItem.date
        }
    }

    override fun getItemViewType(position: Int) = R.layout.item_recyclerview_main
}