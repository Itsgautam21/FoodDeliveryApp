package com.example.loginApp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.loginApp.models.ShopModel
import com.example.loginApp.R
import com.example.loginApp.ui.home.HomeFragmentDirections
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class ShopAdapter(options: FirebaseRecyclerOptions<ShopModel?>) :
    FirebaseRecyclerAdapter<ShopModel, ShopAdapter.ViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sample_shop, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: ShopModel) {
        Glide.with(holder.image.context).load(model.shopImage).into(holder.image)
        //holder.rating.text = model.shopRating.toString()
        holder.name.text = model.shopName
        holder.description.text = model.shopDescription
        if (!model.shopOpen) holder.shopOpen.setImageResource(R.drawable.close)


        holder.itemView.setOnClickListener { v ->
            if (model.shopOpen) {
                val phoneNo = getRef(position).key!!
                val action = HomeFragmentDirections.actionNavHomeToShopFragment(phoneNo)
                Navigation.findNavController(v).navigate(action)
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val image: ImageView = itemView.findViewById(R.id.shopImage)
        val shopOpen: ImageView = itemView.findViewById(R.id.isShopOpen)
        //val rating: TextView = itemView.findViewById(R.id.shopRating)
        val name: TextView = itemView.findViewById(R.id.shopName)
        val description: TextView = itemView.findViewById(R.id.shopDescription)

    }
}