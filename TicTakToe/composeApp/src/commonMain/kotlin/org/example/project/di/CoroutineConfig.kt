package org.example.project.di

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.dsl.module

val coroutineModule = module {
    single {
        CoroutineExceptionHandler { _, throwable ->
            // Log the error properly
            println("Global coroutine exception caught: ${throwable.message}")
            throwable.printStackTrace()
            // In production, send this to your crash reporting service
        }
    }

    single {
        CoroutineScope(SupervisorJob() + get<CoroutineExceptionHandler>())
    }
}