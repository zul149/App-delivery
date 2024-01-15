package com.example.sushi.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class address (
    val user_id : String = "",
    val name : String = "",
    val mobileNumber : String = "",
    val street : String = "",
    val home : String = "",
    val korpus : String = "",
    val under : String = "",
    val domofon : String = "",
    val floor : String = "",
    val flat : String = "",
    var total: Int = 0,
    var id : String = ""

): Parcelable