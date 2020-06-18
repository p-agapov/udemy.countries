package com.agapovp.android.udemy.countries.di

import com.agapovp.android.udemy.countries.model.CountriesApi
import com.agapovp.android.udemy.countries.model.CountriesService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
class ApiModule {

    @Provides
    fun provideCountriesApi(): CountriesApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(CountriesApi::class.java)

    @Provides
    fun provideCountriesService() = CountriesService()

    companion object {
        private const val BASE_URL = "https://raw.githubusercontent.com"
    }
}
