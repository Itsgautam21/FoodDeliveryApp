package com.example.loginApp.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loginApp.adapters.OrderAdapter
import com.example.loginApp.models.OrderModel
import com.example.loginApp.R
import com.example.loginApp.SessionManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query

class GalleryFragment : Fragment() {

    private var adapter : OrderAdapter? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val root = inflater.inflate(R.layout.fragment_gallery, container, false)
        val recyclerView: RecyclerView = root.findViewById(R.id.orderRecyclerView)
        val sessionManager = SessionManager(requireActivity())
        val map = sessionManager.userDetailFromSession
        val phoneNo = map[SessionManager.KEY_PHONE_NO]!!
        if (!sessionManager.checkLogin()) Toast.makeText(requireContext(), "Login to see Orders", Toast.LENGTH_SHORT).show()
        else {
            val query : Query = FirebaseDatabase.getInstance().getReference("User").child(phoneNo)
                .child("My Order").orderByChild("orderDate")

            val options = FirebaseRecyclerOptions.Builder<OrderModel>()
                .setQuery(query, OrderModel::class.java)
                .build()

            adapter = OrderAdapter(options)
            recyclerView.layoutManager = LinearLayoutManager(requireActivity())
            recyclerView.adapter = adapter
        }
        return root
    }

    override fun onStart() {
        super.onStart()
        adapter!!.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter!!.stopListening()
    }
}