package com.example.sushi.ui.profile

import android.app.AlertDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.sushi.MainActivity2
import com.example.sushi.MenuActivity
import com.example.sushi.MyAddressActivity
import com.example.sushi.R
import com.example.sushi.ShoppingBagActivity
import com.example.sushi.data.users


import com.example.sushi.databinding.FragmentProfileBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    fun getCurrntUserId(): String { // ПОЛУЧЕНИЕ ID ЮЗЕРА
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserId = ""
        if (currentUser != null) {
            currentUserId = currentUser.uid
        }
        return currentUserId
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentProfileBinding.bind(view) // запрет изменения пофиля
        _binding = binding
        binding.name.isEnabled = false
        binding.phone.isEnabled = false

        var userid = getCurrntUserId() // получение id
        val Newuser = users(userid)

        FirebaseFirestore.getInstance().collection("users") // чтение объекта (юзера)
            .document(userid)
            .get()
            .addOnSuccessListener { document ->
                val user = document.toObject(users::class.java)
                if (user != null) {
                    binding.nameText.text = user.name.toEditable()
                    binding.phoneText.text = user.phone.toEditable()
                }else {
                    FirebaseFirestore.getInstance().collection("users") // создание нового документа в коллекции
                        .document(Newuser!!.id)
                        .set(Newuser!!, SetOptions.merge()) // set - создание нового документа в коллекции
                }
            }


        binding.btnVihod.setOnClickListener {
            val vihod = AlertDialog.Builder(requireActivity(),R.style.MyDialogTheme) // всплывающие окно
            vihod.setMessage("вы уверены что хотите выйти?")
            vihod.setIcon(android.R.drawable.alert_light_frame)
            vihod.setPositiveButton("да") { dialogInterface, _ ->
                dialogInterface.dismiss()
                FirebaseAuth.getInstance().signOut()//выход
                val intent = Intent(requireActivity(), MainActivity2::class.java)
                startActivity(intent)

            }
            vihod.setNegativeButton("нет") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            val dialog = vihod.create()
            dialog.setCancelable(false) //одно из двух
            dialog.show()
        }

        binding.address.setOnClickListener { view ->
            val intent = Intent(requireActivity(), MyAddressActivity::class.java)
            intent.putExtra("x", "x")
            startActivity(intent)
        }

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.edit_item -> {
                edit()

                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun String.toEditable(): Editable =
        Editable.Factory.getInstance().newEditable(this)

    fun validatePhone() : Boolean { //телефон проверка на не пустое
        return when {
            TextUtils.isEmpty(binding.phoneText.text.toString()) -> {
                Toast.makeText(requireActivity(), "Заполните телефон", Toast.LENGTH_LONG).show()
                false
            }
            else -> {
                true
            }
        }
    }

    fun edit() {
        binding.name.isEnabled = true
        binding.phone.isEnabled = true
        binding.hiddenButton.isVisible = true

        binding.hiddenButton.setOnClickListener { // добавление объекта юзера в базу данных
            binding.hiddenButton.visibility = View.GONE
            if (validatePhone()) {
                var userHashMap: HashMap<String, Any> = HashMap()
                userHashMap["name"] = binding.nameText.text.toString()
                userHashMap["phone"] = binding.phoneText.text.toString()
                FirebaseFirestore.getInstance().collection("users")
                    .document(getCurrntUserId())
                    .update(userHashMap)
                    .addOnSuccessListener {
                        startActivity(
                            Intent(requireActivity(), MenuActivity::class.java)
                        )// динамическиое изменение данных в меню
                    }

                binding.name.isEnabled = false
                binding.phone.isEnabled = false

//                startActivity(
//                    Intent(requireActivity(), MenuActivity::class.java)
//                )// динамическиое изменение данных в меню
            } else{
                binding.hiddenButton.visibility = View.VISIBLE
            }
            }
        }
//    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.profile, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}