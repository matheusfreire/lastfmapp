package com.msf.lastfmapp.di

import com.msf.lastfmapp.BuildConfig
import com.msf.lastfmapp.network.LoggingInterceptor
import com.msf.lastfmapp.network.Service
import com.msf.lastfmapp.repository.LastFmRepository
import com.msf.lastfmapp.repository.impl.LastFmRepositoryImpl
import com.msf.lastfmapp.viewmodel.LastFmViewModel
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object LastFmDi {
    val module = module {
        factory<LastFmRepository> { LastFmRepositoryImpl(get()) }
        factory { LoggingInterceptor() }
        factory { provideOkHttpClient(get()) }
        factory { providerServiceApi(get()) }
        factory { provideRetrofit(get()) }
        viewModel { LastFmViewModel(get()) }
    }
}

fun provideOkHttpClient(interceptor: LoggingInterceptor): OkHttpClient {
    return OkHttpClient().newBuilder().addInterceptor(interceptor).build()
}

fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

fun providerServiceApi(retrofit: Retrofit): Service = retrofit.create(Service::class.java)