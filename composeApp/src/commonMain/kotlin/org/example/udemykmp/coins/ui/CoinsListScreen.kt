package org.example.udemykmp.coins.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import org.example.udemykmp.coins.ui.composables.PriceChart
import org.example.udemykmp.theme.localAppColorPalette
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import udemykmp.composeapp.generated.resources.Res
import udemykmp.composeapp.generated.resources.btn_close
import udemykmp.composeapp.generated.resources.title_dlg_coins_history_title

@Composable
fun CoinsListScreen(
    onCoinClick: (String) -> Unit,
) {
    val vm = koinViewModel<CoinsListViewModel>()
    val state by vm.state.collectAsStateWithLifecycle()

    CoinsListScreenContent(
        state = state,
        onCoinClick = onCoinClick,
        onCoinLongClick = vm::showCoinPriceHistory,
        onCoinPriceHistoryDismiss = vm::dismissPriceHistory,
    )
}

@Composable
private fun CoinsListScreenContent(
    state: CoinsListViewState,
    onCoinClick: (String) -> Unit,
    onCoinLongClick: (String) -> Unit,
    onCoinPriceHistoryDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (state.coinPriceHistoryState != null) {
            CoinPriceHistoryDialog(
                historyState = state.coinPriceHistoryState,
                onDismiss = onCoinPriceHistoryDismiss,
            )
        }

        CoinsList(
            coins = state.coins,
            onCoinClick = onCoinClick,
            onCoinLongClick = onCoinLongClick,
        )
    }
}

@Composable
private fun CoinsList(
    coins: List<UiCoinsListItem>,
    onCoinClick: (String) -> Unit,
    onCoinLongClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Text(
                    text = "🔥 Top Coins:",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    modifier = Modifier.padding(16.dp)
                )
            }
            items(coins) { coin ->
                CoinListItem(
                    coin = coin,
                    onCoinClick = onCoinClick,
                    onCoinLongClick = onCoinLongClick,
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CoinListItem(
    coin: UiCoinsListItem,
    onCoinClick: (String) -> Unit,
    onCoinLongClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                onLongClick = { onCoinLongClick(coin.id) },
                onClick = { onCoinClick(coin.id) },
            )
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
                text = coin.symbol,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
            )
        }
        Spacer(Modifier.width(16.dp))
        Column(
            horizontalAlignment = Alignment.End,
        ) {
            Text(
                text = coin.formattedPrice,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = coin.formattedChange,
                color = if (coin.isPositive) localAppColorPalette.current.profitGreen else localAppColorPalette.current.lossRed,
                fontSize = MaterialTheme.typography.titleSmall.fontSize,
            )
        }
    }
}

@Composable
private fun CoinPriceHistoryDialog(
    historyState: CoinPriceHistoryState,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(Res.string.title_dlg_coins_history_title) + " ${historyState.coinName}")
        },
        text = {
            if (historyState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(32.dp))
                }
            } else {
                PriceChart(
                    pricePoints = historyState.points,
                    profitColor = localAppColorPalette.current.profitGreen,
                    lossColor = localAppColorPalette.current.lossRed,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(16.dp),
                )
            }
        },
        confirmButton = {},
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(text = stringResource(Res.string.btn_close))
            }
        },
        modifier = modifier
    )
}