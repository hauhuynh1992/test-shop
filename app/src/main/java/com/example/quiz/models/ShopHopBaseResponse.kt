package com.example.quiz.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable



open class Card {
    @SerializedName("card_img")
    var cardImg: Int? = null
    @SerializedName("card_type")
    var cardType: String? = null

    @SerializedName("bank_name")
    var bankName: String? = null

    @SerializedName("card_number")
    var cardNumber: String? = null

    @SerializedName("valid_date")
    var validDate: String? = null

    @SerializedName("holder_name")
    var holderName: String? = null
}

open class Address : Serializable {

    @SerializedName("full_name")
    var fullName: String? = null

    @SerializedName("address")
    var address: String? = null

    @SerializedName("phone_no")
    var mobileNo: String? = null

    @SerializedName("address_type")
    var addressType: String? = null

    @SerializedName("city")
    var city: String? = null

    @SerializedName("pin_code")
    var pincode: String? = null

    @SerializedName("state")
    var state: String? = null

    @SerializedName("is_default")
    var isDefault: Boolean? = false

}

open class Size :Serializable{

    @SerializedName("size_name")
    var sizeName:String?=null

    var isSelected:Boolean?=false

    var size: String? = null

    var id: Int? = null


}
open class Reward {
    @SerializedName("reward_img")
    var rewardImg: Int? = null
    @SerializedName("reward_value")
    var rewardValue: String? = null

    @SerializedName("reward")
    var reward: Int? = null

}
open class Brand :Serializable{

    @SerializedName("brand_name")
    var brandName:String?=null

    @SerializedName("brand_color")
    var color:Int?=null

    @SerializedName("brand_id")
    var id:Int?=null

    var isSelected:Boolean?=false

}
open class Color :Serializable{

    @SerializedName("color_name")
    var colorName:Int?=null

    var isSelected:Boolean?=false

    var color: String? = null
    var id: Int? = null



}
open class SubCategory :Serializable{

    @SerializedName("subcategory_name")
    var categoryName:String?=null

    var isSelected:Boolean?=false

    @SerializedName("category")
    var id: Int? = null

}
open class Discount :Serializable{

    @SerializedName("discount")
    var discount:String?=null

    var isSelected:Boolean?=false


}
data class Attribute(
        val count: Int = 0,
        val description: String = "",
        val id: Int = 0,
        val menu_order: Int = 0,
        val name: String = "",
        val slug: String = ""
):Serializable