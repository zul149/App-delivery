package com.example.sushi.adapters

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sushi.FB
import com.example.sushi.GlideLoader
import com.example.sushi.MainActivity2
import com.example.sushi.MyDetailsOrderActivity
import com.example.sushi.R
import com.example.sushi.ShoppingBagActivity
import com.example.sushi.data.CartItem
import com.example.sushi.databinding.ShoppingBagBinding
import com.google.firebase.auth.FirebaseAuth


class AdapterShoppingBag(val context: Activity, val list: ArrayList<CartItem>, private val updateCartItems : Boolean): RecyclerView.Adapter<AdapterShoppingBag.ViewHolder>() {
    class ViewHolder( val binding: ShoppingBagBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.shopping_bag, parent, false)
        return ViewHolder(ShoppingBagBinding.bind(view))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data: CartItem = list[position]

        when(context){
            is MyDetailsOrderActivity -> {
                holder.binding.deleteCartItem.visibility = View.GONE
                holder.binding.ibAddCartItem.visibility = View.GONE
                holder.binding.ibRemoveCartItem.visibility = View.GONE
            }
            is ShoppingBagActivity -> {
                holder.binding.deleteCartItem.visibility = View.VISIBLE
                holder.binding.ibAddCartItem.visibility = View.VISIBLE
                holder.binding.ibRemoveCartItem.visibility = View.VISIBLE
            }
        }

        GlideLoader(context).loadImage(data.img, holder.binding.cartItemImage)
        holder.binding.cartItemTitle.text = data.title
        holder.binding.cartItemPrice.text = "от ${data.price}"
        holder.binding.cartQuantity.text = data.cart_quantity.toString()

        if (data.cart_quantity == 1) {
//            holder.binding.ibAddCartItem.visibility = View.GONE
//            holder.binding.ibRemoveCartItem.visibility = View.GONE

            if (updateCartItems){
                holder.binding.deleteCartItem.visibility = View.VISIBLE
            }else{
                holder.binding.deleteCartItem.visibility = View.GONE
            }


        } else {

//            if (updateCartItems){
//                holder.binding.deleteCartItem.visibility = View.VISIBLE
//                holder.binding.ibAddCartItem.visibility = View.VISIBLE
//                holder.binding.ibRemoveCartItem.visibility = View.VISIBLE
//            }else{
//                holder.binding.deleteCartItem.visibility = View.GONE
//                holder.binding.ibAddCartItem.visibility = View.GONE
//                holder.binding.ibRemoveCartItem.visibility = View.GONE
//            }

        }

        holder.binding.deleteCartItem.setOnClickListener {
            if (context is ShoppingBagActivity) {
                val delete = AlertDialog.Builder(context, R.style.MyDialogTheme) // всплывающие окно
                delete.setMessage("Вы уверены, что хотите удалить товар из карзины?")
                delete.setIcon(android.R.drawable.alert_light_frame)
                delete.setPositiveButton("да") { dialogInterface, _ ->
                        dialogInterface.dismiss()
                    FB().removeItemFromCart(context, data.product_id) //удаление элемента из корзины через функцию в FB
                    }
                delete.setNegativeButton("нет") { dialogInterface, _ ->
                        dialogInterface.dismiss()
                    }
                    val dialog = delete.create()
                    dialog.setCancelable(false) //одно из двух
                    dialog.show()
            }
        }

        holder.binding.ibRemoveCartItem.setOnClickListener {
            if (data.cart_quantity == 1) {
                FB().removeItemFromCart(context, data.product_id)
            } else {
                val cartQuantity: Int = data.cart_quantity.toInt()
                val hashMap = HashMap<String, Any>()
                hashMap["cart_quantity"] = (cartQuantity - 1)

                FB().updateMyCart(context, data.product_id, hashMap)
            }

        }
        holder.binding.ibAddCartItem.setOnClickListener {
            val cartQuantity: Int = data.cart_quantity.toInt()
//            if (data.cart_quantity == 1) {
                val hashMap = HashMap<String, Any>()
                hashMap["cart_quantity"] = (cartQuantity + 1)

                FB().updateMyCart(context, data.product_id, hashMap)
//            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}