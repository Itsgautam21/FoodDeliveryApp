package com.example.loginApp.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loginApp.R
import com.example.loginApp.SessionManager
import com.example.loginApp.adapters.CartAdapter
import com.example.loginApp.models.CartModel
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CartFragment : Fragment() {

    var recyclerView: RecyclerView? = null

    private var shopName : String? = null
    private var shopAddress : String? = null
    private var shopImage : String? = null

    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)

        val changeAddress: Button = view.findViewById(R.id.cartChangeOrAddButton)
        val recyclerView: RecyclerView = view.findViewById(R.id.orderRecyclerView)
        val showPrice = view.findViewById<TextView>(R.id.cartShowPrice)
        val showDiscount = view.findViewById<TextView>(R.id.cartShowDiscount)
        val showDelivery = view.findViewById<TextView>(R.id.cartShowDelivery)
        val showTotal = view.findViewById<TextView>(R.id.cartShowTotal)
        val showBoldTotal = view.findViewById<TextView>(R.id.cartShowBoldTotal)
        val cartCheckOut = view.findViewById<Button>(R.id.cartCheckOut)
        val cartName = view.findViewById<TextView>(R.id.cartName)
        val cartAddress = view.findViewById<TextView>(R.id.cartAddress)
        val cartState = view.findViewById<TextView>(R.id.cartState)
        val cartPhone = view.findViewById<TextView>(R.id.cartPhoneNo)

        val args = CartFragmentArgs.fromBundle(requireArguments())
        val phoneNo = args.phone

        val discount = showDiscount.text.toString().toDouble()
        val deliveryFee = showDelivery.text.toString().toDouble()
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
            .orderByChild("selected").equalTo(true).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (snap in snapshot.children) {
                            cartName.text = snap.child("name").getValue(String::class.java)
                            cartPhone.text = snap.child("phoneNo").getValue(String::class.java)
                            val address = snap.child("houseNo").getValue(String::class.java) + ", " +
                                    snap.child("colony").getValue(String::class.java) + ", " +
                                    snap.child("city").getValue(String::class.java)
                            val state = snap.child("state").getValue(String::class.java) + " - " +
                                    snap.child("pinCode").getValue(String::class.java)
                            cartAddress.text = address
                            cartState.text = state
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {

                }

            })

        FirebaseDatabase.getInstance().getReference("User").child(userPhoneNo).child("cart")
            .child(phoneNo).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Double>()
                val item = mutableListOf<Int>()
                if (snapshot.exists())
                    for (snap in snapshot.children) {
                        snap.child("foodPrice").getValue(String::class.java)?.let { list.add(it.toDouble()) }
                        snap.child("foodCount").getValue(String::class.java)?.let { item.add(it.toInt()) }
                    }
                var s = 0.0
                for (i in 0 until list.size){
                    s += (list[i] * item[i])
                }
                showPrice.text = s.toString()
                showTotal.text = s.toString()
                showBoldTotal.text = s.toString()
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })

        val query = FirebaseDatabase.getInstance().reference.child("User").child(userPhoneNo).child("cart").child(phoneNo)
        val option = FirebaseRecyclerOptions.Builder<CartModel>().setQuery(query, CartModel::class.java).build()
        val cartAdapter = CartAdapter(option, phoneNo)
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.adapter = cartAdapter
        cartAdapter.startListening()

        changeAddress.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.showAddressFragment)
        }

        cartCheckOut.setOnClickListener {
            val action = CartFragmentDirections.actionCartFragmentToPaymentFragment(phoneNo, showBoldTotal.text.toString())
            Navigation.findNavController(it).navigate(action)
        }
        return view
    }
}