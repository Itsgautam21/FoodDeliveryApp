package com.example.loginApp.signUp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.loginApp.R
import com.google.android.material.textfield.TextInputLayout
import com.hbb20.CountryCodePicker

class SignInPage3 : AppCompatActivity() {
    var phoneNumber: TextInputLayout? = null
    var countryCodePicker: CountryCodePicker? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signinpage3)
        phoneNumber = findViewById(R.id.edit_phone_number)
        countryCodePicker = findViewById(R.id.country_code_picker)
    }

    private fun valid_Phone(): Boolean {
        val `val` = phoneNumber!!.editText?.text.toString()
        return if (`val`.isEmpty()) {
            phoneNumber!!.error = "field cannot be Empty"
            false
        } else {
            phoneNumber!!.error = null
            phoneNumber!!.isErrorEnabled = false
            true
        }
    }

    fun getCodePage(view: View?) {
        if (!valid_Phone()) return
        val bundle = intent.getBundleExtra("page1")!!
        val fullName = bundle["fullName"] as String?
        val username = bundle["username"] as String?
        val emailId = bundle["email_id"] as String?
        val password = bundle["password"] as String?
        val date = intent.getStringExtra("date")
        val gender = intent.getStringExtra("gender")
        val _getUserEnteredPhoneNumber = phoneNumber!!.editText?.text.toString().trim { it <= ' ' }
        val _phoneNo = "+91$_getUserEnteredPhoneNumber"
        val intent = Intent(applicationContext, CodePage::class.java)
        intent.putExtra("fullName", fullName)
        intent.putExtra("username", username)
        intent.putExtra("emailId", emailId)
        intent.putExtra("password", password)
        intent.putExtra("date", date)
        intent.putExtra("gender", gender)
        intent.putExtra("phoneNo", _phoneNo)
        intent.putExtra("whatTODO", "")
        startActivity(intent)
    }
}