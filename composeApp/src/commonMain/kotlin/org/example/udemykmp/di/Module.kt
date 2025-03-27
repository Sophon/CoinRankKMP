package org.example.udemykmp.di

import io.ktor.client.HttpClient
import org.example.udemykmp.core.network.HttpClientFactory
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
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

    //core
    single<HttpClient> { HttpClientFactory.create(get()) }
}