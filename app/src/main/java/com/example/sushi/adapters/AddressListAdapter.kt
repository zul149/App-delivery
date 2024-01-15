package com.example.sushi.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sushi.R
import com.example.sushi.data.address
import com.example.sushi.databinding.ItemAddressBinding

class AddressListAdapter (private val context: Context, private val list : ArrayList<address>) : RecyclerView.Adapter<AddressListAdapter.ViewHolder>() {
    class ViewHolder(val binding : ItemAddressBinding) : RecyclerView.ViewHolder(binding.root)

    var click : OnClickListener? = null
    fun setOnClick (onClickListener: OnClickListener) {
        this.click = onClickListener
    }
    interface OnClickListener {
        fun onClick (position: Int, data: address) {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_address,parent,false)
        return ViewHolder(ItemAddressBinding.bind(view))
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]

        holder.binding.street.text = data.street
        holder.binding.home.text = data.home
        holder.binding.korpus.text = data.korpus

        holder.itemView.setOnClickListener{
            if (click != null) {
                click!!.onClick(position, data)
            }
        }
    }

//    fun notifyEditItem(activity : Activity, position: Int){
//        val intent = Intent(context, AddressActivity::class.java)
//        intent.putExtra("address_details",list[position])
//        activity.startActivity(intent)
//        notifyItemChanged(position)
//    }

    override fun getItemCount(): Int {
        return list.size
    }
}