package com.glekhub.chi_hw_2010_7

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.SyncStateContract.Helpers.update
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.glekhub.chi_hw_2010_7.adapter.AnimalAdapter
import com.glekhub.chi_hw_2010_7.data.Animal
import com.glekhub.chi_hw_2010_7.databinding.RecyclerAnimalBinding
import com.glekhub.chi_hw_2010_7.net.AnimalAPI
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "TAG"

class MainFragment : Fragment() {

    private var _binding: RecyclerAnimalBinding? = null
    private val binding get() = _binding!!

    private var animals: MutableList<Animal> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RecyclerAnimalBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //task1()
        //task2()
        task3Supervisor()
        //task3Job()
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
            Log.d(TAG, "In main thread: ${Thread.currentThread()}")
        }
    }

    private fun load() {
        //try {
            loadWithOkHttp()
            loadWithRetrofit("3")
            Log.d(TAG, "In coroutine: ${Thread.currentThread()}")

//        } catch (e: Exception) {
//            Log.d(TAG, "Failure: ${e.message}")
//        }
    }

    private fun task1() {
        CoroutineScope(Dispatchers.IO).launch {
            load()
            Log.d(TAG, "TASK1: loaded")
            update(animals)
        }
    }

    private fun task2() = runBlocking {
        val job: Job = launch(Dispatchers.IO) {
            repeat(3) {
                load()
                Log.d(TAG, "TASK2: start loading in ${it + 1}st time")
                delay(500L)
                Log.d(TAG, "TASK2: launch delay end in ${it + 1}st time")
            }
        }
        delay(900L)
        Log.d(TAG, "TASK2: delay end")
        job.cancel()
        job.join()
        Log.d(TAG, "TASK2: job join")
        update(animals)
    }

    private val superJob = SupervisorJob()
    private val superScope = CoroutineScope(Dispatchers.IO + superJob)

    private fun task3Supervisor() = superScope.launch {
        try {
            coroutineScope {
                async {
                    load()
                    update(animals)
                    delay(1000L)
                    throw RuntimeException("Request Failed")
                }
            }.await()

        } catch (e: Exception) {
            MainScope().launch {
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            }
            Log.d(TAG, "TASK3: Failure $e")
        }
    }

    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    private fun task3Job() = scope.launch {
        try {
            coroutineScope {
                async {
                    load()
                    update(animals)
                }
            }.await()
            delay(1000L)
        } catch (e: Exception) {
            Log.d(TAG, "TASK3: Failure $e")
        }
    }

}