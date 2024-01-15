package com.example.sushi

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.sushi.databinding.FragmentMenuBinding

import java.io.IOException

class GlideLoader (val context: Context) : AppCompatActivity() {

    fun loadImage(image: Any, imageView: ImageView) {
        try {
            Glide // изображение загружается по ссылке в имэдж вью
                .with(context)
                .load(image)
                .centerCrop()
                .placeholder(R.drawable.baseline_account_circle_24)
                .into(imageView) //загрузка в imageView
        }catch (e : IOException) {
            e.printStackTrace() //отображение ошибки
        }
    }

    fun loadProductPicture(image : Any, imageView : ImageView){
        try {
            Glide
                .with(context)
                .load(image)
                .centerCrop()
                .into(imageView)

        }catch (e : IOException){
            e.printStackTrace()
        }
    }

    fun showImage(activity: Activity) { //диалогове окно из галлереи
        val x = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activity.startActivityForResult(x,1) //x - путь к изображению  и запускается фунция  onActivityResult
    }



}