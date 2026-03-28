package org.example.project.di

import org.example.project.game.GameEngine
import org.example.project.game.GameViewModel
import org.koin.dsl.module

val appModule = module {
    single { GameEngine() }
    factory { GameViewModel(get()) }
}.plus(coroutineModule)