package com.example.sushi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sushi.adapters.AdapterShoppingBag
import com.example.sushi.data.CartItem
import com.example.sushi.data.Order
import com.example.sushi.data.productData
import com.example.sushi.databinding.ActivityMyDetailsOrderBinding

class MyDetailsOrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyDetailsOrderBinding
    private var MyOrderDetails: Order? = null
    private lateinit var CartListItem: ArrayList<CartItem>
    private lateinit var ProductList: ArrayList<productData>
    private var ProductID : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyDetailsOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra("details_order")) {
            MyOrderDetails = intent.getParcelableExtra("details_order")
        }
        if (intent.hasExtra("product_id")) {
            ProductID = intent.getStringExtra("product_id")!!
        }

        getProductList()

        setUpActionBar()
    }

    private fun getProductList() {
        FB().getAllProductList(this@MyDetailsOrderActivity)
    }

    private fun setUpActionBar() {
        setSupportActionBar(binding.toolbarOrder)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_24)

        binding.toolbarOrder.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    fun successCartItemsList(cartList: ArrayList<CartItem>) {

        binding.id.text = ProductID


        for (product in ProductList) {
            for (cartItem in cartList) {
                //проверка наличия продуктов в корзине
                if (product.product_id == cartItem.product_id) {
                    cartItem.stock_quantity == product.stock_quantity
                }
                if (product.stock_quantity == 0) {
                    cartItem.cart_quantity = 0
                }
            }
        }

        CartListItem = cartList

        binding.list.layoutManager = LinearLayoutManager(this)
        binding.list.setHasFixedSize(true)
        val adapter = AdapterShoppingBag(this, CartListItem, true)
        binding.list.adapter = adapter
        var SubTotal = 0.0

        for(item in CartListItem){
            val availableQuantity = item.stock_quantity
            if (availableQuantity > 0){
                val quantity = item.cart_quantity.toInt()
                val price = item.price.toInt()

                SubTotal += (price * quantity)
            }
        }
        if (SubTotal > 0){
            binding.checkout.visibility = View.VISIBLE

            binding.total.text = "${SubTotal}"
        }else{
            binding.checkout.visibility = View.GONE
        }

    }

    fun successProductList(productList: ArrayList<productData>) {
        ProductList = productList
        getCartItemList()
    }


    private fun getCartItemList() {
        FB().getCartList(this@MyDetailsOrderActivity)
    }


}