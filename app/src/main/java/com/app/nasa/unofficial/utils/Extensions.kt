package com.app.nasa.unofficial.utils

import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import org.reactivestreams.Publisher

fun Any.showLog(msg: String?) {
    msg?.let { Log.d(this::class.java.simpleName, msg) }
}

fun Context.showToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}

fun <T> Publisher<T>.toLiveData() = LiveDataReactiveStreams.fromPublisher(this) as LiveData<T>

fun <T : Any?> Observable<T>.toLiveData(strategy: BackpressureStrategy = BackpressureStrategy.LATEST) =
    toFlowable(strategy).toLiveData()

@WorkerThread
fun <T> List<T>.combineWith(list: List<T>?): List<T> {
    var newList = emptyList<T>()
    if (!list.isNullOrEmpty()) {
        newList = this.toMutableList()
        newList.addAll(list)
        return newList.toList()
    }
    return newList
}

fun ImageView.setLayoutHeight(): LinearLayout.LayoutParams {
    val height: Int = Resources.getSystem().displayMetrics.heightPixels
    val layoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(this.layoutParams)
    layoutParams.height = (height * 25) / 100
    return layoutParams
}