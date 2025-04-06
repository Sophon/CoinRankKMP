package org.example.udemykmp

import androidx.room.RoomDatabase
import io.ktor.client.HttpClient
import org.example.udemykmp.features.portfolio.data.local.PortfolioDatabase
import org.example.udemykmp.features.portfolio.data.local.getPortfolioDatabase
import org.example.udemykmp.features.coins.data.remote.CoinsRemoteDataSourceImpl
import org.example.udemykmp.features.coins.domain.usecases.GetCoinPriceHistoryUseCase
import org.example.udemykmp.features.coins.domain.usecases.GetCoinsListUseCase
import org.example.udemykmp.features.coins.ui.CoinsListViewModel
import org.example.udemykmp.core.network.HttpClientFactory
import org.example.udemykmp.features.coins.data.CoinsRepositoryImpl
import org.example.udemykmp.features.coins.data.remote.CoinsRemoteDataSource
import org.example.udemykmp.features.coins.domain.usecases.GetCoinDetailsUseCaseImpl
import org.example.udemykmp.features.coins.integration.CoinsRepository
import org.example.udemykmp.features.coins.integration.usecase.GetCoinDetailsUseCase
import org.example.udemykmp.features.portfolio.data.BalanceRepositoryImpl
import org.example.udemykmp.features.portfolio.data.PortfolioRepositoryImpl
import org.example.udemykmp.features.portfolio.domain.usecase.GetPortfolioCoinAmountUseCaseImpl
import org.example.udemykmp.features.portfolio.domain.usecase.GetPortfolioStatusUseCase
import org.example.udemykmp.features.portfolio.domain.usecase.InitializeBalanceUseCase
import org.example.udemykmp.features.portfolio.integration.BalanceRepository
import org.example.udemykmp.features.portfolio.integration.PortfolioRepository
import org.example.udemykmp.features.portfolio.integration.usecase.GetPortfolioCoinAmountUseCase
import org.example.udemykmp.features.portfolio.ui.PortfolioViewModel
import org.example.udemykmp.features.trade.domain.usecase.BuyCoinUseCase
import org.example.udemykmp.features.trade.domain.usecase.GetUserBalanceUseCase
import org.example.udemykmp.features.trade.domain.usecase.SellCoinUseCase
import org.example.udemykmp.features.trade.ui.TradeViewModel
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
    singleOf(::GetCoinsListUseCase)
    singleOf(::GetCoinPriceHistoryUseCase)
    singleOf(::CoinsRemoteDataSourceImpl).bind<CoinsRemoteDataSource>()
    singleOf(::CoinsRepositoryImpl).bind<CoinsRepository>()
    singleOf(::GetCoinDetailsUseCaseImpl).bind<GetCoinDetailsUseCase>()
    //endregion

    //region Portfolio
    single { getPortfolioDatabase(get<RoomDatabase.Builder<PortfolioDatabase>>()) }
    single { get<PortfolioDatabase>().portfolioDao() }
    single { get<PortfolioDatabase>().userBalanceDao() }
    singleOf(::PortfolioRepositoryImpl).bind<PortfolioRepository>()
    singleOf(::BalanceRepositoryImpl).bind<BalanceRepository>()
    viewModel { PortfolioViewModel(get(), get()) }
    singleOf(::GetPortfolioStatusUseCase)
    singleOf(::InitializeBalanceUseCase)
    singleOf(::GetPortfolioCoinAmountUseCaseImpl).bind<GetPortfolioCoinAmountUseCase>()
    //endregion

    //region Trade
    singleOf(::BuyCoinUseCase)
    singleOf(::SellCoinUseCase)
    singleOf(::GetUserBalanceUseCase)
    viewModel { (coinId: String) -> TradeViewModel(coinId, get(), get(), get(), get(), get()) }
    //endregion
}