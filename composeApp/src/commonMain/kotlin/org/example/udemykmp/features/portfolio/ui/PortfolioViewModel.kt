package org.example.udemykmp.features.portfolio.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import org.example.udemykmp.core.domain.DataError
import org.example.udemykmp.core.domain.Result
import org.example.udemykmp.core.util.formatFiat
import org.example.udemykmp.core.util.toUiText
import org.example.udemykmp.features.portfolio.domain.PortfolioCoinModel
import org.example.udemykmp.features.portfolio.data.PortfolioRepository

class PortfolioViewModel(
    private val repo: PortfolioRepository,
): ViewModel() {
    private val _state = MutableStateFlow(PortfolioViewState(isLoading = true))
    val state: StateFlow<PortfolioViewState> = initializeState()

    private fun initializeState(): StateFlow<PortfolioViewState> {
        return combine(
            _state,
            repo.getPortfolioCoins(),
            repo.getTotalBalance(),
            repo.cashBalance()
        ) { currentState, coinsResult, totalBalanceResult, cashBalance ->
            handleCombination(
                currentState = currentState,
                coinsResult = coinsResult,
                totalBalanceResult = totalBalanceResult,
                cashBalance = cashBalance
            )
        }.onStart {
            repo.initializeBalance()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = PortfolioViewState(isLoading = true)
        )
    }

    private fun handleCombination(
        currentState: PortfolioViewState,
        coinsResult: Result<List<PortfolioCoinModel>, DataError.Remote>,
        totalBalanceResult: Result<Double, DataError>,
        cashBalance: Double,
    ): PortfolioViewState {
        when (coinsResult) {
            is Result.Success -> {
                val portfolioValue = when (totalBalanceResult) {
                    is Result.Success -> formatFiat(totalBalanceResult.data)
                    is Result.Error -> formatFiat(0.0)
                }

                return currentState.copy(
                    portfolioValue = portfolioValue,
                    cashBalance = formatFiat(cashBalance),
                    isBuyButtonShown = coinsResult.data.isNotEmpty(),
                    coins = coinsResult.data.map { it.toUiModel() },
                    isLoading = false
                )
            }
            is Result.Error -> {
                return currentState.copy(
                    isLoading = false,
                    error = coinsResult.error.toUiText(),
                )
            }
        }
    }
}