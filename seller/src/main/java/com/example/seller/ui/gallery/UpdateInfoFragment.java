package com.example.seller.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.seller.SessionManager;
import com.example.seller.databinding.FragmentUpdateInfoBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Objects;


public class UpdateInfoFragment extends BottomSheetDialogFragment {


    FragmentUpdateInfoBinding binding;
    String phoneNo;
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentUpdateInfoBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        TextInputLayout shopNameView = binding.shopName;
        TextInputLayout shopDescriptionView = binding.shopDescription;
        TextInputLayout nameView = binding.name;
        TextInputLayout emailIdView = binding.emailId;
        Button profileUpdateButton = binding.profileUpdateButton;

        phoneNo = new SessionManager(requireActivity()).getUserDetailFromSession().get(SessionManager.KEY_PHONE_NO);


        FirebaseDatabase.getInstance().getReference("Seller").child(phoneNo)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if(snapshot.exists()) {
                            Objects.requireNonNull(shopNameView.getEditText()).setText(snapshot.child("shopName").getValue(String.class));
                            Objects.requireNonNull(shopDescriptionView.getEditText()).setText(snapshot.child("shopDescription").getValue(String.class));
                            Objects.requireNonNull(nameView.getEditText()).setText(snapshot.child("Information").child("name").getValue(String.class));
                            Objects.requireNonNull(emailIdView.getEditText()).setText(snapshot.child("Information").child("email").getValue(String.class));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                        Toast.makeText(requireActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        profileUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String shopName = Objects.requireNonNull(shopNameView.getEditText()).getText().toString();
                String shopDescription = Objects.requireNonNull(shopDescriptionView.getEditText()).getText().toString();
                String name = Objects.requireNonNull(nameView.getEditText()).getText().toString();
                String emailId = Objects.requireNonNull(emailIdView.getEditText()).getText().toString();

                HashMap<String, Object> hashMapShop = new HashMap<>();
                hashMapShop.put("shopName", shopName);
                hashMapShop.put("shopDescription", shopDescription);
                HashMap<String, Object> hashMapInfo = new HashMap<>();
                hashMapInfo.put("name", name);
                hashMapInfo.put("email", emailId);

                assert phoneNo != null;
                FirebaseDatabase.getInstance().getReference("Seller").child(phoneNo).updateChildren(hashMapShop);
                FirebaseDatabase.getInstance().getReference("Seller").child(phoneNo).child("Information").updateChildren(hashMapInfo);
                Toast.makeText(requireActivity(), "Updated", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}