package com.example.loginApp.signUp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.loginApp.R

class SignUpPage2 : AppCompatActivity() {
    var gender: String? = null
    var date: String? = null
    var radioGroup: RadioGroup? = null
    var datePicker: DatePicker? = null
    lateinit var radioButton: RadioButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signuppage2)
        radioGroup = findViewById(R.id.gender_selection)
        datePicker = findViewById(R.id.date_picker)
    }

    fun getSignInPage3(view: View?) {
        radioButton = findViewById(radioGroup!!.checkedRadioButtonId)
        gender = radioButton.getText().toString()
        date = datePicker!!.dayOfMonth.toString() + " / " + datePicker!!.month + " / " + datePicker!!.year
        val intent = Intent(this@SignUpPage2, SignInPage3::class.java)
        val bundle = getIntent().extras
        intent.putExtra("page1", bundle)
        intent.putExtra("gender", gender)
        intent.putExtra("date", date)
        startActivity(intent)
    }
}