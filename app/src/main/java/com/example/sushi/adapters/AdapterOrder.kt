package com.example.sushi.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.sushi.GlideLoader
import com.example.sushi.ListActivity
import com.example.sushi.R
import com.example.sushi.data.Order
import com.example.sushi.data.productData
import com.example.sushi.databinding.OrderBinding


class AdapterOrder(val context: FragmentActivity, val list: ArrayList<Order>): RecyclerView.Adapter<AdapterOrder.ViewHolder>() {

    var click : OnClickListener? = null
    fun setOnClick (onClickListener: OnClickListener) {
        this.click = onClickListener
    }
    interface OnClickListener {
        fun onClick (position: Int, product: Order) {

        }
    }

    class ViewHolder( val binding: OrderBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.order, parent, false)
        return ViewHolder(OrderBinding.bind(view))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) { //ссылаемся на элементы из layout
        val data: Order = list[position]
        holder.binding.itemListTitle.text = data.title
        holder.binding.itemListPrice.text = "${data.price}"

        holder.itemView.setOnClickListener{
            if (click != null) {
                click!!.onClick(position, data)
            }
        }
    }

    override fun getItemCount(): Int { //подчсет количесвта элементов списка
        return list.size
    }


}