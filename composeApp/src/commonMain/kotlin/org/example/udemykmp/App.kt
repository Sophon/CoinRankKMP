package org.example.udemykmp

import androidx.compose.runtime.Composable
import org.example.udemykmp.features.coins.ui.CoinsListScreen
import org.example.udemykmp.features.portfolio.ui.PortfolioScreen
import org.example.udemykmp.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    AppTheme {
//        CoinsListScreen {}
        PortfolioScreen({}, {}, {})
    }
}