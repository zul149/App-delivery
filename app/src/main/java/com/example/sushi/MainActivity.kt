package com.example.sushi

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.sushi.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

    private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            val intent = Intent(this@MainActivity, MainActivity2::class.java)
            startActivity(intent)

            val email: String = binding.userEmail.text.toString().trim()
            val password: String = binding.userPass.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (email == "" || password == "" ) {
                            Toast.makeText(this, "не все поля заполнены", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(this, "нeуспешно" + task.exception!!.message, Toast.LENGTH_LONG).show()
                        }
                        if (task.isSuccessful) {
                            val toast = Toast.makeText(
                                this@MainActivity,
                                "успешно",
                                Toast.LENGTH_LONG
                            )
                            if (toast.view != null) {
                                toast.view!!.backgroundTintList =
                                    ColorStateList.valueOf(Color.rgb(0, 206, 209))
                                toast.show()
                            }
                            startActivity(Intent(this@MainActivity, MainActivity2::class.java))
                        } else {
                            Toast.makeText(
                                this@MainActivity,
                                "нeуспешно" + task.exception!!.message,
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