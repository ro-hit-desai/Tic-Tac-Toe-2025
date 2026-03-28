package org.example.project.di

import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

private var koinStarted = false

fun initKoin() {
    if (!koinStarted) {
        startKoin {
            modules(appModule)
        }
        koinStarted = true
    }
}

fun disposeKoin() {
    stopKoin()
    koinStarted = false
}