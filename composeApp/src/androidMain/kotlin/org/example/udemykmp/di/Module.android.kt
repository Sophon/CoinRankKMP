package org.example.udemykmp.di

import androidx.room.RoomDatabase
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.android.Android
import org.example.udemykmp.features.portfolio.data.local.PortfolioDatabase
import org.example.udemykmp.features.portfolio.data.local.getPortfolioDatabaseBuilder
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformModule = module {

    //core
    single<HttpClientEngine> { Android.create() }
    singleOf(::getPortfolioDatabaseBuilder).bind<RoomDatabase.Builder<PortfolioDatabase>>()
}