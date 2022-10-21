package com.glekhub.chi_hw_2010_7.net

import com.glekhub.chi_hw_2010_7.data.Animal
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface AnimalAPI {
    @GET("{count}")
    fun getAnimals(@Path("count") count: String): Call<MutableList<Animal>>
}