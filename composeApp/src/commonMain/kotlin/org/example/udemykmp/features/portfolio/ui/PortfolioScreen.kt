package org.example.udemykmp.features.portfolio.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.example.udemykmp.theme.localAppColorPalette
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PortfolioScreen(
    onCoinClick: (String) -> Unit,
    onBuyCoinClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val vm = koinViewModel<PortfolioViewModel>()
    val state by vm.state.collectAsStateWithLifecycle()

    if (state.isLoading) {
        //TODO: refactor to core/ui/gallery
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator(
                color = localAppColorPalette.current.profitGreen,
                modifier = Modifier.size(32.dp)
            )
        }
    } else {
        PortfolioScreenContent(
            state = state,
            onCoinClick = onCoinClick,
            onBuyCoinClick = onBuyCoinClick,
            modifier = modifier
        )
    }
}

@Composable
private fun PortfolioScreenContent(
    state: PortfolioViewState,
    onCoinClick: (String) -> Unit,
    onBuyCoinClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.inversePrimary),
    ) {
        Balance(
            portfolioValue = state.portfolioValue,
            cashBalance = state.cashBalance,
            isBuyButtonShown = state.isBuyButtonShown,
            onBuyButtonClick = onBuyCoinClick,
        )
//        CoinsList()
    }
}

@Composable
private fun Balance(
    portfolioValue: String,
    cashBalance: String,
    isBuyButtonShown: Boolean,
    onBuyButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxHeight(fraction = 0.3f)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.inversePrimary)
            .padding(32.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.weight(1f))
            Text(
                text = "Total value", //TODO: strings
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = MaterialTheme.typography.titleSmall.fontSize,
            )

            Spacer(Modifier.height(8.dp))
            Text(
                text = portfolioValue,
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = MaterialTheme.typography.headlineLarge.fontSize,
            )

            Row {
                Text(
                    text = "Cash balance", //TODO: strings
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = cashBalance,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                )
            }

            if (isBuyButtonShown) {
                Spacer(Modifier.weight(1f))
                Button(
                    onClick = onBuyButtonClick,
                    colors = ButtonDefaults.buttonColors().copy(containerColor = localAppColorPalette.current.profitGreen),
                    contentPadding = PaddingValues(horizontal = 64.dp),
                ) {
                    Text(
                        text = "Buy coin", //TODO: strings
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}