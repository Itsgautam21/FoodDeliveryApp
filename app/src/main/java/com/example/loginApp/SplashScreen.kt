package com.example.loginApp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        val sessionManager = SessionManager(this)
        Handler().postDelayed({
            if (sessionManager.checkLogin()) {
                val intent = Intent(applicationContext, MainActivity23::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(applicationContext, MainPage::class.java)
                startActivity(intent)
                finish()
            }
        }, 1000)
    }
}