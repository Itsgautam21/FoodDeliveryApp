package com.example.loginApp.ui.gallery

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.loginApp.models.OrderDetailModel
import com.example.loginApp.SessionManager
import com.example.loginApp.adapters.OrderDetailAdapter
import com.example.loginApp.databinding.FragmentOrderDetailBinding
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase

class OrderDetailFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentOrderDetailBinding.inflate(inflater, container, false)
        val view = binding.root

        val args = OrderDetailFragmentArgs.fromBundle(requireArguments())
        val key = args.key

        val userPhoneNo = SessionManager(requireActivity()).userDetailFromSession[SessionManager.KEY_PHONE_NO]
        val query = FirebaseDatabase.getInstance().getReference("User").child(userPhoneNo!!).child("My Order")
            .child(key).child("Food")
        val options = FirebaseRecyclerOptions.Builder<OrderDetailModel>().setQuery(query, OrderDetailModel::class.java).build()
        binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        val adapter = OrderDetailAdapter(options)
        binding.recyclerView.adapter = adapter
        adapter.startListening()
        return view
    }


}