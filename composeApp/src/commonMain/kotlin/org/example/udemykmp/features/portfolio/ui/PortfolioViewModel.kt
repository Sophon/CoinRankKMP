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
import org.example.udemykmp.features.portfolio.domain.usecase.GetPortfolioStatusUseCase
import org.example.udemykmp.features.portfolio.domain.usecase.InitializeBalanceUseCase
import org.example.udemykmp.features.portfolio.integration.model.PortfolioStatus

class PortfolioViewModel(
    private val getPortfolioStatusUseCase: GetPortfolioStatusUseCase,
    private val initializeBalanceUseCase: InitializeBalanceUseCase,
): ViewModel() {
    private val _state = MutableStateFlow(PortfolioViewState(isLoading = true))
    val state: StateFlow<PortfolioViewState> = initializeState()

    private fun initializeState(): StateFlow<PortfolioViewState> {
        return combine(
            _state,
            getPortfolioStatusUseCase.execute(),
        ) { currentState, portfolioStatusResult ->
            handleCombination(
                currentState = currentState,
                portfolioStatusResult = portfolioStatusResult,
            )
        }.onStart {
            initializeBalanceUseCase.execute()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = PortfolioViewState(isLoading = true)
        )
    }

    private fun handleCombination(
        currentState: PortfolioViewState,
        portfolioStatusResult: Result<PortfolioStatus, DataError.Remote>,
    ): PortfolioViewState {
        return when (portfolioStatusResult) {
            is Result.Success -> {
                currentState.copy(
                    portfolioValue = formatFiat(portfolioStatusResult.data.totalPortfolioValue),
                    cashBalance = formatFiat(portfolioStatusResult.data.cashBalance),
                    isBuyButtonShown = portfolioStatusResult.data.coins.isNotEmpty(),
                    coins = portfolioStatusResult.data.coins.map { it.toUiModel() },
                    isLoading = false
                )
            }
            is Result.Error -> {
                currentState.copy(
                    isLoading = false,
                    error = portfolioStatusResult.error.toUiText(),
                )
            }
        }
    }
}