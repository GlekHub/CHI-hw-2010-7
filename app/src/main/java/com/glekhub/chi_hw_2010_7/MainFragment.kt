package com.glekhub.chi_hw_2010_7

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.glekhub.chi_hw_2010_7.adapter.AnimalAdapter
import com.glekhub.chi_hw_2010_7.data.Animal
import com.glekhub.chi_hw_2010_7.databinding.RecyclerAnimalBinding
import com.glekhub.chi_hw_2010_7.net.AnimalAPI
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainFragment : Fragment() {

    private var _binding: RecyclerAnimalBinding? = null
    private val binding get() = _binding!!

    private var animals: MutableList<Animal> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RecyclerAnimalBinding.inflate(inflater, container, false)

        binding.list.layoutManager = LinearLayoutManager(context)
        binding.list.adapter = AnimalAdapter(listOf())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Thread {
            try {
                loadWithOkHttp()
                loadWithRetrofit("3")
                Log.d("FRAG", "$animals")

                activity?.runOnUiThread {
                    update(animals)
                }
            } catch (e: Exception) {
                Log.d("FRAG", "Failure: ${e.message}")
            }

        }.start()

    }

    private fun loadWithOkHttp() {
        val clientOkHttp = OkHttpClient()
        val request = Request.Builder()
            .url("https://zoo-animal-api.herokuapp.com/animals/rand/10")
            .build()

        clientOkHttp.newCall(request).execute().use { response ->
            val animalListType = object : TypeToken<ArrayList<Animal>>() {}.type
            animals = Gson().fromJson(response.body!!.string(), animalListType)
        }
        Log.d("OkHttp", "$animals")
    }

    private fun loadWithRetrofit(count: String) {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://zoo-animal-api.herokuapp.com/animals/rand/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val animalApi = retrofit.create(AnimalAPI::class.java)
        val result = animalApi.getAnimals(count).execute()
        if (result.isSuccessful) {
            animals += result.body()!!
        }
        Log.d("Retrofit", "$animals")
    }

    private fun update(animals: MutableList<Animal>) {
        Handler(Looper.getMainLooper()).post {
            binding.list.adapter = AnimalAdapter(animals)
        }
    }

}