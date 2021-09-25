package com.example.seller.Login.ui.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.seller.R;
import com.example.seller.databinding.FragmentSignUpBinding;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;


public class SignUpFragment extends Fragment {

    FragmentSignUpBinding binding;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSignUpBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        TextInputLayout nameView = binding.name;
        TextInputLayout emailView = binding.email;
        TextInputLayout passwordView = binding.password;
        TextInputLayout phoneNoView = binding.phoneNo;
        Button signUpButton = binding.signUpButton;


        signUpButton.setOnClickListener(v -> {

            String name = Objects.requireNonNull(nameView.getEditText()).getText().toString();
            String email = Objects.requireNonNull(emailView.getEditText()).getText().toString();
            String password = Objects.requireNonNull(passwordView.getEditText()).getText().toString();
            String phoneNo = "+91" + Objects.requireNonNull(phoneNoView.getEditText()).getText().toString();
            Log.i("hhhh", phoneNo + name);
            Intent intent = new Intent(requireActivity(), CodeActivity.class);
            intent.putExtra("name", name);
            intent.putExtra("email", email);
            intent.putExtra("password", password);
            intent.putExtra("phoneNo", phoneNo);
            startActivity(intent);
        });


        return view;
    }
}