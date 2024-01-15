package com.example.sushi.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Order (
    val user_id : String = "",
    var product_id : String = "",
    val items : ArrayList<CartItem> = ArrayList(),
    val address: address = address(),
    val title : String = "",
    val price: Int = 0,
    val img : String = "",
    val sub_total : String = "",
    val order_dateTime : Long = 0L,
    var id : String = ""
) : Parcelable

// календарь вью // тайм пикер