package com.example.loginApp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loginApp.models.ShopModel
import com.example.loginApp.R
import com.example.loginApp.adapters.ShopAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query

class HomeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val recyclerView: RecyclerView = view.findViewById(R.id.shopRecyclerView)
        val query : Query = FirebaseDatabase.getInstance().getReference("Seller")
        val options = FirebaseRecyclerOptions.Builder<ShopModel>()
            .setQuery(query, ShopModel::class.java).build()
        val adapter = ShopAdapter(options)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter.startListening()
        return view
    }
}