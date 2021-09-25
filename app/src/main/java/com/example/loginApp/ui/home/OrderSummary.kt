package com.example.loginApp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.loginApp.R
import com.example.loginApp.SessionManager
import com.example.loginApp.databinding.FragmentOrderSummaryBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class OrderSummary : Fragment() {

    var setImage: String? = null
    var setName: String? = null
    var setDescription: String? = null
    var setPrice: String? = null

    var mainPrice: String? = null
    var mainDiscount: String? = null
    var mainDeliveryFee: String? = null

    var key: String? = null
    var phoneNo: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_order_summary, container, false)
        val orderSummaryName : TextView = view.findViewById(R.id.orderSummaryName)
        val orderSummaryAddress : TextView= view.findViewById(R.id.orderSummaryAddress)
        val orderSummaryState : TextView= view.findViewById(R.id.orderSummaryState)
        val orderSummaryPhone : TextView= view.findViewById(R.id.orderSummaryPhone)
        val changeAddress : Button = view.findViewById(R.id.orderDetailChangeAddress)
        val plusButton : Button = view.findViewById(R.id.plusButton)
        val negativeButton : Button = view.findViewById(R.id.negativeButton)
        val increment : TextView = view.findViewById(R.id.increment)
        val price : TextView = view.findViewById(R.id.orderDetailPrice)
        val showPrice : TextView = view.findViewById(R.id.showPrice)
        val showDiscount : TextView = view.findViewById(R.id.showDiscount)
        val showDeliveryFee : TextView = view.findViewById(R.id.showDeliveryFee)
        val showTotal : TextView = view.findViewById(R.id.showTotal)
        val showTotalBold : TextView = view.findViewById(R.id.showTotalBold)
        val showName : TextView = view.findViewById(R.id.orderDetailName)
        val showDescription : TextView = view.findViewById(R.id.orderDetailDescription)
        val showImage : ImageView = view.findViewById(R.id.orderDetailImage)
        val checkOut : Button= view.findViewById(R.id.checkOut)
        val sessionManager = SessionManager(requireActivity())
        val hashMap = sessionManager.userDetailFromSession

        orderSummaryName.text = hashMap[SessionManager.KEY_ADDRESS_NAME]
        orderSummaryAddress.text = hashMap[SessionManager.KEY_ADDRESS]
        orderSummaryState.text = hashMap[SessionManager.KEY_STATE]
        orderSummaryPhone.text = hashMap[SessionManager.KEY_ADDRESS_PHONE]

        val args = OrderSummaryArgs.fromBundle(requireArguments())
        key = args.key
        phoneNo = args.phoneNo

        FirebaseDatabase.getInstance().getReference("Seller").child(phoneNo!!)
            .child("Menu").child(key!!).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    setImage = snapshot.child("foodImage").getValue(String::class.java)
                    setName = snapshot.child("foodName").getValue(String::class.java)
                    setDescription = snapshot.child("foodDescription").getValue(String::class.java)
                    setPrice = snapshot.child("foodPrice").getValue(String::class.java)
                    Glide.with(showImage.context).load(setImage).into(showImage)
                    showName.text = setName
                    showDescription.text = setDescription
                    price.text = setPrice
                    showPrice.text = setPrice
                    mainDiscount = showDiscount.getText().toString()
                    mainDeliveryFee = showDeliveryFee.text.toString()
                    showPrice.text = setPrice
                    val Total =
                        setPrice!!.toDouble() + mainDiscount!!.toDouble() + mainDeliveryFee!!.toDouble()
                    showTotal.setText(Total.toString())
                    showTotalBold.text = Total.toString()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireActivity(), error.message, Toast.LENGTH_SHORT).show()
            }
        })
        changeAddress.setOnClickListener(View.OnClickListener { v ->
            Navigation.findNavController(v).navigate(R.id.showAddressFragment)
        })
        plusButton.setOnClickListener(View.OnClickListener {
            var text = increment.getText().toString().toInt()
            text += 1
            increment.setText(text.toString())
            val p = text * setPrice!!.toDouble()
            showPrice.setText(p.toString())
            val Total =
                showPrice.getText().toString().toDouble() + showDiscount.getText().toString()
                    .toDouble() + showDeliveryFee.getText().toString().toDouble()
            showTotal.setText(Total.toString())
            showTotalBold.setText(Total.toString())
        })
        negativeButton.setOnClickListener(View.OnClickListener {
            var text = increment.getText().toString().toInt()
            if (text > 1) {
                text -= 1
                increment.setText(text.toString())
                val q = showPrice.getText().toString().toDouble()
                val p = q - setPrice!!.toDouble()
                showPrice.setText(p.toString())
                val Total =
                    showPrice.getText().toString().toDouble() + showDiscount.getText().toString()
                        .toDouble() + showDeliveryFee.getText().toString().toDouble()
                showTotal.setText(Total.toString())
                showTotalBold.setText(Total.toString())
            }
        })
        /*checkOut.setOnClickListener {
            val sessionManager1 = SessionManager(requireActivity())
            val map = sessionManager1.userDetailFromSession
            val userPhoneNo = map[SessionManager.KEY_PHONE_NO]!!
            val seller = HashMap<String, Any?>()
            seller["foodImage"] = setImage
            seller["foodName"] = setName
            seller["foodDescription"] = setDescription
            seller["foodPrice"] = showTotalBold.getText().toString()
            seller["buyerName"] = orderSummaryName.text.toString()
            seller["buyerAddress"] = orderSummaryAddress.getText().toString()
            seller["buyerState"] = orderSummaryState.text.toString()
            seller["buyerPhoneNo"] = orderSummaryPhone.text.toString()
            val hashMap1 = HashMap<String, Any?>()
            hashMap1["orderImage"] = setImage
            hashMap1["orderName"] = setName
            hashMap1["description"] = setDescription
            hashMap1["orderDate"] = "12 / 04 / 2021"
            FirebaseDatabase.getInstance().getReference("User").child(userPhoneNo).child("My Order")
                .push().setValue(hashMap1)
            FirebaseDatabase.getInstance().getReference("Seller").child(phoneNo!!)
                .child("Delivery order").push().setValue(seller)
            Toast.makeText(requireActivity(), "Ordered", Toast.LENGTH_SHORT).show()
        }*/
        return view
    }
}