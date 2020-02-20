package com.app.nasa.unofficial.utils

import android.os.Parcelable
import com.app.nasa.unofficial.api.apimodel.NasaImages
import kotlinx.android.parcel.Parcelize

@Parcelize
data class IntentUtil(
    var imageList: List<NasaImages>
) : Parcelable