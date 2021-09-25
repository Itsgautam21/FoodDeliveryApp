package com.example.loginApp.login

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.loginApp.R

class Forget_password_Success_message : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password__success_message)
    }

    fun backToLoginPage(view: View?) {
        finish()
    }
}