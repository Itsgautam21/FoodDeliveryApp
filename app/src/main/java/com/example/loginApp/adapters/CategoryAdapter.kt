package com.example.loginApp.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.loginApp.models.CategoryModel
import com.example.loginApp.models.MainModel
import com.example.loginApp.R
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase

class CategoryAdapter(options: FirebaseRecyclerOptions<CategoryModel?>, val recyclerView: RecyclerView,
                      private var phoneNo: String, private var allButton : LinearLayout,
                      private var index : Int) : FirebaseRecyclerAdapter<CategoryModel, CategoryAdapter.ViewHolder>(options) {

    private var context: Context? = null

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: CategoryModel) {
        Glide.with(holder.categoryImage).load(model.categoryImage).into(holder.categoryImage)
        holder.categoryName.text = model.categoryName

        holder.itemView.setOnClickListener {
            index = position
            notifyDataSetChanged()
            allButton.setBackgroundColor(Color.parseColor("#FFFFFF"))

            val mainOptions = FirebaseRecyclerOptions.Builder<MainModel>().setQuery(
                    FirebaseDatabase.getInstance().reference.child("Seller").child(phoneNo).child("Menu")
                        .orderByChild("key").equalTo(getRef(position).key), MainModel::class.java).build()
            recyclerView.layoutManager = LinearLayoutManager(context)
            val mainAdapter = MainAdapter(mainOptions, phoneNo)
            recyclerView.adapter = mainAdapter
            mainAdapter.startListening()
        }
        if (index == position) {
            holder.linearLayout.background = ContextCompat.getDrawable(context!!, R.drawable.boarder_black)
        } else {
            holder.linearLayout.background = ContextCompat.getDrawable(context!!, R.drawable.background_rv)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sample_category, parent, false)
        return ViewHolder(view)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var categoryImage : ImageView = itemView.findViewById(R.id.categoryImage)
        var categoryName : TextView = itemView.findViewById(R.id.categoryName)
        val linearLayout : LinearLayout = itemView.findViewById(R.id.linearCategory)
    }
}