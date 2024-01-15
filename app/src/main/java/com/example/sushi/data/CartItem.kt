package com.example.sushi.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CartItem(
    var product_id : String = "",
    val title : String = "",
    val price : String = "",
    val img : String = "",
    var cart_quantity : Int = 1,
    var stock_quantity : Int = 1,
    var id : String = ""

    ):Parcelable
