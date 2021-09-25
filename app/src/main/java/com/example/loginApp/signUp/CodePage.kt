package com.example.loginApp.signUp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.chaos.view.PinView
import com.example.loginApp.login.SetNewPassword
import com.example.loginApp.R
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.google.firebase.database.FirebaseDatabase
import java.util.*
import java.util.concurrent.TimeUnit

class CodePage : AppCompatActivity() {

    var codeSentBySystem: String? = null
    var pinView: PinView? = null
    var phoneNo: String? = null
    var fullName: String? = null
    var password: String? = null
    private var emailId: String? = null
    private var username: String? = null
    private var gender: String? = null
    private var date: String? = null
    private var forgotPasswordPage: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_code_page)
        pinView = findViewById(R.id.pinView)
        val set_phoneNO : TextView = findViewById(R.id.set_phoneNo)
        fullName = intent.getStringExtra("fullName")
        username = intent.getStringExtra("username")
        emailId = intent.getStringExtra("emailId")
        password = intent.getStringExtra("password")
        phoneNo = intent.getStringExtra("phoneNo")
        date = intent.getStringExtra("date")
        gender = intent.getStringExtra("gender")
        forgotPasswordPage = intent.getStringExtra("whatTODO")
        set_phoneNO.text = phoneNo
        sendVerificationCodeToUser(phoneNo)
    }

    private fun sendVerificationCodeToUser(_phoneNo: String?) {
        val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
            .setPhoneNumber(_phoneNo!!) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(mCallbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private val mCallbacks : OnVerificationStateChangedCallbacks =
        object : OnVerificationStateChangedCallbacks() {

            override fun onCodeSent(s: String, forceResendingToken: ForceResendingToken) {
                super.onCodeSent(s, forceResendingToken)
                codeSentBySystem = s
            }

            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                val code = phoneAuthCredential.smsCode
                if (code != null) {
                    pinView!!.setText(code)
                }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(this@CodePage, e.message, Toast.LENGTH_LONG).show()
            }
        }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task: Task<AuthResult?> ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Verification Completed", Toast.LENGTH_LONG).show()
                    if (forgotPasswordPage == "update") updatePasswordData() else storeNewUserData()
                } else {
                    if (task.exception is FirebaseAuthInvalidCredentialsException)
                        Toast.makeText(this, "Verification is Not Completed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun updatePasswordData() {
        val intent = Intent(applicationContext, SetNewPassword::class.java)
        intent.putExtra("phoneNo", phoneNo)
        startActivity(intent)
        finish()
    }

    private fun storeNewUserData() {
        val reference = FirebaseDatabase.getInstance().getReference("User")
        reference.child(phoneNo!!).child("FullName").setValue(fullName)
        reference.child(phoneNo!!).child("Username").setValue(username)
        reference.child(phoneNo!!).child("Email-id").setValue(emailId)
        reference.child(phoneNo!!).child("PhoneNo").setValue(phoneNo)
        reference.child(phoneNo!!).child("Gender").setValue(gender)
        reference.child(phoneNo!!).child("Date of Birth").setValue(date)
        reference.child(phoneNo!!).child("Password").setValue(password)
        finish()
    }

    private fun verifyCode(code: String) {
        val phoneAuthCredential = PhoneAuthProvider.getCredential(codeSentBySystem!!, code)
        signInWithPhoneAuthCredential(phoneAuthCredential)
    }

    fun onClickVerification(view: View?) {
        val code = Objects.requireNonNull(pinView!!.text).toString()
        if (!code.isEmpty()) verifyCode(code)
    }
}