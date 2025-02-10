package com.example.brook.model

import com.google.gson.annotations.SerializedName
import java.util.Locale

class Country {
    var name: String? = null

    @SerializedName("alpha2Code")
    var code: String? = null

    @SerializedName("flags")
    var flag: Flag? = null

    fun getHebrewName(): String {
        return this.getForeignName("he")
    }

    fun getForeignName(foreignLanguage: String?): String {
        val country = Locale.Builder().setRegion(this.code).build()
        val hebrewLanguage = Locale.Builder().setLanguage(foreignLanguage).build()

        return country.getDisplayCountry(hebrewLanguage)
    }

    override fun toString(): String {
        return "Country{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", flag=" + flag +
                '}'
    }
}
