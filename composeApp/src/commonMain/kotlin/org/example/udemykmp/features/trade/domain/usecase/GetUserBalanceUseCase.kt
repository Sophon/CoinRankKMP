package org.example.udemykmp.features.trade.domain.usecase

import kotlinx.coroutines.flow.first
import org.example.udemykmp.features.portfolio.integration.BalanceRepository

class GetUserBalanceUseCase(
    private val balanceRepository: BalanceRepository,
) {
    suspend fun execute(): Double = balanceRepository.cashBalance().first()
}