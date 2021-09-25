package com.example.loginApp.ui.slideshow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loginApp.models.AddressModel
import com.example.loginApp.R
import com.example.loginApp.SessionManager
import com.example.loginApp.adapters.AddressAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ShowAddressFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_show_address, container, false)
        val addAddressProfile : Button = view.findViewById(R.id.addAddressProfile)
        val recyclerView : RecyclerView = view.findViewById(R.id.addressRecyclerView)
        val sessionManager = SessionManager(requireActivity())
        val map = sessionManager.userDetailFromSession
        val phoneNo = map[SessionManager.KEY_PHONE_NO]!!

        FirebaseDatabase.getInstance().getReference("User").child(phoneNo).child("My Address")
            .orderByChild("selected").equalTo(true).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var pos : Int? = 0
                    if (snapshot.exists()) {
                        for (sna in snapshot.children) {
                            pos = sna.child("index").getValue(Int::class.java)
                        }
                    }
                    val options = FirebaseRecyclerOptions.Builder<AddressModel>()
                        .setQuery(FirebaseDatabase.getInstance().reference.child("User").child(phoneNo)
                            .child("My Address"), AddressModel::class.java).build()
                    recyclerView.layoutManager = LinearLayoutManager(activity)
                    val adapter = AddressAdapter(options, pos!!)
                    recyclerView.adapter = adapter
                    adapter.startListening()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        addAddressProfile.setOnClickListener {
            val action = ShowAddressFragmentDirections.actionShowAddressFragmentToAddAddressFragment2("Add")
            Navigation.findNavController(view).navigate(action)
        }
        return view
    }

}