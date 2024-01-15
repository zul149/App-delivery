package com.example.sushi

import android.app.Activity
import android.content.Context
import android.net.Uri
import androidx.fragment.app.Fragment
import com.example.sushi.data.CartItem
import com.example.sushi.data.Item
import com.example.sushi.data.Order
import com.example.sushi.data.address
import com.example.sushi.data.productData
import com.example.sushi.ui.menu.MenuFragment
import com.example.sushi.ui.order.OrderFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FB {

    fun getAllProducts(activity: Activity, tips : String) {
        FirebaseFirestore.getInstance().collection("products").whereEqualTo("tips", tips).get()
            .addOnSuccessListener {
                val list: ArrayList<productData> = ArrayList()
                for (i in it.documents) {
                    val product: productData = i.toObject(productData::class.java)!! // nul поверка, если нал то не будем соверщать дальнейшие действия
                    product.product_id = i.id
                    list.add(product) //заполенение путсого списка
                }
                when (activity) { //передача списка в функцию
                    is ListActivity -> activity.successGetProducts(list)
                }
            }
    }

    fun getAllProducts(fragment : Fragment) {
        FirebaseFirestore.getInstance().collection("item").get()
            .addOnSuccessListener {
                val list: ArrayList<Item> = ArrayList()
                for (i in it.documents) {
                    val item: Item= i.toObject(Item::class.java)!! // nuul поверка, если нал то не будем соверщать дальнейшие действия
                    item.id = i.id
                    list.add(item) //заполенение путсого списка
                }
                when (fragment) { //передача списка в функцию
                    is MenuFragment -> fragment.successGetProducts(list)
                }
            }
    }

    fun getProductDetails(activity: ProductsActivity, productID: String) {
        FirebaseFirestore.getInstance().collection("products")
            .document(productID)
            .get()
            .addOnSuccessListener { document ->
                val product = document.toObject(productData::class.java)
                if (product != null) {
                    product.product_id = document.id
                    activity.productDetailsSuccess(product)
                }
            }

    }

    fun addDataSuccess(activity: Activity, item: CartItem, productID: String){
        FirebaseFirestore.getInstance().collection("cartItems")
            .document(productID)
            .set(item, SetOptions.merge())
            .addOnSuccessListener {
                when(activity) {
                    is ProductsActivity -> activity.addDataSuccess()
                }
            }
        }

    fun getCurrntUserId(): String { // ПОЛУЧЕНИЕ ID ЮЗЕРА
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserId = ""
        if (currentUser != null) {
            currentUserId = currentUser.uid
        }
        return currentUserId
    }


    fun getCartList(activity: Activity) {
        FirebaseFirestore.getInstance().collection("cartItems").whereEqualTo("id", getCurrntUserId()).get()
            .addOnSuccessListener {
                val cartList: ArrayList<CartItem> = ArrayList()
                for (i in it.documents) {
                    val cartItem = i.toObject(CartItem::class.java)
                    if (cartItem != null) {
                        cartItem.product_id = i.id
                        cartList.add(cartItem)
                    }
                    when (activity) {
                        is ShoppingBagActivity -> activity.successCartItemList(cartList)
                        is MyDetailsOrderActivity -> activity.successCartItemsList(cartList)
                    }

                }

            }
    }



    fun getAllProductList(activity: Activity) {
        FirebaseFirestore.getInstance().collection("products").get()
            .addOnSuccessListener { document ->
                val productList: ArrayList<productData> = ArrayList()
                for (i in document.documents) {
                    val product = i.toObject(productData::class.java)!!
                    product.product_id = i.id
                    productList.add(product)
                }
                when (activity) {
                    is ShoppingBagActivity -> activity.successProductList(productList)
                    is MyDetailsOrderActivity -> activity.successProductList(productList)
                }

            }
    }

    fun removeItemFromCart(context: Context, id: String) {
        FirebaseFirestore.getInstance().collection("cartItems")
            .document(id)
            .delete()
            .addOnSuccessListener {
                when (context) {
                    is ShoppingBagActivity -> {
                        context.itemRemoveSuccess()
                    }
                }

            }
    }

    fun updateMyCart(context: Context, productID: String, hashMap: HashMap<String, Any>) {
        FirebaseFirestore.getInstance().collection("cartItems")
            .document(productID)
            .update(hashMap)
            .addOnSuccessListener {

                when (context) {
                    is ShoppingBagActivity -> {
                        context.itemUpdateSuccess()
                    }
                }

            }

    }


    fun addAddress(activity: AddressActivity, addressInfo: address) {
        FirebaseFirestore.getInstance().collection("addresses")
            .document()
            .set(addressInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.addUpdateAddressSuccess()
            }
    }


    fun aploadImg(activity: Activity, link: Uri) {
        val x : StorageReference = FirebaseStorage.getInstance().reference.child("img" + System.currentTimeMillis() + ".png")
        x.putFile(link).addOnSuccessListener {//скачали ссылку
            it.metadata!!.reference!!.downloadUrl
                .addOnSuccessListener { //отправляем сcылку в активити
                    when(activity){
                        is AddProducts -> activity.uploadImageSuccess(it.toString())
                    }
                }
        }
    }

    fun getListAddress(activity: Activity) {
        FirebaseFirestore.getInstance().collection("addresses").whereEqualTo("user_id", getCurrntUserId()).get()
            .addOnSuccessListener {
                val addresslist: ArrayList<address> = ArrayList()
                for (i in it.documents) {
                    val adres = i.toObject(address::class.java)!! // nuul поверка, если нал то не будем соверщать дальнейшие действия
                    adres.id = i.id
                    addresslist.add(adres) //заполенение путсого списка
                }
                when (activity) { //передача списка в функцию
                    is MyAddressActivity -> activity.successGetAddress(addresslist)
                }
            }
    }

    fun getMyOrderList(fragment : Fragment) {
        FirebaseFirestore.getInstance().collection("order")
            .get()
            .addOnSuccessListener { document ->
                val list: ArrayList<Order> = ArrayList()
                for (i in document.documents) {
                    val order= i.toObject(Order::class.java)
                    if (order != null) {
                        order.id = i.id
                        list.add(order)
                    }
                }
                when (fragment) {
                    is OrderFragment -> fragment.successGetOrder(list)
                }


            }
    }

    fun uploadOrder(order: Order, addressActivity: AddressActivity) {
        FirebaseFirestore.getInstance().collection("order").document()
            .set(order)
            .addOnSuccessListener {
                addressActivity.uploadSuccess()

            }
    }

}