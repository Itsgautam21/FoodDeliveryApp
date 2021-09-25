package com.example.loginApp.login

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.loginApp.MainPage
import com.example.loginApp.R
import com.example.loginApp.SessionManager
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.hbb20.CountryCodePicker
import java.util.*

class LoginPage : AppCompatActivity() {

    private var countryCodePicker : CountryCodePicker? = null
    private var phoneNo: TextInputLayout? = null
    private var password: TextInputLayout? = null
    private lateinit var progressBar : ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loginpage)
        countryCodePicker = findViewById(R.id.login_country_code_picker)
        phoneNo = findViewById(R.id.login_phoneNo)
        password = findViewById(R.id.login_password)
        progressBar = findViewById(R.id.progressbar)
        progressBar.visibility = View.INVISIBLE
    }

    private fun validPhone() : Boolean {
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

    private fun validPassword() : Boolean {
        val `val` = password!!.editText?.text.toString()
        return if (`val`.isEmpty()) {
            password!!.error = "field cannot be Empty"
            false
        } else {
            password!!.error = null
            password!!.isErrorEnabled = false
            true
        }
    }

    fun getForgetPasswordPage(view: View?) {
        val intent = Intent(this@LoginPage, ForgetPassword::class.java)
        startActivity(intent)
    }

    private fun isConnected(loginPage: LoginPage): Boolean {
        val connectivityManager =
            loginPage.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        val mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
        return wifiInfo != null && wifiInfo.isConnected || mobileInfo != null && mobileInfo.isConnected
    }

    private fun warningDialogue() {
        val alertDialog = AlertDialog.Builder(this@LoginPage)
        alertDialog.setMessage("You are not currently connected to the Internet press 'Connect' for the Internet")
            .setCancelable(false)
            .setPositiveButton("connect") { dialog : DialogInterface?, which : Int ->
                startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
            }
            .setNegativeButton("quit") { dialog: DialogInterface?, which : Int ->
                startActivity(Intent(applicationContext, MainPage::class.java))
                finish()
            }.show()
    }

    @SuppressLint("RestrictedApi")
    fun letTheUserLogin(view: View?) {
        if (!validPhone() or !validPassword()) return
        progressBar.visibility = View.VISIBLE
        if (!isConnected(this)) warningDialogue()
        val _phoneNo = phoneNo!!.editText?.text.toString()
        val _password = password!!.editText?.text.toString()
        val completePhoneNo = "+91$_phoneNo"

        /*Intent intent  = new Intent(getApplicationContext(), Code_page.class);
        intent.putExtra("phoneNo", completePhoneNo);
        intent.putExtra("loginPage", "logIn");
        startActivity(intent);
        finish();*/
        val checkUser = FirebaseDatabase.getInstance().getReference("User").orderByChild("PhoneNo")
            .equalTo(completePhoneNo)
        checkUser.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val pass = snapshot.child(completePhoneNo).child("Password").getValue(String::class.java)!!
                    if (pass == _password) {
                        val fullName = snapshot.child(completePhoneNo).child("FullName").getValue(String::class.java)
                        val username = snapshot.child(completePhoneNo).child("Username").getValue(String::class.java)
                        val email_id = snapshot.child(completePhoneNo).child("Email-id").getValue(String::class.java)
                        val Password = snapshot.child(completePhoneNo).child("Password").getValue(String::class.java)
                        val DOB = snapshot.child(completePhoneNo).child("Date of Birth").getValue(String::class.java)
                        val gender = snapshot.child(completePhoneNo).child("Gender").getValue(String::class.java)
                        val phone = snapshot.child(completePhoneNo).child("PhoneNo").getValue(String::class.java)
                        val sessionManager = SessionManager(this@LoginPage)
                        sessionManager.createLoginSession(fullName, username, email_id, Password, DOB, gender, phone)
                        progressBar.visibility = View.GONE
                        /*val intent = Intent(baseContext, MainActivity23::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                        startActivity(intent)*/
                        finish()
                    } else {
                        progressBar.visibility = View.GONE
                        Toast.makeText(this@LoginPage, "Password does not match", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this@LoginPage, "Data does not exist", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@LoginPage, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun error(view: View?) {
        phoneNo!!.error = null
        phoneNo!!.isErrorEnabled = false
    }
}