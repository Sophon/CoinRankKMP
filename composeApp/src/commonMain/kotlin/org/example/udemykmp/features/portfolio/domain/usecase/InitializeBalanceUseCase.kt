package org.example.udemykmp.features.portfolio.domain.usecase

import org.example.udemykmp.features.portfolio.data.BalanceRepository

class InitializeBalanceUseCase(
    private val balanceRepository: BalanceRepository
) {
    suspend fun execute() = balanceRepository.initializeBalance()
}