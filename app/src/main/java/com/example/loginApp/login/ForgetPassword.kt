package com.example.loginApp.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.loginApp.R
import com.example.loginApp.signUp.CodePage
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.hbb20.CountryCodePicker
import java.util.*

class ForgetPassword : AppCompatActivity() {

    private var countryCodePicker: CountryCodePicker? = null
    var completePhoneNo: String? = null
    private var _phoneNo: String? = null
    var phoneNo: TextInputLayout? = null
    lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)
        progressBar = findViewById(R.id.ff_progressbar)
        countryCodePicker = findViewById(R.id.ff_country_code_picker)
        phoneNo = findViewById(R.id.ff_phoneNo)
        progressBar.visibility = View.INVISIBLE
    }

    private fun valid_Phone(): Boolean {
        val `val` = phoneNo!!.editText?.text.toString()
        return if (`val`.isEmpty()) {
            phoneNo!!.error = "field cannot be Empty"
            false
        } else {
            phoneNo!!.error = null
            phoneNo!!.isErrorEnabled = false
            true
        }
    }

    fun getMakeSelectionPage(view: View?) {
        if (!valid_Phone()) return
        progressBar.visibility = View.VISIBLE
        _phoneNo = phoneNo!!.editText?.text.toString().trim { it <= ' ' }
        completePhoneNo = "+91$_phoneNo"
        val checkUser = FirebaseDatabase.getInstance().getReference("User").orderByKey().equalTo(completePhoneNo)
        checkUser.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val intent = Intent(applicationContext, CodePage::class.java)
                    intent.putExtra("phoneNo", completePhoneNo)
                    intent.putExtra("whatTODO", "update")
                    progressBar.visibility = View.INVISIBLE
                    startActivity(intent)
                    finish()
                } else {
                    progressBar.visibility = View.INVISIBLE
                    Toast.makeText(this@ForgetPassword, "Data doesn't Exist", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                progressBar.visibility = View.INVISIBLE
                Toast.makeText(this@ForgetPassword, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}