package com.example.sushi.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sushi.GlideLoader
import com.example.sushi.R
import com.example.sushi.data.Item
import com.example.sushi.databinding.ItemlistBinding


class AdapterList (val context: Activity, val list: List<Item>): RecyclerView.Adapter<AdapterList.ViewHolder>() {

    var click : OnClickListener? = null
    fun setOnClick (onClickListener: OnClickListener) {
        this.click = onClickListener
    }
    interface OnClickListener {
        fun onClick (position: Int, tips : Item) {

        }
    }

    class ViewHolder( val binding: ItemlistBinding) : RecyclerView.ViewHolder(binding.root) {
        val title: TextView = binding.itemListTitle
        val image: ImageView = binding.itemListImage
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.itemlist, parent, false)
        return ViewHolder(ItemlistBinding.bind(view))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) { //ссылаемся на элементы из layout
        val data: Item = list[position]
        GlideLoader(context).loadImage(data.img, holder.binding.itemListImage)
        holder.binding.itemListTitle.text = data.title

        holder.itemView.setOnClickListener{
            if (click != null) {
                click!!.onClick(position, data)
            }
        }
    }


    override fun getItemCount(): Int { // подсчет количесвта элементов списка
        return list.size
    }


}