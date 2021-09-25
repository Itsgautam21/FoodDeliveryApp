package com.example.loginApp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.loginApp.login.LoginPage
import com.example.loginApp.signUp.Signup

class MainPage : AppCompatActivity() {

    var login_btn: Button? = null
    var sign_btn: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mainpage)
        login_btn = findViewById(R.id.login_btn)
        sign_btn = findViewById(R.id.sign_btn)
    }

    fun getLoginPage(view: View?) {
        val intent = Intent(applicationContext, LoginPage::class.java)
        /*val pair: Array<Pair<*, *>?> = arrayOfNulls(1)
        pair[0] = Pair<View?, String>(login_btn, "login_trans")
        val activityOptions = ActivityOptions.makeSceneTransitionAnimation(this@MainPage, *pair)*/
        //startActivity(intent, activityOptions.toBundle())
        startActivity(intent)
        finish()
    }

    fun getSignInPage(view: View?) {
        val intent = Intent(applicationContext, Signup::class.java)
        /*val pair: Array<Pair<*, *>?> = arrayOfNulls(1)
        pair[0] = Pair<View?, String>(sign_btn, "sign_trans")
        val activityOptions = ActivityOptions.makeSceneTransitionAnimation(this@MainPage, *pair, "")
        startActivity(intent, activityOptions.toBundle())*/
        startActivity(intent)
        finish()
    }
}