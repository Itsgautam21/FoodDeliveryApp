package com.example.loginApp.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.loginApp.models.AddressModel
import com.example.loginApp.R
import com.example.loginApp.SessionManager
import com.example.loginApp.ui.slideshow.ShowAddressFragmentDirections
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase

class AddressAdapter(options: FirebaseRecyclerOptions<AddressModel?>, var index : Int) :
    FirebaseRecyclerAdapter<AddressModel, AddressAdapter.ViewHolder>(options) {

    lateinit var context: Context

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: AddressModel) {
        holder.name.text = model.name
        holder.address.text = model.houseNo + " , " + model.colony + " , " + model.city
        holder.state.text = model.state + " - " + model.pinCode
        holder.phone.text = model.phoneNo



        holder.itemView.setOnClickListener {
            index = position
            notifyDataSetChanged()
        }
        holder.delete.setOnClickListener {
            val phone = SessionManager(context).userDetailFromSession[SessionManager.KEY_PHONE_NO]
            FirebaseDatabase.getInstance().reference.child("User").child(phone!!)
                .child("My Address").child(getRef(position).key!!).removeValue()
        }
        holder.update.setOnClickListener {
            val action = ShowAddressFragmentDirections.actionShowAddressFragmentToAddAddressFragment2(getRef(position).key!!)
            Navigation.findNavController(it).navigate(action)
        }

            if (index == position) {
                holder.addLinearLayout.background = ContextCompat.getDrawable(context, R.drawable.boarder_black)
                val phone = SessionManager(context).userDetailFromSession[SessionManager.KEY_PHONE_NO]
                val hashMap = HashMap<String, Any>()
                hashMap["selected"] = true
                hashMap["index"] = position
                FirebaseDatabase.getInstance().reference.child("User").child(phone!!)
                    .child("My Address").child(getRef(position).key!!).updateChildren(hashMap)
            } else {
                holder.addLinearLayout.setBackgroundColor(Color.parseColor("#FFFFFF"))
                val phone =
                    SessionManager(context).userDetailFromSession[SessionManager.KEY_PHONE_NO]
                val hashMap = HashMap<String, Any>()
                hashMap["selected"] = false
                hashMap["index"] = position
                FirebaseDatabase.getInstance().reference.child("User").child(phone!!)
                    .child("My Address").child(getRef(position).key!!).updateChildren(hashMap)
            }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.smaple_address, parent, false)
        return ViewHolder(view)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name: TextView = itemView.findViewById(R.id.showAddressName)
        val phone: TextView = itemView.findViewById(R.id.showAddressPhoneNo)
        val address: TextView = itemView.findViewById(R.id.showAddressAddress)
        val state: TextView = itemView.findViewById(R.id.showAddressState)
        val cardView: CardView = itemView.findViewById(R.id.sampleAddressLayout)
        val delete: ImageButton = itemView.findViewById(R.id.deleteAddress)
        val update: ImageButton = itemView.findViewById(R.id.editAddress)
        val addLinearLayout : LinearLayout = itemView.findViewById(R.id.addressLinearLayout)

    }
}