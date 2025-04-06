package org.example.udemykmp.features.trade.ui

import org.example.udemykmp.core.domain.coin.Coin
import org.example.udemykmp.features.coins.integration.model.CoinModel
import org.jetbrains.compose.resources.StringResource

data class TradeViewState(
    val balance: String = "0.0",
    val portfolioValue: String = "0.0",
    val tradingAmount: String = "",
    val coin: UiTradeCoinItem? = null,
    val currentCoinPrice: String = "0.0",

    val isLoading: Boolean = true,
    val error: StringResource? = null,
) {
    val isTradeButtonEnabled = (coin != null) && (tradingAmount.isNotEmpty())
}

data class UiTradeCoinItem(
    val id: String,
    val name: String,
    val symbol: String,
    val iconUrl: String,
    val price: Double,
)

fun CoinModel.toUiModel(): UiTradeCoinItem {
    return UiTradeCoinItem(
        id = coin.id,
        name = coin.name,
        symbol = coin.symbol,
        iconUrl = coin.iconUrl,
        price = price,
    )
}

fun UiTradeCoinItem.toCoin(): Coin {
    return Coin(
        id = id,
        name = name,
        symbol = symbol,
        iconUrl = iconUrl,
    )
}