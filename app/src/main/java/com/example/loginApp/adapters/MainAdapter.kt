package com.example.loginApp.adapters

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.loginApp.models.MainModel
import com.example.loginApp.R
import com.example.loginApp.SessionManager
import com.example.loginApp.ui.home.ShopFragmentDirections
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*

class MainAdapter(options: FirebaseRecyclerOptions<MainModel?>, var phoneNo: String) :
    FirebaseRecyclerAdapter<MainModel, MainAdapter.ViewHolder>(options) {

    var context: Context? = null
    var options: FirebaseRecyclerOptions<MainModel>? = null

    private val remove : String = "Remove"
    private val add : String = "Add"
    private val outOfStock : String = "out of Stock"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.sample_main_food, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: MainModel) {
        val userPhoneNo = SessionManager(context!!).userDetailFromSession[SessionManager.KEY_PHONE_NO]
        Glide.with(holder.foodImage.context).load(model.foodImage).into(holder.foodImage)
        holder.name.text = model.foodName
        holder.price.text = model.foodPrice
        holder.description.text = model.foodDescription
        if (model.available) holder.isAvailable.text = ""
        else holder.isAvailable.text = outOfStock
        FirebaseDatabase.getInstance().getReference("User").child(userPhoneNo!!).child("cart")
            .child(phoneNo)
            .orderByKey().equalTo(getRef(position).key).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        holder.addToCart.text = remove
                        holder.addToCart.setBackgroundColor(Color.parseColor("#FF5C5C"))
                    } else {
                        holder.addToCart.text = add
                        holder.addToCart.setBackgroundColor(Color.parseColor("#ffb300"))
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        if (model.available) {
            holder.itemView.setOnClickListener { v ->
                val action = ShopFragmentDirections.actionShopFragmentToDetailShowFragment(getRef(position).key!!, phoneNo)
                Navigation.findNavController(v).navigate(action)
            }
        }


        holder.addToCart.setOnClickListener {
            if (model.available) {
                if (holder.addToCart.text == remove) {
                    Log.i("value", holder.addToCart.text.toString())
                    FirebaseDatabase.getInstance().getReference("User").child(userPhoneNo).child("cart")
                        .child(phoneNo)
                        .child(getRef(position).key!!).removeValue()
                    Toast.makeText(context, "Item Added to cart", Toast.LENGTH_SHORT).show()
                }
                else {
                    Log.i("value", holder.addToCart.text.toString())
                    val hashMap = HashMap<String, Any>()
                    val hashMap1 = HashMap<String, Any>()

                    val main = getItem(position)
                    hashMap1[getRef(position).key!!] = main

                    hashMap["MenuId"] = getRef(position).key!!
                    hashMap["foodImage"] = main.foodImage!!
                    hashMap["foodName"] = main.foodName!!
                    hashMap["foodDescription"] = main.foodDescription!!
                    hashMap["foodPrice"] = main.foodPrice!!
                    hashMap["foodCount"] = "1"

                    /*FirebaseDatabase.getInstance().getReference("User").child(userPhoneNo).child("cart")
                        .addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.exists()) {
                                    for (snap in snapshot.children) {
                                        if (phoneNo != snap.key)
                                            FirebaseDatabase.getInstance().getReference("User").child(userPhoneNo).child("cart")
                                                .child(snap.key!!).removeValue()
                                    }

                                }
                            }

                            override fun onCancelled(error: DatabaseError) {

                            }

                        })*/
                    FirebaseDatabase.getInstance().getReference("User")
                        .child(userPhoneNo).child("cart")
                        .child(phoneNo).child(getRef(position).key!!)
                        .setValue(hashMap)

                    Toast.makeText(context, "Item Added to cart", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val foodImage: ImageView = itemView.findViewById(R.id.foodImage)
        val name: TextView = itemView.findViewById(R.id.foodName)
        val price: TextView = itemView.findViewById(R.id.foodPrice)
        val description: TextView = itemView.findViewById(R.id.foodDescription)
        val isAvailable: TextView = itemView.findViewById(R.id.isAvailable)
        val addToCart: Button = itemView.findViewById(R.id.addToCartButton)
    }
}