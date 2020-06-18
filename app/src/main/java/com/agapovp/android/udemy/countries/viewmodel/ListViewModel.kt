package com.agapovp.android.udemy.countries.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.agapovp.android.udemy.countries.di.DaggerApiComponent
import com.agapovp.android.udemy.countries.model.CountriesService
import com.agapovp.android.udemy.countries.model.Country
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ListViewModel : ViewModel() {

    val countries = MutableLiveData<List<Country>>()
    val countryLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    @Inject
    lateinit var countriesService: CountriesService

    private val disposable = CompositeDisposable()

    init {
        DaggerApiComponent.create().inject(this)
    }

    fun refresh() {
        fetchCountries()
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    private fun fetchCountries() {
        loading.value = true

        disposable.add(
            countriesService.getCountries()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        countries.value = it
                        countryLoadError.value = false
                        loading.value = false
                    },
                    {
                        countryLoadError.value = true
                        loading.value = false
                    }
                )
        )
    }
}
