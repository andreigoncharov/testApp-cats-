package com.example.myapp

import com.example.myapp.model.CatsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap
import java.util.*

interface Api {
    @GET("list")
    fun getCats(
        @QueryMap parameters: HashMap<String, String>
    ):Call<ArrayList<CatsResponse>>
}