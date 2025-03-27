package org.example.udemykmp.coins.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import org.example.udemykmp.coins.domain.usecases.GetCoinsListUseCase
import org.example.udemykmp.core.domain.Result
import org.example.udemykmp.core.util.formatFiat
import org.example.udemykmp.core.util.formatPercentage

class CoinsListViewModel(
    private val getCoinsListUseCase: GetCoinsListUseCase,
): ViewModel() {
    private val _state = MutableStateFlow(CoinsListViewState())
    val state = _state
        .onStart {
            getAllCoins()
        }
        .stateIn(
            scope = viewModelScope,
            started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
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
                        error = null, //TODO: format the error
                    )
                }
            }
        }
    }
}