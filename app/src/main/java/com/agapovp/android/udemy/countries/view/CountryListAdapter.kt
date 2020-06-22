package com.agapovp.android.udemy.countries.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.agapovp.android.udemy.countries.databinding.ItemCountryBinding
import com.agapovp.android.udemy.countries.model.Country
import com.agapovp.android.udemy.countries.util.getProgressDrawable
import com.agapovp.android.udemy.countries.util.loadImage

class CountryListAdapter(var countries: ArrayList<Country>) :
    RecyclerView.Adapter<CountryListAdapter.CountryViewHolder>() {

    fun updateCountries(newCountries: List<Country>) {
        countries.clear()
        countries.addAll(newCountries)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CountryViewHolder(
            ItemCountryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ).root
        )

    override fun getItemCount() = countries.size

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.binding.country = countries[position]
    }

    class CountryViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        internal lateinit var binding: ItemCountryBinding

        init {
            DataBindingUtil.bind<ItemCountryBinding>(view)?.let {
                binding = it
            }
        }
    }

    companion object {

        @JvmStatic
        @BindingAdapter("bind:imageUrl")
        fun loadImage(imageView: ImageView, imageUrl: String) {
            imageView.loadImage(imageUrl, getProgressDrawable(imageView.context))
        }
    }
}
