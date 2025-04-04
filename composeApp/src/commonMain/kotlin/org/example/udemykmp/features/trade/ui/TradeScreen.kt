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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import org.example.udemykmp.theme.localAppColorPalette
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import udemykmp.composeapp.generated.resources.Res
import udemykmp.composeapp.generated.resources.btn_trade_buy
import udemykmp.composeapp.generated.resources.btn_trade_sell
import udemykmp.composeapp.generated.resources.label_trade_buy
import udemykmp.composeapp.generated.resources.label_trade_coin_value
import udemykmp.composeapp.generated.resources.label_trade_sell
import udemykmp.composeapp.generated.resources.txt_trade_available_balance

@Composable
fun TradeScreen(
    modifier: Modifier = Modifier,
) {
    val vm = koinViewModel<TradeViewModel>()
    val state by vm.state.collectAsStateWithLifecycle()

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
                tradeType = state.tradeType,
                onAmountChange = vm::onAmountChange,
                availableBalance = when (state.tradeType) {
                    TradeType.BUY -> state.balance
                    TradeType.SELL -> state.portfolioValue
                },
                error = state.error,
            )
        }

        Btn(
            tradeType = state.tradeType,
            onButtonClick = when (state.tradeType) {
                TradeType.BUY -> vm::onBuyClick
                TradeType.SELL -> vm::onSellClick
            }
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
            text = stringResource(Res.string.label_trade_coin_value, currentCoinPrice),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(start = 2.dp, end = 4.dp)
        )
    }
}

@Composable
private fun Amount(
    tradeType: TradeType,
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
                TradeType.BUY -> stringResource(Res.string.label_trade_buy)
                TradeType.SELL -> stringResource(Res.string.label_trade_sell)
            },
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(4.dp)
        )

        //TODO: textfield

        Text(
            text = stringResource(Res.string.txt_trade_available_balance, availableBalance),
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
private fun BoxScope.Btn(
    tradeType: TradeType,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onButtonClick,
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = when (tradeType) {
                TradeType.BUY -> localAppColorPalette.current.profitGreen
                TradeType.SELL -> localAppColorPalette.current.lossRed
            }
        ),
        contentPadding = PaddingValues(horizontal = 64.dp),
        modifier = modifier
            .align(Alignment.BottomCenter)
            .padding(bottom = 32.dp)
    ) {
        val text: StringResource
        val color: Color
        when (tradeType) {
            TradeType.BUY -> {
                text = Res.string.btn_trade_buy
                color = localAppColorPalette.current.profitGreen
            }
            TradeType.SELL -> {
                text = Res.string.btn_trade_sell
                color = localAppColorPalette.current.lossRed
            }
        }

        Text(
            text = stringResource(text),
            style = MaterialTheme.typography.bodyLarge,
            color = color,
        )
    }
}