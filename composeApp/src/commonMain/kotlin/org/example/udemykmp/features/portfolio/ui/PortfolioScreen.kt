package org.example.udemykmp.features.portfolio.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import org.example.udemykmp.theme.localAppColorPalette
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import udemykmp.composeapp.generated.resources.Res
import udemykmp.composeapp.generated.resources.btn_portfolio_buy
import udemykmp.composeapp.generated.resources.btn_portfolio_discover
import udemykmp.composeapp.generated.resources.title_portfolio_owned
import udemykmp.composeapp.generated.resources.title_portfolio_total_value
import udemykmp.composeapp.generated.resources.txt_portfolio_cash_balance
import udemykmp.composeapp.generated.resources.txt_portfolio_no_coins

@Composable
fun PortfolioScreen(
    onCoinClick: (String) -> Unit,
    onBuyCoinClick: () -> Unit,
    onDiscoverClick: () -> Unit,
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
            onDiscoverClick = onDiscoverClick,
            modifier = modifier
        )
    }
}

@Composable
private fun PortfolioScreenContent(
    state: PortfolioViewState,
    onCoinClick: (String) -> Unit,
    onBuyCoinClick: () -> Unit,
    onDiscoverClick: () -> Unit,
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

        PortfolioCoinsList(
            coins = state.coins,
            onCoinClick = onCoinClick,
            onDiscoverClick = onDiscoverClick,
        )
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
                text = stringResource(Res.string.title_portfolio_total_value),
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
                    text = stringResource(Res.string.txt_portfolio_cash_balance),
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
                        text = stringResource(Res.string.btn_portfolio_buy),
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@Composable
private fun PortfolioCoinsList(
    coins: List<UiPortfolioCoin>,
    onCoinClick: (String) -> Unit,
    onDiscoverClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
    ) {
        if (coins.isEmpty()) {
            EmptyPortfolioSection(onDiscoverClick)
            return@Box
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = stringResource(Res.string.title_portfolio_owned),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp)
                )

                Spacer(Modifier.height(8.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(coins) { coin ->
                        PortfolioCoinListItem(
                            coin = coin,
                            onCoinClick = onCoinClick,
                        )
                    }
                }
            }
        }
    }
}

//TODO: refactor to core/ui/gallery with the coinlist item variant
@Composable
private fun PortfolioCoinListItem(
    coin: UiPortfolioCoin,
    onCoinClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable {onCoinClick(coin.id) }
            .padding(16.dp)
    ) {
        AsyncImage(
            model = coin.iconUrl,
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .padding(4.dp)
                .clip(CircleShape)
                .size(40.dp)
        )
        Spacer(Modifier.width(16.dp))
        Column(
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = coin.name,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = coin.amountInUnit,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
            )
        }
        Spacer(Modifier.width(16.dp))
        Column(
            horizontalAlignment = Alignment.End,
        ) {
            Text(
                text = coin.amountInFiat,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = coin.performancePercent,
                color = if (coin.isPositivePerformance) localAppColorPalette.current.profitGreen else localAppColorPalette.current.lossRed,
                fontSize = MaterialTheme.typography.titleSmall.fontSize,
            )
        }
    }
}

@Composable
private fun EmptyPortfolioSection(
    onDiscoverClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp)
    ) {
        Text(
            text = stringResource(Res.string.txt_portfolio_no_coins),
            color = MaterialTheme.colorScheme.primary,
            fontSize = MaterialTheme.typography.titleSmall.fontSize,
        )
        Button(
            onClick = onDiscoverClick,
            colors = ButtonDefaults.buttonColors(containerColor = localAppColorPalette.current.profitGreen),
            contentPadding = PaddingValues(horizontal = 64.dp),
        ) {
            Text(
                text = stringResource(Res.string.btn_portfolio_discover),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }
    }
}