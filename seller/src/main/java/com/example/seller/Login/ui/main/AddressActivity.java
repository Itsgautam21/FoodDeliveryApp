package com.example.seller.Login.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.seller.HomeActivity;
import com.example.seller.databinding.ActivityAddressBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Objects;

public class AddressActivity extends AppCompatActivity {

    ActivityAddressBinding binding;
    String phone;
    String profilePage = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        TextInputLayout fullNameInput = binding.fullNameADD;
        TextInputLayout phoneNoInput = binding.PhoneNoAdd;
        TextInputLayout pinCodeInput = binding.pinCodeAdd;
        TextInputLayout stateInput = binding.stateAdd;
        TextInputLayout cityInput = binding.cityADD;
        TextInputLayout houseNoInput = binding.houseNoAdd;
        TextInputLayout roadNameInput = binding.roadNameADD;
        Button saveAddress = binding.saveAddress;
        phone = getIntent().getStringExtra("phone");
        profilePage = getIntent().getStringExtra("profilePage");

        if (profilePage.equals("update")) {

            FirebaseDatabase.getInstance().getReference("Seller").child(phone).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Objects.requireNonNull(fullNameInput.getEditText()).setText(snapshot.child("Address").child("fullName").getValue(String.class));
                        Objects.requireNonNull(phoneNoInput.getEditText()).setText(snapshot.child("Address").child("phoneNo").getValue(String.class));
                        Objects.requireNonNull(pinCodeInput.getEditText()).setText(snapshot.child("Address").child("pinCode").getValue(String.class));
                        Objects.requireNonNull(stateInput.getEditText()).setText(snapshot.child("Address").child("state").getValue(String.class));
                        Objects.requireNonNull(cityInput.getEditText()).setText(snapshot.child("Address").child("city").getValue(String.class));
                        Objects.requireNonNull(houseNoInput.getEditText()).setText(snapshot.child("Address").child("houseNo").getValue(String.class));
                        Objects.requireNonNull(roadNameInput.getEditText()).setText(snapshot.child("Address").child("roadName").getValue(String.class));
                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {
                    Toast.makeText(AddressActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        saveAddress.setOnClickListener(v -> {
            String fullName = Objects.requireNonNull(fullNameInput.getEditText()).getText().toString();
            String phoneNo = Objects.requireNonNull(phoneNoInput.getEditText()).getText().toString();
            String pinCode = Objects.requireNonNull(pinCodeInput.getEditText()).getText().toString();
            String state = Objects.requireNonNull(stateInput.getEditText()).getText().toString();
            String city = Objects.requireNonNull(cityInput.getEditText()).getText().toString();
            String houseNo = Objects.requireNonNull(houseNoInput.getEditText()).getText().toString();
            String roadName = Objects.requireNonNull(roadNameInput.getEditText()).getText().toString();

            HashMap<String, Object> hashMap = new HashMap<>();

            hashMap.put("fullName", fullName);
            hashMap.put("phoneNo", phoneNo);
            hashMap.put("pinCode", pinCode);
            hashMap.put("state", state);
            hashMap.put("city", city);
            hashMap.put("houseNo", houseNo);
            hashMap.put("roadName", roadName);

            if (profilePage.equals("set")) {
                FirebaseDatabase.getInstance().getReference("Seller").child(phone).child("Address").setValue(hashMap).addOnSuccessListener(unused -> {
                    Toast.makeText(AddressActivity.this, "Account Created", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                });
            } else {
                FirebaseDatabase.getInstance().getReference("Seller").child(phone).child("Address").updateChildren(hashMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(AddressActivity.this, "Address Updated", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
            }


        });
    }
}