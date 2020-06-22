package com.agapovp.android.udemy.countries.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.agapovp.android.udemy.countries.R
import com.agapovp.android.udemy.countries.databinding.ActivityMainBinding
import com.agapovp.android.udemy.countries.viewmodel.ListViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: ListViewModel
    private lateinit var dataBinding: ActivityMainBinding

    private val countriesAdapter = CountryListAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)
        viewModel.refresh()

        dataBinding.rvCountries.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = countriesAdapter
        }

        dataBinding.layoutSwipeRefresh.setOnRefreshListener {
            dataBinding.layoutSwipeRefresh.isRefreshing = false
            viewModel.refresh()
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.countries.observe(this, Observer { countries ->
            countries?.let {
                countriesAdapter.updateCountries(it)
                dataBinding.rvCountries.visibility = View.VISIBLE
            }
        })

        viewModel.countryLoadError.observe(this, Observer { isError ->
            isError?.let { dataBinding.tvError.visibility = if (it) View.VISIBLE else View.GONE }
        })

        viewModel.loading.observe(this, Observer { isLoading ->
            isLoading?.let {
                dataBinding.pbLoading.visibility = if (it) View.VISIBLE else View.GONE
                if (it) {
                    dataBinding.tvError.visibility = View.GONE
                    dataBinding.rvCountries.visibility = View.GONE
                }
            }
        })
    }
}
