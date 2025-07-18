package com.yoelkill.exchangerate.data.di

import com.yoelkill.exchangerate.data.local.mapper.ConversionHistoryMapper
import com.yoelkill.exchangerate.data.local.mapper.ConversionHistoryMapperImpl
import com.yoelkill.exchangerate.data.local.mapper.ExchangeRateMapper
import com.yoelkill.exchangerate.data.local.mapper.ExchangeRateMapperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class MapperModule {

    @Binds
    abstract fun bindExchangeRateMapper(
        exchangeRateMapperImpl: ExchangeRateMapperImpl
    ): ExchangeRateMapper

    @Binds
    abstract fun bindConversionHistoryMapper(
        conversionHistoryMapperImpl: ConversionHistoryMapperImpl
    ): ConversionHistoryMapper

}