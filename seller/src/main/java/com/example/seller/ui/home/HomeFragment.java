package com.example.seller.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.seller.Adapter.OrderAdapter;
import com.example.seller.Models.OrderModal;
import com.example.seller.SessionManager;
import com.example.seller.databinding.FragmentHomeBinding;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class HomeFragment extends Fragment {

    OrderAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentHomeBinding binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        final RecyclerView recyclerView = binding.orderRecyclerView;

        String phoneNo = new SessionManager(requireActivity()).getUserDetailFromSession().get(SessionManager.KEY_PHONE_NO);
        assert phoneNo != null;
        FirebaseRecyclerOptions<OrderModal> options = new FirebaseRecyclerOptions.Builder<OrderModal>()
                .setQuery(FirebaseDatabase.getInstance().getReference("Seller").child(phoneNo).child("Delivery order"), OrderModal.class)
                .build();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        adapter = new OrderAdapter(options);
        recyclerView.setAdapter(adapter);

        return root;
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