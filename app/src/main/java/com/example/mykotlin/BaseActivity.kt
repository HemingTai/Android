package com.example.mykotlin

import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

open class BaseActivity : AppCompatActivity(), CoroutineScope {
    private var job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() {
            if (!job.isActive) job = Job()
            return Dispatchers.Main + job
        }


    fun runSuspend(
        context: CoroutineContext = Dispatchers.Main,
        closure: suspend CoroutineScope.() -> Unit
    ): Job = launch(context + generalExceptionHandler) {
        closure()
    }

}