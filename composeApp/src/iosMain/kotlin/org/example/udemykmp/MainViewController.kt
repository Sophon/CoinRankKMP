package org.example.udemykmp

import androidx.compose.ui.window.ComposeUIViewController
import org.example.udemykmp.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = { initKoin() },
    content = { App() },
)