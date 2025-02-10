package com.example.brook.model

import com.google.gson.annotations.SerializedName

class Flag {
    @SerializedName("svg")
    var imageUrlSvg: String? = null

    @SerializedName("png")
    var imageUrlPng: String? = null

    override fun toString(): String {
        return "Flag{" +
                "imageUrlSvg='" + imageUrlSvg + '\'' +
                ", imageUrlPng='" + imageUrlPng + '\'' +
                '}'
    }
}
