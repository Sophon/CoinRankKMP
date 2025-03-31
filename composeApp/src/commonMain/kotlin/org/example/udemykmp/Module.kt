package org.example.udemykmp

import androidx.room.RoomDatabase
import io.ktor.client.HttpClient
import org.example.udemykmp.features.portfolio.data.local.PortfolioDatabase
import org.example.udemykmp.features.portfolio.data.local.getPortfolioDatabase
import org.example.udemykmp.features.coins.data.remote.impl.CoinsKtorRemoteDataSource
import org.example.udemykmp.features.coins.domain.api.CoinsRemoteDataSource
import org.example.udemykmp.features.coins.domain.usecases.GetCoinDetailsUseCase
import org.example.udemykmp.features.coins.domain.usecases.GetCoinPriceHistoryUseCase
import org.example.udemykmp.features.coins.domain.usecases.GetCoinsListUseCase
import org.example.udemykmp.features.coins.ui.CoinsListViewModel
import org.example.udemykmp.core.network.HttpClientFactory
import org.example.udemykmp.features.portfolio.data.PortfolioRepositoryImpl
import org.example.udemykmp.features.portfolio.domain.PortfolioRepository
import org.example.udemykmp.features.portfolio.ui.PortfolioViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.bind
import org.koin.dsl.module

fun initKoin(config: KoinAppDeclaration? = null) =
    startKoin {
        config?.invoke(this)

        //add Koin modules here
        modules(
            sharedModule,
            platformModule,
        )
    }

expect val platformModule: Module

//shared dependencies
val sharedModule = module {

    //region Core
    single<HttpClient> { HttpClientFactory.create(get()) }
    //endregion

    //region Coins
    viewModel { CoinsListViewModel(get(), get()) }
    singleOf(::CoinsKtorRemoteDataSource).bind<CoinsRemoteDataSource>()
    singleOf(::GetCoinsListUseCase)
    singleOf(::GetCoinDetailsUseCase)
    singleOf(::GetCoinPriceHistoryUseCase)
    //endregion

    //region portfolio
    single { getPortfolioDatabase(get<RoomDatabase.Builder<PortfolioDatabase>>()) }
    singleOf(::PortfolioRepositoryImpl).bind<PortfolioRepository>()
    viewModel { PortfolioViewModel(get()) }
    //endregion
}