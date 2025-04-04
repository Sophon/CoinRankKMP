package org.example.udemykmp.features.portfolio.data

import androidx.sqlite.SQLiteException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import org.example.udemykmp.core.domain.DataError
import org.example.udemykmp.core.domain.EmptyResult
import org.example.udemykmp.core.domain.Result
import org.example.udemykmp.core.domain.onError
import org.example.udemykmp.core.domain.onSuccess
import org.example.udemykmp.features.coins.data.remote.impl.CoinsRemoteDataSource
import org.example.udemykmp.features.portfolio.data.local.PortfolioDao
import org.example.udemykmp.features.portfolio.integration.model.PortfolioCoinModel
import org.example.udemykmp.features.portfolio.integration.PortfolioRepository

class PortfolioRepositoryImpl(
    private val portfolioDao: PortfolioDao,
    private val coinsRemoteDataSource: CoinsRemoteDataSource,
): PortfolioRepository {

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
    override fun getTotalPortfolioCoinValue(): Flow<Result<Double, DataError.Remote>> {
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
}