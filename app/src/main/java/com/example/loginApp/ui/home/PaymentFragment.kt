package com.example.loginApp.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.loginApp.SessionManager
import com.example.loginApp.adapters.PaymentAdapter
import com.example.loginApp.databinding.FragmentPaymentBinding
import com.example.loginApp.models.PaymentModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class PaymentFragment : Fragment() {

    private var shopName : String? = null
    private var shopAddress : String? = null
    private var shopImage : String? = null

    private var userName : String? = null
    private var userPhone : String? = null
    private var userAddress : String? = null
    private var userState : String? = null

    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentPaymentBinding.inflate(inflater, container, false)
        val view = binding.root

        val list = ArrayList<PaymentModel>()

        val args = PaymentFragmentArgs.fromBundle(requireArguments())
        val phoneNo = args.phoneNo
        val price = args.price

        binding.showBoldTotal.text = price

        list.add(PaymentModel("UPI"))
        list.add(PaymentModel("Wallets"))
        list.add(PaymentModel("Credit / Debit / ATM Card"))
        list.add(PaymentModel("Net Banking"))
        list.add(PaymentModel("EMI"))
        list.add(PaymentModel("Cash on Delivery"))

        binding.recyclerView.adapter = PaymentAdapter(list)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())


        FirebaseDatabase.getInstance().getReference("Seller").child(phoneNo).
        addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    shopName = snapshot.child("shopName").getValue(String::class.java)
                    shopImage = snapshot.child("shopImage").getValue(String::class.java)
                    shopAddress = snapshot.child("Address").child("roadName").getValue(String::class.java)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
        val userPhoneNo = SessionManager(requireActivity()).userDetailFromSession[SessionManager.KEY_PHONE_NO]
        FirebaseDatabase.getInstance().getReference("User").child(userPhoneNo!!).child("My Address")
            .orderByChild("selected").equalTo(true).addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (snap in snapshot.children) {
                            userName = snap.child("name").getValue(String::class.java)
                            userPhone = snap.child("phoneNo").getValue(String::class.java)
                            val address = snap.child("houseNo").getValue(String::class.java) + ", " +
                                    snap.child("colony").getValue(String::class.java) + ", " +
                                    snap.child("city").getValue(String::class.java)
                            val state = snap.child("state").getValue(String::class.java) + " - " +
                                    snap.child("pinCode").getValue(String::class.java)
                            userAddress = address
                            userState = state
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {

                }

            })

        binding.placeOrder.setOnClickListener {

            val calendar = Calendar.getInstance()
            val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy, HH : mm")
            val date = simpleDateFormat.format(calendar.time)

            val seller = HashMap<String, Any?>()
            seller["buyerName"] = userName
            seller["buyerAddress"] = userAddress
            seller["buyerState"] = userState
            seller["buyerPhoneNo"] = userPhone
            seller["buyerTotalPrice"] = price

            val userAddress = HashMap<String, Any>()
            userAddress["shopImage"] = shopImage!!
            userAddress["shopName"] = shopName!!
            userAddress["shopAddress"] = shopAddress!!
            userAddress["totalPrice"] = price
            userAddress["orderDate"] = date

            val ref = FirebaseDatabase.getInstance().getReference("Seller").child(phoneNo).child("Delivery order")
            val userRef = FirebaseDatabase.getInstance().getReference("User").child(userPhoneNo).child("My Order")
            val key = ref.push().key
            val userKey = userRef.push().key
            userRef.child(userKey!!).setValue(userAddress).addOnSuccessListener {

                ref.child(key!!).setValue(seller).addOnSuccessListener {

                    FirebaseDatabase.getInstance().reference.child("User").child(userPhoneNo)
                        .child("cart").child(phoneNo).addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.exists()) {
                                    for (snap in snapshot.children) {
                                        val hashMap = HashMap<String, Any>()
                                        hashMap["foodImage"] = snap.child("foodImage").getValue(String::class.java)!!
                                        hashMap["foodName"] = snap.child("foodName").getValue(String::class.java)!!
                                        hashMap["foodDescription"] = snap.child("foodDescription").getValue(String::class.java)!!
                                        hashMap["foodPrice"] = snap.child("foodPrice").getValue(String::class.java)!!
                                        hashMap["foodCount"] = snap.child("foodCount").getValue(String::class.java)!!

                                        FirebaseDatabase.getInstance().getReference("User").child(userPhoneNo).child("My Order")
                                            .child(userKey).child("Food").push().setValue(hashMap)

                                        FirebaseDatabase.getInstance().getReference("Seller")
                                            .child(phoneNo).child("Delivery order").child(key).child("Food").push().setValue(hashMap)

                                            .addOnSuccessListener {
                                                Toast.makeText(requireActivity(), "Ordered", Toast.LENGTH_SHORT).show()
                                                FirebaseDatabase.getInstance().getReference("User").child(userPhoneNo)
                                                    .child("cart").child(phoneNo).removeValue()
                                                    .addOnSuccessListener {
                                                        activity?.onBackPressed()

                                                    }
                                            }
                                    }
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                            }
                        })
                }
            }
        }

        return view
    }
}