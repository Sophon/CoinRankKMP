package org.example.udemykmp.features.portfolio.data

import androidx.sqlite.SQLiteException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import org.example.udemykmp.core.domain.DataError
import org.example.udemykmp.core.domain.EmptyResult
import org.example.udemykmp.core.domain.Result
import org.example.udemykmp.core.domain.onError
import org.example.udemykmp.core.domain.onSuccess
import org.example.udemykmp.features.coins.domain.api.CoinsRemoteDataSource
import org.example.udemykmp.features.portfolio.data.local.PortfolioDao
import org.example.udemykmp.features.portfolio.data.local.UserBalanceDao
import org.example.udemykmp.features.portfolio.data.local.UserBalanceEntity
import org.example.udemykmp.features.portfolio.domain.PortfolioCoinModel
import org.example.udemykmp.features.portfolio.domain.PortfolioRepository

/**
 * TODO: refactor
 * We should be able to create two separate repositories:
 * 1. portfolio
 * 2. user balance
 *
 * And most of these functions would be usecases
 */
class PortfolioRepositoryImpl(
    private val portfolioDao: PortfolioDao,
    private val userBalanceDao: UserBalanceDao,
    private val coinsRemoteDataSource: CoinsRemoteDataSource, //TODO: refactor source into Core
): PortfolioRepository {
    override suspend fun initializeBalance() {
        if (userBalanceDao.getCashBalance() == null) {
            userBalanceDao.insertBalance(userBalanceEntity = UserBalanceEntity(cashBalance = 10_000.0))
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getPortfolioCoins(): Flow<Result<List<PortfolioCoinModel>, DataError.Remote>> {
        return portfolioDao.getAllCoins().flatMapLatest { portfolioCoinEntities ->
            flow {
                if (portfolioCoinEntities.isEmpty()) {
                    emit(Result.Success(emptyList<PortfolioCoinModel>()))
                } else {
                    coinsRemoteDataSource.getCoinsList()
                        .onError { emit(Result.Error(it)) }
                        .onSuccess { coinsListResponseDto ->
                            val coins = portfolioCoinEntities.mapNotNull { portfolioCoin ->
                                val coin = coinsListResponseDto.data.coins.find { it.uuid == portfolioCoin.coinId }
                                coin?.let { portfolioCoin.toDomainModel(it.price) }
                            }
                            emit(Result.Success(coins))
                        }
                }
            }
        }.catch {
            emit(Result.Error(DataError.Remote.UNKNOWN))
        }
    }

    override suspend fun getPortfolioCoin(coinId: String): Result<PortfolioCoinModel, DataError.Remote> {
        val portfolioCoinEntity = portfolioDao.getCoin(coinId)
        if (portfolioCoinEntity == null) {
            Result.Success(null)
        } else {
            coinsRemoteDataSource.getCoinDetailsOf(coinId)
                .onError { return Result.Error(it) }
                .onSuccess { dto ->
                    return Result.Success(portfolioCoinEntity.toDomainModel(dto.data.coin.price))
                }
        }

        return Result.Error(DataError.Remote.UNKNOWN)
    }

    override suspend fun savePortfolioCoin(coin: PortfolioCoinModel): EmptyResult<DataError.Local> {
        try {
            portfolioDao.insert(coin.toEntity())
            return Result.Success(Unit)
        } catch (e: SQLiteException) {
            return Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun removePortfolioCoin(coinId: String): EmptyResult<DataError.Local> {
        try {
            portfolioDao.deleteCoin(coinId)
            return Result.Success(Unit)
        } catch (e: SQLiteException) {
            return Result.Error(DataError.Local.UNKNOWN)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getTotalPortfolioValue(): Flow<Result<Double, DataError.Remote>> {
        return portfolioDao.getAllCoins().flatMapLatest { portfolioCoinEntities ->
            flow {
                if (portfolioCoinEntities.isEmpty()) {
                    emit(Result.Success(0.0))
                } else {
                    coinsRemoteDataSource.getCoinsList()
                        .onError { emit(Result.Error(it)) }
                        .onSuccess { coinsListResponseDto ->
                            val totalValue = portfolioCoinEntities.sumOf { portfolioCoinEntity ->
                                val coinPrice = coinsListResponseDto.data.coins.find { it.uuid == portfolioCoinEntity.coinId }?.price ?: 0.0
                                coinPrice*portfolioCoinEntity.amountOwned
                            }

                            emit(Result.Success(totalValue))
                        }
                }
            }
        }.catch { Result.Error(DataError.Remote.UNKNOWN) }
    }

    override fun getTotalBalance(): Flow<Result<Double, DataError.Remote>> {
        return combine(
            cashBalance(),
            getTotalPortfolioValue()
        ) { cashAmount, portfolioResult ->
            portfolioResult
                .onError { Result.Error(it) }
                .onSuccess { portfolioValue -> Result.Success(cashAmount + portfolioValue) }
        }
    }

    override fun cashBalance(): Flow<Double> {
        return flow { emit(userBalanceDao.getCashBalance() ?: 0.0) }
    }

    override suspend fun updateCashBalance(newBalance: Double): EmptyResult<DataError.Local> {
        try {
            userBalanceDao.updateBalance(newBalance)
            return Result.Success(Unit)
        } catch (e: SQLiteException) {
            return Result.Error(DataError.Local.DISK_FULL)
        }
    }
}