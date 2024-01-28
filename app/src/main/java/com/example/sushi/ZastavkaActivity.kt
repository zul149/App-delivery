package com.example.sushi

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.WindowManager

class ZastavkaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zastavka)

        @Suppress("DEPRECATION") // чтобы не было уведомления зачеркнутости на коде
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) { //для того чтобы скрыть полосу статуса (новая версия)
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }else {
            window.setFlags( //старая версия
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        Handler(Looper.myLooper()!!).postDelayed({ //дилей
            if (FB().getCurrntUserId().isNotEmpty()){ //получение id пользователя
                startActivity(Intent(this,MenuActivity::class.java))
            }else{
                startActivity(Intent(this,MainActivity::class.java))
            }
        },2000)

    }
}