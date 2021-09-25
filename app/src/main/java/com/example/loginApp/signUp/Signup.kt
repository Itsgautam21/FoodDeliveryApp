package com.example.loginApp.signUp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.loginApp.R
import com.google.android.material.textfield.TextInputLayout
import java.util.*

class Signup : AppCompatActivity() {
    var arrow: ImageView? = null
    var create: TextView? = null
    var name: TextInputLayout? = null
    var pass: TextInputLayout? = null
    var Email: TextInputLayout? = null
    var user: TextInputLayout? = null
    var next: Button? = null
    var login: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        arrow = findViewById(R.id.arrow_image)
        create = findViewById(R.id.create_account)
        name = findViewById(R.id.fullname)
        user = findViewById(R.id.username)
        Email = findViewById(R.id.email)
        pass = findViewById(R.id.password_signin)
        next = findViewById(R.id.next1)
        login = findViewById(R.id.login1)
    }

    private fun valid_Name(): Boolean {
        val `val` = name!!.editText?.text.toString()
        return if (`val`.isEmpty()) {
            name!!.error = "field cannot be Empty"
            false
        } else {
            name!!.error = null
            name!!.isErrorEnabled = false
            true
        }
    }

    private fun valid_Username(): Boolean {
        val `val` = user!!.editText?.text.toString()
        return if (`val`.isEmpty()) {
            user!!.error = "field cannot be Empty"
            false
        } else {
            user!!.error = null
            user!!.isErrorEnabled = false
            true
        }
    }

    private fun valid_Email(): Boolean {
        val `val` = Email!!.editText?.text.toString()
        return if (`val`.isEmpty()) {
            Email!!.error = "field cannot be Empty"
            false
        } else {
            Email!!.error = null
            Email!!.isErrorEnabled = false
            true
        }
    }

    private fun valid_Password(): Boolean {
        val `val` = pass!!.editText?.text.toString()
        return if (`val`.isEmpty()) {
            pass!!.error = "field cannot be Empty"
            false
        } else {
            pass!!.error = null
            pass!!.isErrorEnabled = false
            true
        }
    }

    fun getSignInPage2(view: View?) {
        if (!valid_Name() or !valid_Username() or !valid_Email() or !valid_Password()) return
        val fullName = name!!.editText?.text.toString().trim { it <= ' ' }
        val username = user!!.editText?.text.toString().trim { it <= ' ' }
        val email_id = Email!!.editText?.text.toString().trim { it <= ' ' }
        val password = pass!!.editText?.text.toString().trim { it <= ' ' }
        val intent = Intent(this@Signup, SignUpPage2::class.java)
        intent.putExtra("fullName", fullName)
        intent.putExtra("username", username)
        intent.putExtra("email_id", email_id)
        intent.putExtra("password", password)

        startActivity(intent)
    }
}