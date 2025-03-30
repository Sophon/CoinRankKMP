package org.example.udemykmp.features.coins.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.udemykmp.features.coins.domain.usecases.GetCoinPriceHistoryUseCase
import org.example.udemykmp.features.coins.domain.usecases.GetCoinsListUseCase
import org.example.udemykmp.core.domain.Result
import org.example.udemykmp.core.util.formatFiat
import org.example.udemykmp.core.util.formatPercentage
import org.example.udemykmp.core.util.toUiText

class CoinsListViewModel(
    private val getCoinsListUseCase: GetCoinsListUseCase,
    private val getCoinPriceHistoryUseCase: GetCoinPriceHistoryUseCase,
): ViewModel() {
    private val _state = MutableStateFlow(CoinsListViewState())
    val state = _state
        .onStart {
            getAllCoins()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CoinsListViewState()
        )

    private suspend fun getAllCoins() {
        when (val response = getCoinsListUseCase.execute()) {
            is Result.Success -> {
                _state.update {
                    CoinsListViewState(
                        coins = response.data.map { coinModel ->
                            UiCoinsListItem(
                                id = coinModel.coin.id,
                                name = coinModel.coin.name,
                                iconUrl = coinModel.coin.iconUrl,
                                symbol = coinModel.coin.symbol,
                                formattedPrice = formatFiat(coinModel.price),
                                formattedChange = formatPercentage(coinModel.change),
                                isPositive = (coinModel.change > 0),
                            )
                        }
                    )
                }
            }
            is Result.Error -> {
                _state.update {
                    it.copy(
                        coins = emptyList(),
                        error = response.error.toUiText(),
                    )
                }
            }
        }
    }

    fun showCoinPriceHistory(coinId: String) {
        _state.update {
            it.copy(
                coinPriceHistoryState = CoinPriceHistoryState(pricePoints = emptyList(), isLoading = true)
            )
        }

        viewModelScope.launch {
            when (val response = getCoinPriceHistoryUseCase.execute(coinId)) {
                is Result.Success -> {
                    _state.update { state ->
                        state.copy(
                            coinPriceHistoryState = CoinPriceHistoryState(
                                isLoading = false,
                                coinName = state.coins.find { it.id == coinId }?.name.orEmpty(),
                                pricePoints = response.data.sortedBy { it.timestamp }.map { it.price },
                            )
                        )
                    }
                }
                is Result.Error -> {
                    _state.update {
                        it.copy(
                            coinPriceHistoryState = CoinPriceHistoryState(
                                isLoading = false,
                                pricePoints = emptyList(),
                                coinName = "",
                            )
                        )
                    }
                }
            }
        }
    }

    fun dismissPriceHistory() {
        _state.update { it.copy(coinPriceHistoryState = null,) }
    }
}