package org.example.project

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import kotlinx.coroutines.CoroutineExceptionHandler
import org.koin.core.context.startKoin
import org.example.project.di.appModule

fun MainViewController() = ComposeUIViewController {
    // Initialize Koin only once
    AppWithKoin()
}

@Composable
fun AppWithKoin() {
    val koinInitialized = remember { mutableStateOf(false) }
    val (initializationError, setInitializationError) = remember { mutableStateOf<Throwable?>(null) }

    // Handle coroutine exceptions
    val exceptionHandler = remember {
        CoroutineExceptionHandler { _, throwable ->
            setInitializationError(throwable)
        }
    }

    LaunchedEffect(exceptionHandler) {
        try {
            if (!koinInitialized.value) {
                startKoin {
                    modules(appModule)
                }
                koinInitialized.value = true
            }
        } catch (e: Throwable) {
            setInitializationError(e)
        }
    }

    if (initializationError != null) {
        // Show error UI
        FallbackUI("App Error: ${initializationError.message}")
    } else if (koinInitialized.value) {
        SafeApp()
    } else {
        // Show loading state while initializing Koin
        FallbackUI("Initializing...")
    }
}

@Composable
fun SafeApp() {
    val (error, setError) = remember { mutableStateOf<Throwable?>(null) }

    val safeExceptionHandler = remember {
        CoroutineExceptionHandler { _, throwable ->
            setError(throwable)
        }
    }

    LaunchedEffect(safeExceptionHandler) {
        // App initialization logic here
    }

    if (error != null) {
        FallbackUI("Application Error: ${error.message}")
    } else {
        App()
    }
}

@Composable
fun FallbackUI(message: String) {
    androidx.compose.material3.Surface {
        androidx.compose.material3.Text(
            text = message,
            modifier = androidx.compose.ui.Modifier
                .fillMaxSize()
                .wrapContentSize()
        )
    }
}