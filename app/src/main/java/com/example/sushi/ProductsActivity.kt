package com.example.sushi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.sushi.data.CartItem
import com.example.sushi.data.productData
import com.example.sushi.databinding.ActivityProductsBinding
import com.google.firebase.firestore.FirebaseFirestore


class ProductsActivity : AppCompatActivity(), View.OnClickListener  {

    private lateinit var binding: ActivityProductsBinding

    private var ProductID : String = ""
    private lateinit var ProductDetails : productData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra("product_id")) {
            ProductID = intent.getStringExtra("product_id")!!
        }

//        binding.buttonBag.setOnClickListener {
//            val intent = Intent(this@ProductsActivity, ShoppingBagActivity::class.java)
//            startActivity(intent)
//        }

        binding.buttonBag.setOnClickListener(this)

        setupActionBar() //toolbar

        getProductDetails()

    }

    private fun getProductDetails() {
        FB().getProductDetails(this,ProductID)
    }

    fun addDataSuccess() {
//        startActivity(Intent(this@ProductsActivity,ShoppingBagActivity::class.java))
        Toast.makeText(this@ProductsActivity, "Продукт был добавлен в корзину", Toast.LENGTH_SHORT).show()
    }


    fun productDetailsSuccess(product : productData) {


        ProductDetails = product

//        hideProgressDialog()

        GlideLoader(this).loadProductPicture(product.img, binding.img)
        binding.itemTitle.text = product.title
        binding.itemPrice.text = "от ${product.price}"
        binding.productInfo.text = product.info

    }



    private fun setupActionBar() { // toolbar
        setSupportActionBar(binding.toolbarList)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_24)

        }
        binding.toolbarList.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onClick(v: View?) {
        if (v != null){
            when(v.id){

                R.id.button_bag->{
                    val item = CartItem(price = ProductDetails.price, title = binding.itemTitle.text.toString(), img = ProductDetails.img, id = FB().getCurrntUserId())
                    FB().addDataSuccess(this, item, ProductDetails.product_id)
                }
            }
        }
    }


}