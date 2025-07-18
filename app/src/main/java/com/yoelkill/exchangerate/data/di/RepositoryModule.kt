package com.yoelkill.exchangerate.data.di

import com.yoelkill.exchangerate.data.repository.ExchangeRepositoryImpl
import com.yoelkill.exchangerate.domain.repository.ExchangeRepository
import dagger.Binds

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindExchangeRepository(
        exchangeRepositoryImpl: ExchangeRepositoryImpl
    ): ExchangeRepository

}