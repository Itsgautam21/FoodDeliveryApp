package com.example.loginApp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.loginApp.models.CartModel
import com.example.loginApp.R
import com.example.loginApp.SessionManager
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase

class CartAdapter(options: FirebaseRecyclerOptions<CartModel?>,
                  var phoneNo: String) : FirebaseRecyclerAdapter<CartModel, CartAdapter.ViewHolder>(options) {

    var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sample_cart, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: CartModel) {
        Glide.with(holder.orderImage.context).load(model.foodImage).into(holder.orderImage)
        holder.soldItemName.text = model.foodName
        holder.price.text = model.foodPrice
        holder.orderNumber.text = model.foodDescription
        holder.increment.text = model.foodCount
        val userPhoneNo : String? = SessionManager(context!!).userDetailFromSession[SessionManager.KEY_PHONE_NO]

        holder.deleteItemOnCart.setOnClickListener {
            FirebaseDatabase.getInstance().getReference("User").child(userPhoneNo!!).child("cart")
                .child(phoneNo).child(getRef(position).key!!).removeValue()
        }

        holder.plusButton.setOnClickListener {
            var text = holder.increment.text.toString().toInt()
            text += 1
            val hashMap = HashMap<String, Any>()
            hashMap["foodCount"] = text.toString()
            FirebaseDatabase.getInstance().getReference("User").child(userPhoneNo!!).child("cart")
                .child(phoneNo).child(getRef(position).key!!).updateChildren(hashMap)
        }
        holder.negativeButton.setOnClickListener {
            var text = holder.increment.text.toString().toInt()
            if (text > 1) {
                text -= 1
                val hashMap = HashMap<String, Any>()
                hashMap["foodCount"] = text.toString()
                FirebaseDatabase.getInstance().getReference("User").child(userPhoneNo!!).child("cart")
                    .child(phoneNo).child(getRef(position).key!!).updateChildren(hashMap)
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val orderImage: ImageView = itemView.findViewById(R.id.orderImage)
        val soldItemName: TextView = itemView.findViewById(R.id.orderName)
        val price: TextView = itemView.findViewById(R.id.cartPrice)
        val orderNumber: TextView = itemView.findViewById(R.id.orderNumber)
        val deleteItemOnCart: ImageButton = itemView.findViewById(R.id.deleteItemOnCart)
        val plusButton: Button = itemView.findViewById(R.id.plusButtonCart)
        val negativeButton: Button = itemView.findViewById(R.id.negativeButtonCart)
        val increment: TextView = itemView.findViewById(R.id.incrementCart)

    }
}