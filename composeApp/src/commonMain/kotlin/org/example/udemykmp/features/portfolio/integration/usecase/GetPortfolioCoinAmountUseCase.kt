package org.example.udemykmp.features.portfolio.integration.usecase

interface GetPortfolioCoinAmountUseCase {
    suspend fun execute(coinId: String): Double
}