package com.yoelkill.exchangerate.data.di

import android.content.Context
import androidx.room.Room
import com.yoelkill.exchangerate.data.local.dao.ConversionHistoryDao
import com.yoelkill.exchangerate.data.local.database.AppDatabase
import com.yoelkill.exchangerate.data.remote.api.ExchangeApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    // --- Network Dependencies (Retrofit, OkHttpClient, ApiService) ---
    private const val BASE_URL = "https://mock-8061279c63a34f7c9b7a4869f5f5c69b.mock.insomnia.run/"
    private const val CONNECT_TIMEOUT_SECONDS = 30L
    private const val READ_TIMEOUT_SECONDS = 30L

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        // Configura el nivel de log. Para depuración, usa BODY. En producción, considera NONE o BASIC.
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return loggingInterceptor
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor) // Agrega el interceptor de logging
            // .addInterceptor { chain ->
            //     val request = chain.request().newBuilder()
            //         .addHeader("X-Api-Key", "YOUR_API_KEY")
            //         .build()
            //     chain.proceed(request)
            // }
            .connectTimeout(CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()) // O MoshiConverterFactory, etc.
            .build()
    }

    @Provides
    @Singleton
    fun provideExchangeRateApiService(retrofit: Retrofit): ExchangeApiService {
        return retrofit.create(ExchangeApiService::class.java)
    }

    // --- Database Dependencies (Room, DAO) ---

    private const val DATABASE_NAME = "exchange_rate_calculator_db"

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            DATABASE_NAME
        )
            // .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideConversionHistoryDao(appDatabase: AppDatabase): ConversionHistoryDao {
        return appDatabase.conversionHistoryDao()
    }
}