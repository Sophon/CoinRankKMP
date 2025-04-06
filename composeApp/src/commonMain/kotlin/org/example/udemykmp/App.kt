package org.example.udemykmp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.example.udemykmp.core.navigation.Destination
import org.example.udemykmp.core.navigation.encodeType
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
                        navHostController.navigate(Destination.Trade(coinId, encodeType(Destination.Trade.Type.SELL)))
                    },
                    onDiscoverClick = { navHostController.navigate(Destination.Coins) },
                )
            }

            composable<Destination.Coins> {
                CoinsListScreen(
                    onCoinClick = { coinId ->
                        navHostController.navigate(Destination.Trade(coinId, encodeType(Destination.Trade.Type.BUY)))
                    }
                )
            }

            composable<Destination.Trade> { navBackstackEntry ->
                TradeScreen(
                    coinId = navBackstackEntry.toRoute<Destination.Trade>().coinId,
                    transactionType = navBackstackEntry.toRoute<Destination.Trade>().tradeType,
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