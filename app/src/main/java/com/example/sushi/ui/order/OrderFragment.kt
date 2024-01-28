package com.example.sushi.ui.order

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sushi.FB
import com.example.sushi.MyDetailsOrderActivity
import com.example.sushi.ProductsActivity
import com.example.sushi.ShoppingBagActivity
import com.example.sushi.adapters.AdapterOrder
import com.example.sushi.data.Order
import com.example.sushi.data.productData
import com.example.sushi.databinding.FragmentOrderBinding

class OrderFragment : Fragment() {

    private var _binding: FragmentOrderBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        getMyOrdersList()
    }

    private fun getMyOrdersList(){
        FB().getMyOrderList(this)
    }

    fun successGetOrder(list: ArrayList<Order>) {
        if(list.size > 0) {
            binding.spisok.layoutManager = LinearLayoutManager(requireActivity())
            binding.spisok.setHasFixedSize(true)
            val x = AdapterOrder(requireActivity(), list)
            binding.spisok.adapter = x

            x.setOnClick(object : AdapterOrder.OnClickListener{
                override fun onClick(position: Int, product: Order) {
                    super.onClick(position, product)
                    val intent = Intent(requireActivity(), MyDetailsOrderActivity::class.java)
                    intent.putExtra("product_id",product.title)
                    intent.putExtra("order_details", product)
                    startActivity(intent)
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}