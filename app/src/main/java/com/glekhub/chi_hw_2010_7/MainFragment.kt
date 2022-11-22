package com.glekhub.chi_hw_2010_7

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.glekhub.chi_hw_2010_7.adapter.AnimalAdapter
import com.glekhub.chi_hw_2010_7.data.Animal
import com.glekhub.chi_hw_2010_7.data.RetroDbManager
import com.glekhub.chi_hw_2010_7.databinding.RecyclerAnimalBinding
import com.glekhub.chi_hw_2010_7.net.AnimalAPI
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainFragment : Fragment() {

    private var _binding: RecyclerAnimalBinding? = null
    private val binding get() = _binding!!
    private var dbManager: RetroDbManager? = null

    private var animals: MutableList<Animal> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = RecyclerAnimalBinding.inflate(inflater, container, false)

        dbManager = context?.let { RetroDbManager(it) }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        coroutineTask()
    }

    private fun insertAnimals() {
        dbManager?.insertAnimals(animals)
    }

    private fun fetchAnimals(): List<Animal>? {
        return dbManager?.fetchAnimals()
    }

    private fun loadWithRetrofit(count: String) {
        val retrofit: Retrofit =
            Retrofit.Builder().baseUrl("https://zoo-animal-api.herokuapp.com/animals/rand/")
                .addConverterFactory(GsonConverterFactory.create()).build()

        val animalApi = retrofit.create(AnimalAPI::class.java)
        val result = animalApi.getAnimals(count).execute()
        if (result.isSuccessful) {
            animals += result.body()!!
        }
    }

    private fun update() {
        insertAnimals()
        Handler(Looper.getMainLooper()).post {
            binding.list.adapter = fetchAnimals()?.let { AnimalAdapter(it) }
        }
    }

    private fun coroutineTask() {
        CoroutineScope(Dispatchers.IO).launch {
            loadWithRetrofit("3")
            update()
        }
    }
}