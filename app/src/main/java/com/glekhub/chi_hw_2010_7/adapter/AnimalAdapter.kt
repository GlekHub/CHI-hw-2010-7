package com.glekhub.chi_hw_2010_7.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.glekhub.chi_hw_2010_7.data.Animal
import com.glekhub.chi_hw_2010_7.databinding.FragmentMainBinding
import com.squareup.picasso.Picasso

class AnimalAdapter(private val mList: List<Animal>) :
    RecyclerView.Adapter<AnimalAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentMainBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mList[position]
        holder.name.text = item.name
        holder.type.text = item.animal_type
        holder.weight.text = item.weight_max
        Picasso.get().load(item.image_link).into(holder.img)

    }

    inner class ViewHolder(binding: FragmentMainBinding) : RecyclerView.ViewHolder(binding.root) {
        val name: TextView = binding.name
        val type: TextView = binding.type
        val weight: TextView = binding.weight
        val img: ImageView = binding.img

    }

    override fun getItemCount(): Int = mList.size
}