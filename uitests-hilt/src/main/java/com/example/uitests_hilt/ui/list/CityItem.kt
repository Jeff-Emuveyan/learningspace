package com.example.uitests_hilt.ui.list

import androidx.recyclerview.widget.RecyclerView
import com.example.uitests_hilt.databinding.RecyclerItemCityBinding
import com.example.uitests_hilt.model.entity.CityEntity

class CityItem(val binding: RecyclerItemCityBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(city: CityEntity, onItemClicked: (CityEntity)-> Unit) {
        binding.tvName.text = city.name
        binding.tvLocalName.text = city.localName
        binding.tvLabel.text = city.name?.take(2)
        binding.parentLayout.setOnClickListener {
            onItemClicked(city)
        }
    }
}