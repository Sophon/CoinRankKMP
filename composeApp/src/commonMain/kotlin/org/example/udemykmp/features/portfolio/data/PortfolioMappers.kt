package org.example.udemykmp.features.portfolio.data

import kotlinx.datetime.Clock
import org.example.udemykmp.core.domain.coin.Coin
import org.example.udemykmp.features.portfolio.data.local.PortfolioCoinEntity
import org.example.udemykmp.features.portfolio.integration.model.PortfolioCoinModel

fun PortfolioCoinEntity.toDomainModel(currentPrice: Double): PortfolioCoinModel {
    return PortfolioCoinModel(
        coin = Coin(
            id = coinId,
            name = name,
            symbol = symbol,
            iconUrl = iconUrl,
        ),
        performancePercent = ((currentPrice - averagePurchasePrice)/averagePurchasePrice)*100,
        averagePurchasePrice = averagePurchasePrice,
        ownedAmountInUnit = amountOwned,
        ownedAmountInFiat = amountOwned*currentPrice,
    )
}

fun PortfolioCoinModel.toEntity(): PortfolioCoinEntity {
    return PortfolioCoinEntity(
        coinId = coin.id,
        name = coin.name,
        symbol = coin.symbol,
        iconUrl = coin.iconUrl,
        averagePurchasePrice = averagePurchasePrice,
        amountOwned = ownedAmountInUnit,
        timestamp = Clock.System.now().toEpochMilliseconds(),
    )
}