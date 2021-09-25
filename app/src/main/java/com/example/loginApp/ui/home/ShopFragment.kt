package com.example.loginApp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loginApp.models.CategoryModel
import com.example.loginApp.models.MainModel
import com.example.loginApp.R
import com.example.loginApp.SessionManager
import com.example.loginApp.adapters.CategoryAdapter
import com.example.loginApp.adapters.MainAdapter
import com.example.loginApp.databinding.FragmentShopBinding
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.*

class ShopFragment : Fragment() {

    lateinit var cart : CardView
    private lateinit var itemCount : TextView
    private lateinit var totalPrice : TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentShopBinding.inflate(inflater, container, false)
        val view = binding.root

        val recyclerView: RecyclerView = binding.recyclerView
        val categoryRecyclerView: RecyclerView = binding.categoryRecyclerView
        val allButton : LinearLayout = binding.allButton
        val shopName : TextView = binding.shopName
        val shopDescription : TextView = binding.shopDescription
        val shopAddress : TextView = binding.shopAddress
        cart = binding.cart
        itemCount = binding.countItem
        totalPrice = binding.totalPrice

        // get Arguments from Home Fragment
        val args = ShopFragmentArgs.fromBundle(requireArguments())
        val phoneNo = args.phoneNo
        val userPhoneNo = SessionManager(requireActivity()).userDetailFromSession[SessionManager.KEY_PHONE_NO]

        FirebaseDatabase.getInstance().getReference("Seller").child(phoneNo)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        shopName.text = snapshot.child("shopName").getValue(String::class.java)
                        shopDescription.text = snapshot.child("shopDescription").getValue(String::class.java)
                        shopAddress.text = snapshot.child("Address").child("roadName").getValue(String::class.java)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        // set recycler view for category of Menu data
        val options = FirebaseRecyclerOptions.Builder<CategoryModel>()
            .setQuery(FirebaseDatabase.getInstance().reference.child("Seller").child(phoneNo)
                .child("Category"), CategoryModel::class.java).build()
        categoryRecyclerView.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        var adapter = CategoryAdapter(options, recyclerView, phoneNo, allButton, -1)
        categoryRecyclerView.adapter = adapter
        adapter.startListening()

        // set recycler view for Menu data
        val option = FirebaseRecyclerOptions.Builder<MainModel>()
            .setQuery(FirebaseDatabase.getInstance().reference.child("Seller").child(phoneNo)
                    .child("Menu"), MainModel::class.java).build()
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        var mainAdapter = MainAdapter(option, phoneNo)
        recyclerView.adapter = mainAdapter
        mainAdapter.startListening()

        // to view all Menu
        allButton.setOnClickListener {
            allButton.background = ContextCompat.getDrawable(requireActivity(), R.drawable.boarder_black)
            adapter = CategoryAdapter(options, recyclerView, phoneNo, allButton, -1)
            categoryRecyclerView.adapter = adapter
            adapter.startListening()
            val option1 = FirebaseRecyclerOptions.Builder<MainModel>()
                .setQuery(FirebaseDatabase.getInstance().reference.child("Seller").child(phoneNo)
                        .child("Menu"), MainModel::class.java).build()
            recyclerView.layoutManager = LinearLayoutManager(requireActivity())
            mainAdapter = MainAdapter(option1, phoneNo)
            recyclerView.adapter = mainAdapter
            mainAdapter.startListening()
        }

        cart.setOnClickListener { v ->
            val action = ShopFragmentDirections.actionShopFragmentToCartFragment2(phoneNo)
            Navigation.findNavController(v).navigate(action)
        }

        val query : Query = FirebaseDatabase.getInstance().getReference("User").child(userPhoneNo!!).child("cart")
            .child(phoneNo)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Double>()
                val item = mutableListOf<Int>()
                var count = 0
                if (snapshot.exists())
                    for (snap in snapshot.children) {
                        snap.child("foodPrice").getValue(String::class.java)?.let { list.add(it.toDouble()) }
                        snap.child("foodCount").getValue(String::class.java)?.let { item.add(it.toInt()) }
                        count++
                    }
                var s = 0.0
                for (i in 0 until list.size){
                    s += (list[i] * item[i])
                }
                if (count != 0) cart.visibility = View.VISIBLE
                else cart.visibility = View.GONE
                itemCount.text = count.toString()
                totalPrice.text = s.toString()
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })


        return view
    }
}


