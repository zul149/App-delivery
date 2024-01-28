package com.example.sushi

import android.app.AlertDialog
import android.app.TimePickerDialog
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
import java.text.SimpleDateFormat
import java.util.Calendar


class ShoppingBagActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShoppingBagBinding
    private lateinit var ProductList: ArrayList<productData>
    private lateinit var CartListItem: ArrayList<CartItem>
    private var total = 0
    private var timeDelivery: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShoppingBagBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCheckout.setOnClickListener{

            val items = CartListItem.size
            for (i in CartListItem) {
                FB().removeItemFromCart(this, i.product_id)
            }

            if (timeDelivery == "") {
                Toast.makeText(this,"выберите время доставки", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this@ShoppingBagActivity, MyAddressActivity::class.java)
                intent.putExtra("total", total.toString())
                intent.putExtra("timeDelivery", timeDelivery)
                startActivity(intent)
            }
        }

        setupActionBar()

//        getCartItemList()

        binding.btnTime.setOnClickListener{// календарь
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                binding.txtTime.text = SimpleDateFormat("HH:mm").format(cal.time)
                timeDelivery = SimpleDateFormat("HH:mm").format(cal.time).toString()
            }
            TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true)
                .show()
        }
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
            binding.btnTime.visibility = View.VISIBLE
            binding.txtTime.visibility = View.VISIBLE

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
                        else -> {
                            item.price.toInt()
                        }
                    }
                    val quantity = item.cart_quantity.toDouble()
                    subTotal += (price * quantity)

            }
            binding.total.text = "${subTotal}"
            binding.delivery.text = "${100}"

            if (subTotal >0 ){
                binding.btnCheckout.visibility = View.VISIBLE

                total = (subTotal).toInt()
                binding.totalDelivety.text = "${total + 100}"

                binding.total.text = "${total}"

//                var total = subTotal + 100
//
//                binding.totalDelivety.text = "${total}"

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









