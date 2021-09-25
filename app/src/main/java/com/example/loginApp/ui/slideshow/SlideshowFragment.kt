package com.example.loginApp.ui.slideshow

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.loginApp.MainPage
import com.example.loginApp.R
import com.example.loginApp.SessionManager
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SlideshowFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val sessionManager = SessionManager(requireActivity())
        val hashMap = sessionManager.userDetailFromSession
        val phoneNo = hashMap[SessionManager.KEY_PHONE_NO]

        return if (sessionManager.checkLogin()) {

            val root = inflater.inflate(R.layout.fragment_slideshow, container, false)
            val profileName : TextInputLayout = root.findViewById(R.id.profileName)
            val profileEmail : TextInputLayout = root.findViewById(R.id.profileEmail)
            val profileNumber : TextInputLayout = root.findViewById(R.id.profilePhoneNo)
            val nameView : TextView = root.findViewById(R.id.nameProfileView)
            val userNameView : TextView = root.findViewById(R.id.usernameProfileView)
            val addressView : TextView = root.findViewById(R.id.addressTextView)
            val stateView : TextView = root.findViewById(R.id.stateTextView)
            val logoutButton : CardView  = root.findViewById(R.id.profileLogoutButton)
            val addressButton : Button  = root.findViewById(R.id.profileChangeOrAddButton)


            FirebaseDatabase.getInstance().getReference("User").child(phoneNo!!).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        nameView.text = snapshot.child("FullName").getValue(String::class.java)
                        userNameView.text = snapshot.child("Email-id").getValue(String::class.java)
                        profileName.editText?.setText(snapshot.child("FullName").getValue(String::class.java))
                        profileEmail.editText?.setText(snapshot.child("Email-id").getValue(String::class.java))
                        profileNumber.editText?.setText(snapshot.child("PhoneNo").getValue(String::class.java))
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
            FirebaseDatabase.getInstance().getReference("User").child(phoneNo).child("My Address")
                .orderByChild("selected").equalTo(true).addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            for (snap in snapshot.children) {
                                val address = snap.child("houseNo").getValue(String::class.java) + ", " +
                                        snap.child("colony").getValue(String::class.java) + ", " +
                                        snap.child("city").getValue(String::class.java)
                                val state = snap.child("state").getValue(String::class.java) + " - " +
                                        snap.child("pinCode").getValue(String::class.java)
                                addressView.text = address
                                stateView.text = state
                            }
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })

            logoutButton.setOnClickListener {
                FirebaseAuth.getInstance().signOut()
                sessionManager.logoutUserFromSession()
                /*val intent = Intent(requireActivity().baseContext, MainActivity23::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)*/
                activity?.onBackPressed()
            }
            addressButton.setOnClickListener {
                val action = SlideshowFragmentDirections.actionNavSlideshowToShowAddressFragment("profilePage")
                Navigation.findNavController(root).navigate(action)
            }
            root
        } else {
            startActivity(Intent(requireActivity(), MainPage::class.java))
            null
        }
    }
}