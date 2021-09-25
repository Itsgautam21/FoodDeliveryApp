package com.example.loginApp.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.loginApp.R
import com.example.loginApp.signUp.CodePage

class MakeSelection : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_makeselection)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    fun getCode_page(view: View?) {
        val intent = Intent(this@MakeSelection, CodePage::class.java)
        startActivity(intent)
    }
}