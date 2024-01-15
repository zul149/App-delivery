package com.example.sushi.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sushi.GlideLoader
import com.example.sushi.ListActivity
import com.example.sushi.R
import com.example.sushi.data.Item
import com.example.sushi.data.productData
import com.example.sushi.databinding.ProductBinding

class AdapterMenu (val context: ListActivity, val list: ArrayList<productData>): RecyclerView.Adapter<AdapterMenu.ViewHolder>() {

    var click : OnClickListener? = null
    fun setOnClick (onClickListener: OnClickListener) {
        this.click = onClickListener
    }
    interface OnClickListener {
        fun onClick (position: Int, product: productData) {

        }
    }

    class ViewHolder( val binding: ProductBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.product, parent, false)
        return ViewHolder(ProductBinding.bind(view))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) { //ссылаемся на элементы из layout
        val data: productData = list[position]
        holder.binding.itemListTitle.text = data.title
        holder.binding.itemListPrice.text = "от ${data.price}"
        GlideLoader(context).loadImage(data.img, holder.binding.itemListImage)

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