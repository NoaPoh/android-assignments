package com.example.brook.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CountryModel private constructor() {
    val BASE_URL: String = "https://restcountries.com/"
    var retrofit: Retrofit?
    var countryApi: CountryApi

    init {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        countryApi = retrofit!!.create<CountryApi>(CountryApi::class.java)
    }

    fun getAllCountries(): LiveData<MutableList<Country?>?> {
        val data = MutableLiveData<MutableList<Country?>?>()
        val call = countryApi.getAllCountries()
        call?.enqueue(object : Callback<MutableList<Country?>?> {
            override fun onResponse(
                call: Call<MutableList<Country?>?>?,
                response: Response<MutableList<Country?>?>?
            ) {
                if (response!!.isSuccessful()) {
                    data.setValue(response.body())
                } else {
                    Log.d("TAG", "----- response error")
                }
            }

            override fun onFailure(call: Call<MutableList<Country?>?>?, t: Throwable?) {
                Log.d("TAG", "----- fail")
            }
        })
        return data
    }

    fun getCountryByName(name: String?): LiveData<Country?> {
        val data = MutableLiveData<Country?>()
        val call = countryApi.getCountryByName(name)
        call?.enqueue(object : Callback<MutableList<Country?>?> {
            override fun onResponse(
                call: Call<MutableList<Country?>?>?,
                response: Response<MutableList<Country?>?>?
            ) {
                if (response!!.isSuccessful()) {
                    val responseCountries = response.body()

                    if (responseCountries != null && responseCountries.size > 0) {
                        data.setValue(responseCountries.get(0))
                    } else {
                        data.setValue(null)
                    }
                } else {
                    Log.d("TAG", "----- response error")
                }
            }

            override fun onFailure(call: Call<MutableList<Country?>?>?, t: Throwable?) {
                Log.d("TAG", "----- fail")
                t!!.printStackTrace()
            }
        })
        return data
    }

    companion object {
        private val instance = CountryModel()

        @JvmStatic
        fun getInstance(): CountryModel {
            return CountryModel.Companion.instance
        }
    }
}
