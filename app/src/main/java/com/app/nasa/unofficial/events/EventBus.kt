package com.app.nasa.unofficial.events

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

object EventBus {

    private val publisherSubject = PublishSubject.create<Any>()

    fun publish(event: Any) {
        publisherSubject.onNext(event)
    }

    fun <T> listen(eventType: Class<T>): Observable<T> = publisherSubject.ofType(eventType)
}