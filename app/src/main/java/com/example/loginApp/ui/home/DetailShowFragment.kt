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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DetailShowFragment : Fragment() {

    private var key: String = ""
    private var phoneNO: String = ""
    private var isAddToCart : Boolean = false
    private val added = "Added"
    private val addCart = "Add To Cart"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_detail_show, container, false)
        val detailName: TextView = view.findViewById(R.id.detailName)
        val detailDescription: TextView = view.findViewById(R.id.detailDescription)
        val detailPrice: TextView = view.findViewById(R.id.detailPrice)
        val detailImageView: ImageView = view.findViewById(R.id.detailImage)
        val button: Button = view.findViewById(R.id.orderNow)
        val addToCart: Button = view.findViewById(R.id.addToCart)

        val args = DetailShowFragmentArgs.fromBundle(requireArguments())
        key = args.key
        phoneNO = args.phoneNo

        FirebaseDatabase.getInstance().getReference("Seller").child(phoneNO).child("Menu").child(key)
            .addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val mainImage = snapshot.child("foodImage").getValue(String::class.java)
                    Glide.with(detailImageView.context).load(mainImage).into(detailImageView)
                    detailPrice.text = snapshot.child("foodPrice").getValue(String::class.java)
                    detailName.text = snapshot.child("foodName").getValue(String::class.java)
                    detailDescription.text = snapshot.child("foodDescription").getValue(String::class.java)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireActivity(), error.message, Toast.LENGTH_SHORT).show()
            }
        })
        button.setOnClickListener { v: View? ->
            val action = DetailShowFragmentDirections.actionDetailShowFragmentToOrderSummary(key, phoneNO)
            Navigation.findNavController(v!!).navigate(action)
        }
        return view
    }
}