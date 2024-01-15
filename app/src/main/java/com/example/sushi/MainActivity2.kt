package com.example.sushi

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.sushi.databinding.ActivityMain2Binding
import com.google.firebase.auth.FirebaseAuth

class MainActivity2 : AppCompatActivity() {

    private lateinit var binding: ActivityMain2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonAuth.setOnClickListener {
            val email: String = binding.userEmailAuth.text.toString().trim()
            val password: String = binding.userPassAuth.text.toString().trim()
            if (email.isNotEmpty() && password.isNotEmpty()) {

                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val toast = Toast.makeText(
                                this,
                                "успешно",
                                Toast.LENGTH_LONG
                            )
                            if (toast.view != null) {
                                toast.view!!.backgroundTintList =
                                    ColorStateList.valueOf(Color.rgb(0, 206, 209))
                                toast.show()
                            }
                            val intent = Intent(this@MainActivity2, MenuActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(
                                this@MainActivity2,
                                "нeуспешно" + task.exception!!.message,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }.addOnFailureListener {
                        Toast.makeText(
                            this@MainActivity2,
                            "нeуспешно" + it!!.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
            }
        }
        binding.linkToReg.setOnClickListener {  // переход
            val intent = Intent(this@MainActivity2, MainActivity::class.java)
            startActivity(intent)
        }
    }
}