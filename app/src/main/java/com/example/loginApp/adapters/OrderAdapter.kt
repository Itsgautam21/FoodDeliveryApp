package com.example.loginApp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.loginApp.models.OrderModel
import com.example.loginApp.R
import com.example.loginApp.ui.gallery.GalleryFragmentDirections
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class OrderAdapter(options: FirebaseRecyclerOptions<OrderModel?>) : FirebaseRecyclerAdapter<OrderModel, OrderAdapter.ViewHolder>(options) {

    var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sample_order, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: OrderModel) {
        Glide.with(holder.orderShopImage.context).load(model.shopImage).into(holder.orderShopImage)
        holder.orderShopName.text = model.shopName
        holder.orderShopAddress.text = model.shopAddress
        holder.date.text = model.orderDate
        holder.orderPrice.text = model.totalPrice

        holder.itemView.setOnClickListener {
            val action = GalleryFragmentDirections.actionNavGalleryToOrderDetailFragment(getRef(position).key!!)
            Navigation.findNavController(it).navigate(action)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var orderShopImage : ImageView = itemView.findViewById(R.id.orderShopImage)
        var orderShopName : TextView = itemView.findViewById(R.id.orderShopName)
        var orderShopAddress : TextView = itemView.findViewById(R.id.orderShopAddress)
        var date : TextView = itemView.findViewById(R.id.orderDate)
        var orderPrice : TextView = itemView.findViewById(R.id.orderPrice)
    }
}