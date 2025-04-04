package org.example.udemykmp.features.portfolio.domain.usecase

import org.example.udemykmp.features.portfolio.integration.BalanceRepository

class InitializeBalanceUseCase(
    private val balanceRepository: BalanceRepository
) {
    suspend fun execute() = balanceRepository.initializeBalance()
}