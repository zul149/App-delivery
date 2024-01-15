package com.example.sushi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.example.sushi.adapters.AdapterMenu
import com.example.sushi.data.productData

import com.example.sushi.databinding.ActivityListBinding
import com.google.firebase.auth.FirebaseAuth


class ListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListBinding
    private var tips: String = ""
    private var product: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.getStringExtra( "tips").toString() != null) {
            tips = intent.getStringExtra( "tips").toString()
        }

        binding.bag.setOnClickListener {
            val intent = Intent(this@ListActivity, ShoppingBagActivity::class.java)
            startActivity(intent)
        }

        binding.add.setOnClickListener {
            val intent = Intent(this@ListActivity, AddProducts::class.java)
            startActivity(intent)
        }

        val email = FirebaseAuth.getInstance().currentUser?.email.toString()
        if (email == "admin@email.ru") { // админ
//            userid = args.uidUser
            binding.add.visibility = View.VISIBLE
        }

        setupActionBar() //toolbar

        getProducts()
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

    private fun getProducts() {
        FB().getAllProducts(this, tips)
    }

    fun successGetProducts(list: ArrayList<productData>) {
        if(list.size > 0) {
            binding.spisok.layoutManager = GridLayoutManager(this, 2) //GridLayoutManager - сетка
            binding.spisok.setHasFixedSize(true) // не будет искажаться, когда будет изменяться количество элементов
            val x = AdapterMenu(this, list) // x содержить значиения адапетра
            binding.spisok.adapter = x

            x.setOnClick(object : AdapterMenu.OnClickListener{
                override fun onClick(position: Int, product: productData) {
                    super.onClick(position, product)
                    val intent = Intent(this@ListActivity, ProductsActivity::class.java)
                    intent.putExtra("product_id",product.product_id)
                    startActivity(intent)
                }
            })
        }
    }
}