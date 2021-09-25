package com.example.loginApp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.loginApp.R
import com.example.loginApp.models.PaymentModel

class PaymentAdapter(private var list : ArrayList<PaymentModel>) : RecyclerView.Adapter<PaymentAdapter.ViewHolder>() {

    var context : Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view  = LayoutInflater.from(parent.context).inflate(R.layout.sample_payment, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]
        holder.name.text = model.name
        holder.checkBox.isChecked = list.size - 1 == position
    }
    override fun getItemCount(): Int = list.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name : TextView = itemView.findViewById(R.id.paymentName)
        val checkBox : CheckBox = itemView.findViewById(R.id.paymentCheckBox)
    }
}