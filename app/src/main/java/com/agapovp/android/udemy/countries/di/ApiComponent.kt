package com.agapovp.android.udemy.countries.di

import com.agapovp.android.udemy.countries.model.CountriesService
import com.agapovp.android.udemy.countries.viewmodel.ListViewModel
import dagger.Component

@Component(modules = [ApiModule::class])
interface ApiComponent {

    fun inject(service: CountriesService)

    fun inject(listViewModel: ListViewModel)
}
