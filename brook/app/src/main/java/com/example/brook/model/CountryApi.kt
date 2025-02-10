package com.example.brook.model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface CountryApi {
    @GET("/v2/all")
    fun getAllCountries(): Call<MutableList<Country?>?>?

    @GET("/v2/name/{name}")
    fun getCountryByName(@Path("name") name: String?): Call<MutableList<Country?>?>?
}