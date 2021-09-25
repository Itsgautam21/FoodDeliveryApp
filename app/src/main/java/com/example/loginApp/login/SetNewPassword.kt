package com.example.loginApp.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.loginApp.R
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class SetNewPassword : AppCompatActivity() {
    var newPassword: TextInputLayout? = null
    var newConfirmPassword: TextInputLayout? = null
    private var newPass: String? = null
    private var newConfirmPass: String? = null
    var phoneNo: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setnewpassword)
        newPassword = findViewById(R.id.new_password)
        newConfirmPassword = findViewById(R.id.new_confirm_password)
    }

    fun getForget_password_success_message_page(view: View?) {
        newPass = newPassword!!.editText?.text.toString().trim { it <= ' ' }
        newConfirmPass = newConfirmPassword!!.editText?.text.toString().trim { it <= ' ' }
        phoneNo = intent.getStringExtra("phoneNo")
        if (newPass == newConfirmPass) {
            val reference = FirebaseDatabase.getInstance().getReference("User")
            reference.child(phoneNo!!).child("Password").setValue(newConfirmPass)
            val intent = Intent(this@SetNewPassword, Forget_password_Success_message::class.java)
            startActivity(intent)
            finish()
        } else newConfirmPassword!!.error = "Password does not match"
    }
}