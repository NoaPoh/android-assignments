package com.example.brook.model

import com.google.gson.annotations.SerializedName
import java.util.Locale

class Country {
    var name: String? = null

    @SerializedName("alpha2Code")
    var code: String? = null

    @SerializedName("flags")
    var flag: Flag? = null

    fun getName(): String? {
        return name
    }

    fun getHebrewName(): String {
        return this.getForeignName("he")
    }

    fun getForeignName(foreignLanguage: String?): String {
        val country = Locale.Builder().setRegion(this.getCode()).build()
        val hebrewLanguage = Locale.Builder().setLanguage(foreignLanguage).build()

        return country.getDisplayCountry(hebrewLanguage)
    }

    fun getCode(): String? {
        return code
    }

    fun setName(name: String?) {
        this.name = name
    }

    fun setCode(code: String?) {
        this.code = code
    }

    fun getFlag(): Flag? {
        return flag
    }

    fun setFlag(flag: Flag?) {
        this.flag = flag
    }

    override fun toString(): String {
        return "Country{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", flag=" + flag +
                '}'
    }
}
