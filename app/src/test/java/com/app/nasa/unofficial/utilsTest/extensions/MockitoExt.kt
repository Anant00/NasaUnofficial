package com.app.nasa.unofficial.utilsTest.extensions

import org.mockito.Mockito

inline fun <reified T> mock(): T = Mockito.mock(T::class.java)