package com.example.sushi

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.example.sushi.data.productData
import com.example.sushi.databinding.ActivityAddProductsBinding
import com.example.sushi.databinding.ActivityMainBinding
import com.example.sushi.databinding.ActivityMenuBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.io.IOException

class AddProducts : AppCompatActivity() {

    private lateinit var binding: ActivityAddProductsBinding

    var selectImage: Uri? = null
    var imageString: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_products)

        binding = ActivityAddProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonAdd.setOnClickListener {
            FB().aploadImg(this, selectImage!!)
        }

        binding.backBtn.setOnClickListener{
            startActivity(Intent(this, MenuActivity::class.java))
        }

        binding.addImg.setOnClickListener {
            GlideLoader(this).showImage(this)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) { // для предоставления разрешения для фото
        super.onActivityResult(requestCode, resultCode, data)
        if (data!!.data != null) { //data.data - ссылка на изображение  в памяти телефона
            try {
                selectImage = data.data
                GlideLoader(this).loadImage(selectImage!!, binding.addImg)
            } catch (it: IOException) { //ошибка
                val toast = Toast.makeText(
                    this@AddProducts,
                    it.message.toString(),
                    Toast.LENGTH_LONG
                )
            }
        }
    }

    fun uploadImageSuccess(toString: String) {// ссылка на изображение в базе данных для добавления в документ
        imageString = toString
        val product = productData(price = binding.priceText.text.toString(), tips = binding.tipsText.text.toString(), title = binding.titleText.text.toString(), img = imageString)
        FirebaseFirestore.getInstance().collection("products")
            .document()
            .set(product, SetOptions.merge()) //SetOptions.merge() для добавления новых аписей, без удалени существующих
            .addOnSuccessListener {
                startActivity(Intent(this, MenuActivity::class.java))
            }
    }
}