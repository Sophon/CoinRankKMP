package org.example.udemykmp.features.trade.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.udemykmp.core.domain.onError
import org.example.udemykmp.core.domain.onSuccess
import org.example.udemykmp.core.util.formatFiat
import org.example.udemykmp.core.util.toUiText
import org.example.udemykmp.features.coins.integration.usecase.GetCoinDetailsUseCase
import org.example.udemykmp.features.portfolio.integration.usecase.GetPortfolioCoinAmountUseCase
import org.example.udemykmp.features.trade.domain.usecase.BuyCoinUseCase
import org.example.udemykmp.features.trade.domain.usecase.GetUserBalanceUseCase
import org.example.udemykmp.features.trade.domain.usecase.SellCoinUseCase

class TradeViewModel(
    private val buyCoinUseCase: BuyCoinUseCase,
    private val sellCoinUseCase: SellCoinUseCase,
    private val getCoinDetailsUseCase: GetCoinDetailsUseCase,
    private val getUserBalanceUseCase: GetUserBalanceUseCase,
    private val getPortfolioCoinAmountUseCase: GetPortfolioCoinAmountUseCase,
): ViewModel() {

    private val mockCoinId = "1" //TODO: refactor once we implement nav args
    private val _typedAmount = MutableStateFlow("")
    private val _state = MutableStateFlow(TradeViewState())
    val state = initializeState()

    fun onAmountChanged(amount: String) {
        _typedAmount.update { amount }
    }

    fun onBuyClick() {
        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            buyCoinUseCase.execute(
                coinId = mockCoinId,
                amountToBuyInFiat = _typedAmount.value.toDouble()
            ).onSuccess {
                //TODO: navigate
            }.onError { error ->
                _state.update { it.copy(error = error.toUiText()) }
            }

            _state.update { it.copy(isLoading = false) }
        }
    }

    fun onSellClick() {
        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            sellCoinUseCase.execute(
                coinId = mockCoinId,
                amountToSellInFiat = _typedAmount.value.toDouble()
            ).onSuccess {
                //TODO: navigate
            }.onError { error ->
                _state.update { it.copy(error = error.toUiText()) }
            }
        }

        _state.update { it.copy(isLoading = false) }
    }

    private fun initializeState(): StateFlow<TradeViewState> {
        return combine(
            _state,
            _typedAmount
        ) { state, typedAmount ->
            state.copy(tradingAmount = typedAmount)
        }.onStart {
            loadPortfolio()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = TradeViewState(isLoading = true)
        )
    }

    private suspend fun loadPortfolio() {
        val balance = getUserBalanceUseCase.execute()
        val portfolioCoinAmount = getPortfolioCoinAmountUseCase.execute(mockCoinId)

        getCoinDetailsUseCase.execute(mockCoinId)
            .onSuccess { coinModel ->
                _state.update {
                    it.copy(
                        coin = coinModel.toUiModel(),
                        balance = "Available: ${formatFiat(balance)}", //TODO: string
                        portfolioValue = "Available: ${formatFiat(portfolioCoinAmount)}", //TODO: string
                        isLoading = false,
                    )
                }
            }
            .onError { error ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = error.toUiText(),
                    )
                }
            }
    }
}