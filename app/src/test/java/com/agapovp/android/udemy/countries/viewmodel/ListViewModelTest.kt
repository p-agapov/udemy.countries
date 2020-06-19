package com.agapovp.android.udemy.countries.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.agapovp.android.udemy.countries.model.CountriesService
import com.agapovp.android.udemy.countries.model.Country
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

class ListViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Mock
    lateinit var countriesService: CountriesService

    @InjectMocks
    var listViewModel = ListViewModel()

    private var testSingle: Single<List<Country>>? = null

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Before
    fun setRxSchedulers() {
        val immediate = object : Scheduler() {
            override fun createWorker() = ExecutorScheduler.ExecutorWorker(Executor { it.run() })

            override fun scheduleDirect(run: Runnable?, delay: Long, unit: TimeUnit?) =
                super.scheduleDirect(run, 0, unit)
        }

        RxJavaPlugins.setInitIoSchedulerHandler { immediate }
        RxJavaPlugins.setInitComputationSchedulerHandler { immediate }
        RxJavaPlugins.setInitNewThreadSchedulerHandler { immediate }
        RxJavaPlugins.setInitSingleSchedulerHandler { immediate }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { immediate }
    }

    @Test
    fun getCountriesSuccess() {
        val country = Country("countryName", "capital", "url")
        val countriesList = arrayListOf(country)

        testSingle = Single.just(countriesList)

        `when`(countriesService.getCountries()).thenReturn(testSingle)

        listViewModel.refresh()

        assertEquals(1, listViewModel.countries.value?.size)
        assertEquals(false, listViewModel.countryLoadError.value)
        assertEquals(false, listViewModel.loading.value)
    }

    @Test
    fun getCountriesError() {
        testSingle = Single.error(Throwable())

        `when`(countriesService.getCountries()).thenReturn(testSingle)

        listViewModel.refresh()

        assertEquals(null, listViewModel.countries.value)
        assertEquals(true, listViewModel.countryLoadError.value)
        assertEquals(false, listViewModel.loading.value)
    }
}
