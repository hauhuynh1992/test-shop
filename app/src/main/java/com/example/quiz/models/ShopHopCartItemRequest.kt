package com.example.quiz.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ShopHopCartItemRequest {
    @SerializedName("colors")
    @Expose
    var colors: ArrayList<String>? = null

    @SerializedName("image")
    @Expose
    var image: String? = null

    @SerializedName("size")
    @Expose
    var size: ArrayList<String>? = null
}