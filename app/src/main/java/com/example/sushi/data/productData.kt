package com.example.sushi.data

data class  productData(
    val img: String = "",
    val title: String = "",
    val price: String = "",
    val info: String = "",
    var stock_quantity : Int = 1,
    var product_id: String = "",
    var tips: String = ""
)
