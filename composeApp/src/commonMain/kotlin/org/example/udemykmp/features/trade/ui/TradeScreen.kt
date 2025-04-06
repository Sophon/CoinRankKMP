package org.example.udemykmp.features.trade.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import coil3.compose.AsyncImage
import org.example.udemykmp.core.navigation.Destination
import org.example.udemykmp.features.trade.ui.components.CenteredAmountTextField
import org.example.udemykmp.theme.localAppColorPalette
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import udemykmp.composeapp.generated.resources.Res
import udemykmp.composeapp.generated.resources.btn_trade_buy
import udemykmp.composeapp.generated.resources.btn_trade_sell
import udemykmp.composeapp.generated.resources.label_trade_buy
import udemykmp.composeapp.generated.resources.label_trade_sell

@Composable
fun TradeScreen(
    coinId: String,
    tradeType: Destination.Trade.Type,
    navigateToPortfolio: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val vm = koinViewModel<TradeViewModel>(
        parameters = { parametersOf(coinId) }
    )
    val state by vm.state.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current

    //region Event handling
    LaunchedEffect(vm.events) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            vm.events.collect { event ->
                when (event) {
                    is TradeEvent.PurchaseSuccess,
                    TradeEvent.SaleSuccess -> navigateToPortfolio()
                }
            }
        }
    }
    //endregion

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier,
        ) {
            CoinInfo(
                coin = state.coin,
                currentCoinPrice = state.currentCoinPrice,
            )

            Spacer(Modifier.height(24.dp))
            Amount(
                tradeType = tradeType,
                amount = state.tradingAmount,
                onAmountChange = vm::onAmountChange,
                availableBalance = when (tradeType) {
                    Destination.Trade.Type.BUY -> state.balance
                    Destination.Trade.Type.SELL -> state.portfolioValue
                },
                error = state.error,
            )
        }

        TradeButton(
            tradeType = tradeType,
            onButtonClick = when (tradeType) {
                Destination.Trade.Type.BUY -> vm::onBuyClick
                Destination.Trade.Type.SELL -> vm::onSellClick
            },
            isEnabled = state.isTradeButtonEnabled,
        )
    }
}

@Composable
private fun CoinInfo(
    coin: UiTradeCoinItem?,
    currentCoinPrice: String,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onBackground,
                shape = RoundedCornerShape(32.dp)
            )
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        AsyncImage(
            model = coin?.iconUrl,
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .padding(4.dp)
                .clip(CircleShape)
                .size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = coin?.name ?: "",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(start = 4.dp, end = 2.dp)
        )

        Text(
            text = "($currentCoinPrice)",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(start = 2.dp, end = 4.dp)
        )
    }
}

@Composable
private fun Amount(
    tradeType: Destination.Trade.Type,
    amount: String,
    onAmountChange: (String) -> Unit,
    availableBalance: String,
    error: StringResource?,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        Text(
            text = when (tradeType) {
                Destination.Trade.Type.BUY -> stringResource(Res.string.label_trade_buy)
                Destination.Trade.Type.SELL -> stringResource(Res.string.label_trade_sell)
            },
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(4.dp)
        )

        CenteredAmountTextField(
            text = amount,
            onTextChange = onAmountChange,
        )

        Text(
            text = "Available: $availableBalance",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(4.dp)
        )

        if (error != null) {
            Text(
                text = stringResource(error),
                style = MaterialTheme.typography.labelLarge,
                color = localAppColorPalette.current.lossRed,
                modifier = Modifier.padding(4.dp),
            )
        }
    }
}

@Composable
private fun BoxScope.TradeButton(
    tradeType: Destination.Trade.Type,
    onButtonClick: () -> Unit,
    isEnabled: Boolean,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onButtonClick,
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = when (tradeType) {
                Destination.Trade.Type.BUY -> localAppColorPalette.current.profitGreen
                Destination.Trade.Type.SELL -> localAppColorPalette.current.lossRed
            }
        ),
        contentPadding = PaddingValues(horizontal = 64.dp),
        enabled = isEnabled,
        modifier = modifier
            .align(Alignment.BottomCenter)
            .padding(bottom = 32.dp)
    ) {
        val text: StringResource
        val color: Color
        when (tradeType) {
            Destination.Trade.Type.BUY -> {
                text = Res.string.btn_trade_buy
                color = MaterialTheme.colorScheme.onPrimary
            }
            Destination.Trade.Type.SELL -> {
                text = Res.string.btn_trade_sell
                color = MaterialTheme.colorScheme.onBackground
            }
        }

        Text(
            text = stringResource(text),
            style = MaterialTheme.typography.bodyLarge,
            color = color,
        )
    }
}
