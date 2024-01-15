package com.example.sushi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sushi.adapters.AdapterMenu
import com.example.sushi.adapters.AddressListAdapter
import com.example.sushi.data.address
import com.example.sushi.data.productData
import com.example.sushi.databinding.ActivityMyAddressBinding

class MyAddressActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyAddressBinding
    private var total = 0
    private var q = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra("total")){
            total = intent.getStringExtra("total")!!.toInt()
        }
        if (intent.hasExtra("x")){
            q = intent.getStringExtra("q").toString()
        }

        binding.addAddress.setOnClickListener {
            val intent = Intent(this, AddressActivity::class.java)
            startActivity(intent)
        }

        setUpActionBar()

        getListAddress()
    }

    private fun getListAddress() {
        FB().getListAddress(this)
    }

    private fun setUpActionBar() {
        setSupportActionBar(binding.toolbarAddress)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_24)

        binding.toolbarAddress.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    fun successGetAddress(list: ArrayList<address>) {
        if(list.size > 0) {
            binding.spisok.layoutManager = LinearLayoutManager(this)
            binding.spisok.setHasFixedSize(true) // не будет искажаться, когда будет изменяться количество элементов
            val x = AddressListAdapter(this, list) // x содержить значиения адапетра
            binding.spisok.adapter = x

            x.setOnClick(object : AddressListAdapter.OnClickListener{
                override fun onClick(position: Int, address: address) {
                    super.onClick(position, address)
                    if (q != "") {
                        val intent = Intent(this@MyAddressActivity, AddressActivity::class.java)
                        intent.putExtra("q", q)
                        startActivity(intent)
                    } else {
                        val intent = Intent(this@MyAddressActivity, AddressActivity::class.java)
                        address.total = total
                        intent.putExtra("address_details", address)
                        startActivity(intent)
                    }
                }
            })
        }
    }

}