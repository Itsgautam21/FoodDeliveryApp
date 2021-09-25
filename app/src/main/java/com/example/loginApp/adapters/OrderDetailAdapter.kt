package com.example.loginApp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.loginApp.models.OrderDetailModel
import com.example.loginApp.R
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class OrderDetailAdapter(options: FirebaseRecyclerOptions<OrderDetailModel?>) : FirebaseRecyclerAdapter<OrderDetailModel, OrderDetailAdapter.ViewHolder>(options) {

    var context : Context? = null
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val foodName : TextView = itemView.findViewById(R.id.orderDetailName)
        val foodPrice : TextView = itemView.findViewById(R.id.orderDetailPrice)
        val foodCount : TextView = itemView.findViewById(R.id.orderDetailCount)
        val totalPrice : TextView = itemView.findViewById(R.id.orderDetailTotal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sample_order_detail, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: OrderDetailModel) {
        val total = model.foodPrice.toDouble() * model.foodCount.toDouble()
        holder.foodName.text = model.foodName
        holder.foodCount.text = model.foodCount
        holder.foodPrice.text = model.foodPrice
        holder.totalPrice.text = total.toString()
    }
}