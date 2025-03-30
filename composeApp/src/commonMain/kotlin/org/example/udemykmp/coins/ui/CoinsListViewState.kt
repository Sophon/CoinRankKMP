package org.example.udemykmp.coins.ui

import androidx.compose.runtime.Stable
import org.jetbrains.compose.resources.StringResource

@Stable
data class CoinsListViewState(
    val error: StringResource? = null,
    val coins: List<UiCoinsListItem> = emptyList(),
    val coinPriceHistoryState: CoinPriceHistoryState? = null,
)

data class UiCoinsListItem(
    val id: String,
    val name: String,
    val symbol: String,
    val iconUrl: String,
    val formattedPrice: String,
    val formattedChange: String,
    val isPositive: Boolean,
)

data class CoinPriceHistoryState(
    val coinName: String = "",
    val isLoading: Boolean = true,
    val points: List<Double> = emptyList(),
)