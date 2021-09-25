package com.example.loginApp.ui.slideshow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.loginApp.R
import com.example.loginApp.SessionManager
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*

class AddAddressFragment : Fragment() {

    private var value : String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val args = AddAddressFragmentArgs.fromBundle(requireArguments())
        value = args.name

        val view = inflater.inflate(R.layout.fragment_add_adress, container, false)
        val name : TextInputLayout = view.findViewById(R.id.fullNameADD)
        val phoneNo : TextInputLayout = view.findViewById(R.id.PhoneNoAdd)
        val pinCode : TextInputLayout = view.findViewById(R.id.pinCodeAdd)
        val state : TextInputLayout = view.findViewById(R.id.stateAdd)
        val city : TextInputLayout = view.findViewById(R.id.cityADD)
        val houseNo : TextInputLayout = view.findViewById(R.id.houseNoAdd)
        val colony : TextInputLayout = view.findViewById(R.id.roadNameADD)
        val saveButton : Button = view.findViewById(R.id.saveAddress)

        if (value != "Add") {
            val phone = SessionManager(requireActivity()).userDetailFromSession[SessionManager.KEY_PHONE_NO]
            FirebaseDatabase.getInstance().getReference("User").child(phone!!).child("My Address")
                .child(value).addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            name.editText?.setText(snapshot.child("name").getValue(String::class.java))
                            phoneNo.editText?.setText(snapshot.child("phoneNo").getValue(String::class.java))
                            pinCode.editText?.setText(snapshot.child("pinCode").getValue(String::class.java))
                            state.editText?.setText(snapshot.child("state").getValue(String::class.java))
                            city.editText?.setText(snapshot.child("city").getValue(String::class.java))
                            houseNo.editText?.setText(snapshot.child("houseNo").getValue(String::class.java))
                            colony.editText?.setText(snapshot.child("colony").getValue(String::class.java))
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {

                    }

                })
        }

        saveButton.setOnClickListener {

            val sessionManager = SessionManager(requireActivity())
            val phone = sessionManager.userDetailFromSession[SessionManager.KEY_PHONE_NO]
            val map = HashMap<String, Any>()
            map["name"] = name.editText?.text.toString()
            map["phoneNo"] = phoneNo.editText?.text.toString()
            map["state"] = state.editText?.text.toString()
            map["houseNo"] = houseNo.editText?.text.toString()
            map["colony"] = colony.editText?.text.toString()
            map["pinCode"] = pinCode.editText?.text.toString()
            map["city"] = city.editText?.text.toString()
            assert(phone != null)

            if (value == "Add") {
                map["selected"] = false
                map["index"] = -1
                FirebaseDatabase.getInstance().getReference("User").child(phone!!).child("My Address")
                    .push().setValue(map)
                Toast.makeText(requireActivity(), "Address saved", Toast.LENGTH_SHORT).show()
                activity?.onBackPressed()
            } else {
                FirebaseDatabase.getInstance().getReference("User").child(phone!!).child("My Address")
                    .child(value).updateChildren(map)
                Toast.makeText(requireActivity(), "Address updated", Toast.LENGTH_SHORT).show()
                activity?.onBackPressed()
            }
        }
        return view
    }
}