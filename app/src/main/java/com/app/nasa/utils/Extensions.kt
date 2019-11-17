package com.app.nasa.utils

import android.content.Context
import android.util.Log

fun Context.showLog(msg: String) {
    Log.d(this.javaClass.simpleName, msg)
}
