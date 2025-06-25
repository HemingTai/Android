package com.example.mykotlin

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

fun MutableList<Int>.swap(i: Int, j: Int) {
    val temp = this[i]
    this[i] = this[j]
    this[j] = temp
}

//扩展属性
val String.size: Int
    get() = this.length

fun String.isEmailValid(): Boolean {
    val reg = "W+(@)[a-zA-z0-9]+(\\.)+[a-zA-z0-9]+".toRegex()
    return reg.matches(this)
}

val generalExceptionHandler = CoroutineExceptionHandler { _, e ->
    println("****** ${e.message} ******")
}

val defaultScope = CoroutineScope(Dispatchers.Default)

fun runSuspendInDefaultScope(
    context: CoroutineContext = Dispatchers.Main,
    closure: suspend CoroutineScope.() -> Unit
): Job = defaultScope.launch(context + generalExceptionHandler) {
    closure()
}

fun ViewModel.runSuspend(
    context: CoroutineContext = Dispatchers.Main.immediate,
    closure: suspend CoroutineScope.() -> Unit
): Job = viewModelScope.launch(context + generalExceptionHandler) {
    closure()
}

fun Fragment.runSuspend(
    context: CoroutineContext = Dispatchers.Main.immediate,
    closure: suspend CoroutineScope.() -> Unit
): Job = activity?.runSuspend(context, closure) ?: runSuspendInDefaultScope(context, closure)

fun Context.runSuspend(
    context: CoroutineContext = Dispatchers.Main,
    closure: suspend CoroutineScope.() -> Unit
): Job = when (this) {
    is FirstActivity -> runSuspend(context, closure)
    else -> runSuspendInDefaultScope(context, closure)
}