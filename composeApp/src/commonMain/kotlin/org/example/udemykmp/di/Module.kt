package org.example.udemykmp.di

import io.ktor.client.HttpClient
import org.example.udemykmp.coins.data.remote.impl.CoinsKtorRemoteDataSource
import org.example.udemykmp.coins.domain.api.CoinsRemoteDataSource
import org.example.udemykmp.coins.domain.usecases.GetCoinDetailsUseCase
import org.example.udemykmp.coins.domain.usecases.GetCoinPriceHistoryUseCase
import org.example.udemykmp.coins.domain.usecases.GetCoinsListUseCase
import org.example.udemykmp.coins.ui.CoinsListViewModel
import org.example.udemykmp.core.network.HttpClientFactory
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
}