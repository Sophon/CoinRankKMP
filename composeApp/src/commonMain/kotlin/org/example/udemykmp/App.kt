package org.example.udemykmp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.example.udemykmp.core.navigation.Destination
import org.example.udemykmp.features.coins.ui.CoinsListScreen
import org.example.udemykmp.features.portfolio.ui.PortfolioScreen
import org.example.udemykmp.features.trade.ui.TradeScreen
import org.example.udemykmp.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    val navHostController = rememberNavController()

    AppTheme {
        NavHost(
            navController = navHostController,
            startDestination = Destination.Portfolio,
            modifier = Modifier.fillMaxSize()
        ) {
            composable<Destination.Portfolio> {
                PortfolioScreen(
                    onCoinClick = { coinId ->
                        navHostController.navigate(Destination.Trade)
                    },
                    onDiscoverClick = { navHostController.navigate(Destination.Coins) },
                )
            }

            composable<Destination.Coins> {
                CoinsListScreen(
                    onCoinClick = { coinId ->
                        navHostController.navigate(Destination.Trade)
                    }
                )
            }

            composable<Destination.Trade> { navBackstackEntry ->
                val mockCoinId = "Qwsogvtv82FCd"
                TradeScreen(
                    coinId = mockCoinId,
                    navigateToPortfolio = {
                        navHostController.navigate(Destination.Portfolio) {
                            popUpTo(Destination.Portfolio) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}