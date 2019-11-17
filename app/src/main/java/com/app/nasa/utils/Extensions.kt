package com.app.nasa.utils

import android.content.Context
import android.util.Log
import android.widget.Toast

fun Context.showLog(msg: String) {
    Log.d(this.javaClass.simpleName, msg)
}

fun Context.showToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}
