package com.example.sushi.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Order (
    val user_id : String = "",
    var product_id : String = "",
    val items : ArrayList<CartItem> = ArrayList(),
    val address: address = address(),
    val price: Int = 0,
    val order_dateTime : Long = 0L,
    val time : Long = System.currentTimeMillis(),
    val time_delivery : String = "",
    val title : String = "",
    var id : String = ""
) : Parcelable

// календарь вью // тайм пикер