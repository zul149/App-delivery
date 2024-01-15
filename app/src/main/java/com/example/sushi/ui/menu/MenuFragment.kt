package com.example.sushi.ui.menu

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sushi.AddProducts
import com.example.sushi.FB
import com.example.sushi.ListActivity
import com.example.sushi.MainActivity2
import com.example.sushi.MenuActivity
import com.example.sushi.ShoppingBagActivity
import com.example.sushi.adapters.AdapterList
import com.example.sushi.data.Item
import com.example.sushi.databinding.FragmentMenuBinding


class MenuFragment : Fragment() {

    private var _binding: FragmentMenuBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bag.setOnClickListener { view ->
            val intent = Intent(requireActivity(), ShoppingBagActivity::class.java)
            startActivity(intent)
        }


        getProducts()
    }

    private fun getProducts() {
        FB().getAllProducts(this)
    }

    fun successGetProducts(list: ArrayList<Item>) {
        if(list.size > 0) {
            binding.tips.layoutManager = LinearLayoutManager(requireActivity())
            binding.tips.setHasFixedSize(true) // не будет искажаться, когда будет изменяться количество элементов
            val x = AdapterList(requireActivity(), list) // x содержить значиения адапетра
            binding.tips.adapter = x

            x.setOnClick(object : AdapterList.OnClickListener{
                override fun onClick(position: Int, tips: Item) {
                    super.onClick(position, tips)
//                    val i = Intent(this@MenuFragment, MenuActivity::class.java)
//                    i.putExtra("tips", tips.tips)
//                    startActivity(i)
                    val intent = Intent(requireActivity(), ListActivity::class.java)
                    intent.putExtra("tips", tips.tips)
                    startActivity(intent)
                }
            })
        }
    }

}