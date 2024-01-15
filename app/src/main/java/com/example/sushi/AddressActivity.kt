package com.example.sushi


import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.sushi.data.CartItem
import com.example.sushi.data.Order
import com.example.sushi.data.address
import com.example.sushi.data.users
import com.example.sushi.databinding.ActivityAddressBinding
import com.example.sushi.ui.profile.ProfileFragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore


class AddressActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddressBinding
    private var addressDetails : address? = null
    private var q = ""
//    private lateinit var CartListItem: ArrayList<CartItem>
//    private var total = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)


        FirebaseFirestore.getInstance().collection("users")
            .document(ProfileFragment().getCurrntUserId()) //вызов функции получения айди юзера
            .get()

            .addOnSuccessListener { document ->
                val user = document.toObject(users::class.java)
                if ( user != null) {
                    val name = user!!.name
                    binding.etName.text = name.toEditable() // подтягиваем значения
                    val phone = user!!.phone
                    binding.etPhone.text = phone.toEditable() // подтягиваем значения
                }
            }


        if (intent.hasExtra("address_details")){ // принимаем занчения которые заполняют адрес из MyAddressActivity
            addressDetails = intent.getParcelableExtra("address_details")!!
        }

        if (intent.hasExtra("q")){ // принятие
            q = intent.getStringExtra("q").toString()
            binding.btnCheck.visibility = View.GONE
        }
//        if (intent.hasExtra("cart_list_item")){ // принимаем занчения которые заполняют адрес из MyAddressActivity
//            CartListItem = intent.getParcelableExtra("cart_list_item")!!
//        }

//        if (intent.hasExtra("total")){
//            total = intent.getParcelableExtra("total")!!
//        }

        if(addressDetails != null){
            binding.addAddress.text = "Изменить адрес" //dинамисечкое изменение от ситуации
            binding.btnAddress.visibility = View.VISIBLE
            binding.container2.visibility = View.VISIBLE
            binding.btnCheck.visibility = View.VISIBLE
            binding.etStreet.setText(addressDetails?.street)
                binding.etHome.setText(addressDetails?.home)
                binding.etKorpus.setText(addressDetails?.korpus)
                binding.etUnder.setText(addressDetails?.under)
                binding.etDomofon.setText(addressDetails?.domofon)
                binding.etFloor.setText(addressDetails?.floor)
                binding.etFlat.setText(addressDetails?.flat)

        }
        binding.addAddress.setOnClickListener{
            if(addressDetails != null) {
                var userHashMap: HashMap<String, Any> = HashMap()
                userHashMap["street"] = binding.etStreet.text.toString()
                userHashMap["home"] = binding.etHome.text.toString()
                userHashMap["korpus"] = binding.etKorpus.text.toString()
                userHashMap["under"] = binding.etUnder.text.toString()
                userHashMap["domofon"] = binding.etDomofon.text.toString()
                userHashMap["floor"] = binding.etFloor.text.toString()
                userHashMap["flat"] = binding.etFlat.text.toString()
                FirebaseFirestore.getInstance().collection("addresses")
                    .document(addressDetails!!.id)
                    .update(userHashMap)
                    .addOnSuccessListener {
                        val intent = Intent(this@AddressActivity,MyAddressActivity::class.java)
                        startActivity(intent)
                    }
            } else {
                saveAddressToFireStore()
            }
        }
        binding.btnAddress.setOnClickListener{
            val intent = Intent(this@AddressActivity,MyAddressActivity::class.java)
            startActivity(intent)
        }

        binding.btnCheck.setOnClickListener{
            val order = Order(price = addressDetails!!.total, title = System.currentTimeMillis().toString())
            FB().uploadOrder(order, this)
        }

        setUpActionBar()
    }

    fun uploadSuccess() {
        val intent = Intent(this@AddressActivity, MenuActivity::class.java)
        startActivity(intent)
    }

    fun String.toEditable(): Editable =
        Editable.Factory.getInstance().newEditable(this) // помещаем значение в едит текст

    private fun setUpActionBar() {
        setSupportActionBar(binding.toolbarAddress)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_24)

        binding.toolbarAddress.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    fun showErrorSnackBar(message : String, errorMessage : Boolean){
        val snackBar = Snackbar.make(findViewById(android.R.id.content),
            message,
            Snackbar.LENGTH_LONG)

        val snackBarView = snackBar.view

        if (errorMessage){
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(this@AddressActivity,R.color.colorSnackBarError)
            )
        }else{
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(this@AddressActivity,R.color.colorSnackBarSuccess)
            )
        }
        snackBar.show()
    }

    private fun validateData() : Boolean{

        return when{
            TextUtils.isEmpty(binding.etStreet.text.toString().trim{ it <= ' '})->{
                showErrorSnackBar("Please enter your full street.",true)
                false
            }
            TextUtils.isEmpty(binding.etHome.text.toString().trim{ it <= ' '})->{
                showErrorSnackBar("Please enter your home.",true)
                false
            }
            TextUtils.isEmpty(binding.etKorpus.text.toString().trim{ it <= ' '})->{
                showErrorSnackBar("Please enter your korpus.",true)
                false
            }
            TextUtils.isEmpty(binding.etUnder.text.toString().trim{ it <= ' '})->{
                showErrorSnackBar("Please enter your under.",true)
                false
            }
            TextUtils.isEmpty(binding.etDomofon.text.toString().trim{ it <= ' '})->{
                showErrorSnackBar("Please enter your domofon.",true)
                false
            }
            TextUtils.isEmpty(binding.etFloor.text.toString().trim{ it <= ' '})->{
                showErrorSnackBar("Please enter your floor.",true)
                false
            }
            TextUtils.isEmpty(binding.etFlat.text.toString().trim{ it <= ' '})->{
                showErrorSnackBar("Please enter your flat.",true)
                false
            }
            else->{
                true
            }
        }

    }



    private fun saveAddressToFireStore() {
        val street: String = binding.etStreet.text.toString().trim { it <= ' ' }
        val home: String = binding.etHome.text.toString().trim { it <= ' ' }
        val korpus: String = binding.etKorpus.text.toString().trim { it <= ' ' }
        val under: String = binding.etUnder.text.toString().trim { it <= ' ' }
        val domofon: String = binding.etDomofon.text.toString().trim { it <= ' ' }
        val floor: String = binding.etFloor.text.toString().trim { it <= ' ' }
        val flat: String = binding.etFlat.text.toString().trim { it <= ' ' }
        val name: String = binding.etName.text.toString().trim { it <= ' ' }
        val phone: String = binding.etPhone.text.toString().trim { it <= ' ' }
        val user_id: String = FB().getCurrntUserId()

        if (validateData()){

            val addressModel = address(
                user_id = user_id,
                name = name,
                mobileNumber = phone,
                street = street,
                home = home,
                korpus = korpus,
                under = under,
                domofon = domofon,
                floor = floor,
                flat = flat,
            )
            val x = addressModel
            FB().addAddress(this, addressModel)
        }

    }

    fun addUpdateAddressSuccess(){
        if(addressDetails != null && addressDetails!!.id.isNotEmpty()){
            Toast.makeText(this, getString(R.string.msg_your_address_updated_successfully), Toast.LENGTH_SHORT).show()
        }else {
            Toast.makeText(this, getString(R.string.address_added_success_msg), Toast.LENGTH_SHORT)
                .show()
        }
        setResult(Activity.RESULT_OK)
        finish()
    }


}