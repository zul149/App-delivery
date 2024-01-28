package com.example.sushi

import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sushi.adapters.AdapterShoppingBag
import com.example.sushi.data.CartItem
import com.example.sushi.data.Order
import com.example.sushi.data.address
import com.example.sushi.data.productData
import com.example.sushi.data.users
import com.example.sushi.databinding.ActivityMyDetailsOrderBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

class MyDetailsOrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyDetailsOrderBinding
    private var MyOrderDetails: Order? = null
    private lateinit var MyOrderList: ArrayList<Order>
//    private lateinit var CartListItem: ArrayList<CartItem>
//    private lateinit var ProductList: ArrayList<productData>
    private var ProductID : String = ""
    private var AddressDetails: address? = null
    private var timeDelivery = ""

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

        if (intent.hasExtra("order_details")){ // принимаем занчения которые заполняют адрес из MyAddressActivity
            MyOrderDetails = intent.getParcelableExtra("order_details")!!
        }

        if (intent.hasExtra("timeDelivery")) {
            timeDelivery = intent.getStringExtra("timeDelivery")!!
        }

        getDetails()

        if (AddressDetails != null) {
            binding.street.text = AddressDetails!!.street
            binding.home.text = AddressDetails!!.home
            binding.phone.text = AddressDetails!!.mobileNumber
            binding.name.text = AddressDetails!!.name
        }

//        val id = FirebaseAuth.getInstance().currentUser!!.uid.toString()
//        FirebaseFirestore.getInstance().collection("users")
//            .document(id) //вызов функции получения айди юзера
//            .get()
//
//            .addOnSuccessListener { document ->
//                val user = document.toObject(users::class.java)
//                if ( user != null) {
//                    binding.name.text = user.name
//                    binding.phone.text = user.phone
//                }
//            }

//        getProductList()

        setUpActionBar()


    }

    private fun getDetails() {
        FB().getOrder(this, MyOrderDetails!!.id)
    }

    fun getOrderSuccess(order: Order) { // отображение данных заказа
        if (order != null) {
            binding.name.text = order.address.name
            binding.phone.text = order.address.mobileNumber
            binding.home.text = order.address.home
            binding.street.text = order.address.street
            binding.total.text = "${order.price}".toInt().toString()
            binding.id.text = order.title
            binding.time.text = order.time_delivery
        }


        val dateFormat = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = order.time + 10800000
        val date = dateFormat.format(calendar.time)
        binding.date.text = "$date"

//        binding.total.text = order.total_delivery
    }


    private fun setUpActionBar() {
        setSupportActionBar(binding.toolbarOrder)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_24)

        binding.toolbarOrder.setNavigationOnClickListener {
            onBackPressed()
        }
    }




}