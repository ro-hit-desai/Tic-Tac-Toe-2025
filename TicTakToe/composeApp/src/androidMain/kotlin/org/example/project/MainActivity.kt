package org.example.project

import org.example.project.di.initKoin
import org.example.project.di.disposeKoin
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        initKoin()
        setContent {
            App()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposeKoin()
    }
}
