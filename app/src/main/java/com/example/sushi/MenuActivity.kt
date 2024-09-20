package com.example.sushi

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.sushi.data.users
import com.example.sushi.databinding.ActivityMenuBinding
import com.example.sushi.databinding.NavHeaderMainBinding

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject

class MenuActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBar.toolbar)

        val drawerMenu = binding.navView
        val header = drawerMenu.getHeaderView(0)
        val headerBinding = NavHeaderMainBinding.bind(header)

        val id = FirebaseAuth.getInstance().currentUser

        if (id != null) {
            val id = id.uid
            FirebaseFirestore.getInstance().collection("users")
                .document(id)
                .get()
                .addOnSuccessListener { document ->
                    val user = document.toObject(users::class.java)
                    if (user != null) {
                        headerBinding.textViewName.text = user.name
                        headerBinding.textViewPhone.text = user.phone
                    }
                }
        } else {
            // Обработка случая, если пользователь не авторизован
            Toast.makeText(this, "Пользователь не авторизован", Toast.LENGTH_LONG).show()
            // Можешь, например, перекинуть его на экран авторизации
            startActivity(Intent(this, MainActivity2::class.java))
            finish() // Завершаем текущую активность
        }


        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Передача идентификатора каждого меню в виде набора идентификаторов, поскольку каждое
        // меню следует рассматривать как пункты назначения верхнего уровня.
        FirebaseFirestore.getInstance().collection("users").document(FB().getCurrntUserId()).get()
            .addOnSuccessListener {
                val user = it.toObject(users::class.java)
                if(user?.phone.isNullOrEmpty()) {
//                    drawerLayout.visibility = View.VISIBLE
                    appBarConfiguration = AppBarConfiguration(
                        setOf(
                            R.id.nav_home, R.id.nav_profile, R.id.nav_order
                        )
                    )
                    setupActionBarWithNavController(navController, appBarConfiguration)
                    navView.setupWithNavController(navController)
                } else {
                    appBarConfiguration = AppBarConfiguration(
                        setOf(
                            R.id.nav_home, R.id.nav_profile, R.id.nav_order
                        ),drawerLayout
                    )
                    setupActionBarWithNavController(navController, appBarConfiguration)
                    navView.setupWithNavController(navController)
                }
            }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Расширьте меню; это добавит элементы на панель действий, если она присутствует.
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}