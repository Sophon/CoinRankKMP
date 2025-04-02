package org.example.udemykmp.features.portfolio.data

import androidx.sqlite.SQLiteException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.example.udemykmp.core.domain.DataError
import org.example.udemykmp.core.domain.EmptyResult
import org.example.udemykmp.core.domain.Result
import org.example.udemykmp.features.portfolio.data.local.UserBalanceDao
import org.example.udemykmp.features.portfolio.data.local.UserBalanceEntity

interface BalanceRepository {
    suspend fun initializeBalance()
    fun cashBalance(): Flow<Double>
    suspend fun updateCashBalance(newBalance: Double): EmptyResult<DataError.Local>
}

internal class BalanceRepositoryImpl(
    private val userBalanceDao: UserBalanceDao,
): BalanceRepository {

    override suspend fun initializeBalance() {
        if (userBalanceDao.getCashBalance() == null) {
            userBalanceDao.insertBalance(userBalanceEntity = UserBalanceEntity(cashBalance = 10_000.0))
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