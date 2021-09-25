package com.example.seller.ui.slideshow;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.seller.Adapter.MenuAdapter;
import com.example.seller.Models.MenuModel;
import com.example.seller.R;
import com.example.seller.SessionManager;
import com.example.seller.databinding.FragmentMenuBinding;
import com.example.seller.databinding.FragmentSlideshowBinding;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;


public class MenuFragment extends Fragment {

    FragmentMenuBinding binding;
    MenuAdapter adapter;
    String key;
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMenuBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        RecyclerView recyclerView = binding.menuRecyclerView;
        FloatingActionButton floatingActionButton = binding.addMenu;

        if (getArguments() != null) {
            MenuFragmentArgs args = MenuFragmentArgs.fromBundle(getArguments());
            key = args.getKey();
        }

        SessionManager sessionManager = new SessionManager(requireActivity());
        String phone = sessionManager.getUserDetailFromSession().get(SessionManager.KEY_PHONE_NO);
        FirebaseRecyclerOptions<MenuModel> options = new FirebaseRecyclerOptions.Builder<MenuModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference("Seller").child(phone).child("Menu").orderByChild("key").equalTo(key), MenuModel.class)
                .build();
        adapter = new MenuAdapter(options, key);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuFragmentDirections.ActionMenuFragmentToAddMenuFragment action =
                        MenuFragmentDirections.actionMenuFragmentToAddMenuFragment(key);
                Navigation.findNavController(v).navigate(action);
            }
        });

        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}