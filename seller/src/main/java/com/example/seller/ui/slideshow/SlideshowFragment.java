package com.example.seller.ui.slideshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.seller.Adapter.CategoryAdapter;
import com.example.seller.Models.CategoryModel;
import com.example.seller.R;
import com.example.seller.SessionManager;
import com.example.seller.databinding.FragmentSlideshowBinding;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;

public class SlideshowFragment extends Fragment {

    private FragmentSlideshowBinding binding;
    CategoryAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        RecyclerView recyclerView = binding.catogeryRecyclerView;
        FloatingActionButton floatingActionButton = binding.addCategory;

        floatingActionButton.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.addCatogoryFragment));
        SessionManager sessionManager = new SessionManager(requireActivity());
        String phone = sessionManager.getUserDetailFromSession().get(SessionManager.KEY_PHONE_NO);
        FirebaseRecyclerOptions<CategoryModel> options = new FirebaseRecyclerOptions.Builder<CategoryModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference("Seller").child(phone).child("Category"), CategoryModel.class)
                .build();

        adapter = new CategoryAdapter(options);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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