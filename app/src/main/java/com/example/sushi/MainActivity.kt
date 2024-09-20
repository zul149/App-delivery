package com.example.sushi

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.sushi.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            val email: String? = binding.userEmail?.text?.toString()?.trim()
            val password: String? = binding.userPass?.text?.toString()?.trim()

            if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
                Toast.makeText(this, "не все поля заполнены", Toast.LENGTH_LONG).show()
            } else {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val toast = Toast.makeText(
                                this@MainActivity,
                                "успешно",
                                Toast.LENGTH_LONG
                            )
                            toast.view?.let {
                                it.backgroundTintList = ColorStateList.valueOf(Color.rgb(0, 206, 209))
                                toast.show()
                            }
                            startActivity(Intent(this@MainActivity, MainActivity2::class.java))
                        } else {
                            Toast.makeText(
                                this@MainActivity,
                                "нeуспешно: ${task.exception?.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
            }
        }

        binding.linkToAuth.setOnClickListener {  // переход на регистрацию
            val intent = Intent(this@MainActivity, MainActivity2::class.java)
            startActivity(intent)
        }

    }

}