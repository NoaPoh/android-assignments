package com.example.brook.model

import com.google.gson.annotations.SerializedName

class Flag {
    @SerializedName("svg")
    var imageUrlSvg: String? = null

    @SerializedName("png")
    var imageUrlPng: String? = null

    fun getImageUrlSvg(): String? {
        return imageUrlSvg
    }

    fun setImageUrlSvg(imageUrlSvg: String?) {
        this.imageUrlSvg = imageUrlSvg
    }

    fun getImageUrlPng(): String? {
        return imageUrlPng
    }

    fun setImageUrlPng(imageUrlPng: String?) {
        this.imageUrlPng = imageUrlPng
    }

    override fun toString(): String {
        return "Flag{" +
                "imageUrlSvg='" + imageUrlSvg + '\'' +
                ", imageUrlPng='" + imageUrlPng + '\'' +
                '}'
    }
}
