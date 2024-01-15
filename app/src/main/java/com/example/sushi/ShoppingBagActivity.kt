package com.example.sushi

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sushi.adapters.AdapterShoppingBag
import com.example.sushi.data.CartItem
import com.example.sushi.data.productData
import com.example.sushi.databinding.ActivityShoppingBagBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class ShoppingBagActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShoppingBagBinding
    private lateinit var ProductList: ArrayList<productData>
    private lateinit var CartListItem: ArrayList<CartItem>
    private var total = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShoppingBagBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCheckout.setOnClickListener{
            val intent = Intent(this@ShoppingBagActivity,MyAddressActivity::class.java)
                intent.putExtra("total", total.toString())
            startActivity(intent)
        }

        setupActionBar()

//        getCartItemList()
    }


    private fun setupActionBar() { // toolbar
        setSupportActionBar(binding.toolbarShoppingBag)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_24)

        }
        binding.toolbarShoppingBag.setNavigationOnClickListener { onBackPressed() }
    }


    override fun onResume() {
        super.onResume()
        getProductList()
    }

    private fun getCartItemList() {
        FB().getCartList(this)
    }



    fun successProductList(productList: ArrayList<productData>) {
        ProductList = productList
        getCartItemList()
    }

    private fun getProductList() {
        FB().getAllProductList(this)
    }

    fun successCartItemList(cartList : ArrayList<CartItem>){

        for(product in ProductList){
            for (cartItem in cartList){
                //проверка наличия продуктов в корзине
                if (product.product_id == cartItem.product_id){

                }
            }
        }

        CartListItem = cartList

        if (CartListItem.size >0){
            binding.shoppingBagItemsList.visibility = View.VISIBLE
            binding.checkout.visibility = View.VISIBLE

            binding.shoppingBagItemsList.layoutManager = LinearLayoutManager(this)
            binding.shoppingBagItemsList.setHasFixedSize(true)
            val adapter = AdapterShoppingBag(this, CartListItem, true)
            binding.shoppingBagItemsList.adapter = adapter

            var subTotal  = 0.0
            var price = 0
            for(item in CartListItem){
                    price = when {
                        item.price.contains(",") -> {
                            val index = item.price.indexOf(",")
                            val s1 = item.price.substring(0,index)
                            val s2 = item.price.substring(index+1,item.price.length)
                            (s1 + s2).toInt()
                        }
//                        item.price.contains(".") -> {
//                            val index = item.price.indexOf(".")
//                            val s1 = item.price.substring(0,index)
//                            val s2 = item.price.substring(index+1,item.price.length)
//
//                            (s1 + s2).toInt()
//                        }
                        else -> {
                            item.price.toInt()
                        }
                    }
                    val quantity = item.cart_quantity.toDouble()
                    subTotal += (price * quantity)

            }
            binding.total.text = "${subTotal}"

            if (subTotal >0 ){
                binding.btnCheckout.visibility = View.VISIBLE

                total = subTotal.toInt()

                binding.total.text = "${total}"
            }else{
                binding.checkout.visibility = View.GONE
            }
        }else{
            binding.checkout.visibility = View.GONE
            binding.shoppingBagItemsList.visibility = View.GONE
        }
    }

    fun itemRemoveSuccess(){
        getCartItemList()
    }

    fun itemUpdateSuccess(){
        getCartItemList()
    }


}









