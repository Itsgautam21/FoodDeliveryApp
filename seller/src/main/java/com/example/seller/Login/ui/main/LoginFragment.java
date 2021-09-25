package com.example.seller.Login.ui.main;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import com.example.seller.HomeActivity;
import com.example.seller.SessionManager;
import com.example.seller.databinding.FragmentLogin2Binding;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import org.jetbrains.annotations.NotNull;
import java.util.Objects;


public class LoginFragment extends Fragment {

    FragmentLogin2Binding binding;

    //private CountryCodePicker countryCodePicker;
    private TextInputLayout phoneNo, password;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentLogin2Binding.inflate(inflater, container, false);
        View view = binding.getRoot();

        phoneNo = binding.loginPhoneNo;
        password = binding.loginPassword;
        progressBar = binding.progressbar;
        Button button = binding.loginButton;
        progressBar.setVisibility(View.INVISIBLE);


        button.setOnClickListener(v -> {

            if (!valid_Phone() | !valid_Password())
                return;
            progressBar.setVisibility(View.VISIBLE);
            if (!isConnected())
                warningDialogue();
            final String _phoneNo = Objects.requireNonNull(phoneNo.getEditText()).getText().toString();
            final String _password = Objects.requireNonNull(password.getEditText()).getText().toString();
            final String completePhoneNo = "+" + "91" + _phoneNo;

            Query checkUser = FirebaseDatabase.getInstance().getReference("Seller").orderByKey().equalTo(completePhoneNo);
            checkUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String pass = snapshot.child(completePhoneNo).child("Information").child("password").getValue(String.class);
                        assert pass != null;
                        if (pass.equals(_password)) {
                            String fullName = snapshot.child(completePhoneNo).child("Information").child("name").getValue(String.class);
                            String email_id = snapshot.child(completePhoneNo).child("Information").child("email").getValue(String.class);
                            String Password = snapshot.child(completePhoneNo).child("Information").child("password").getValue(String.class);
                            String phone = snapshot.child(completePhoneNo).child("Information").child("phone").getValue(String.class);

                            String name = snapshot.child(completePhoneNo).child("Address").child("fullName").getValue(String.class);
                            String phoneN = snapshot.child(completePhoneNo).child("Address").child("phoneNo").getValue(String.class);
                            String pinCode = snapshot.child(completePhoneNo).child("Address").child("pinCode").getValue(String.class);
                            String state = snapshot.child(completePhoneNo).child("Address").child("state").getValue(String.class);
                            String city = snapshot.child(completePhoneNo).child("Address").child("city").getValue(String.class);
                            String houseNo = snapshot.child(completePhoneNo).child("Address").child("houseNo").getValue(String.class);
                            String roadName = snapshot.child(completePhoneNo).child("Address").child("roadName").getValue(String.class);


                            String shopName = snapshot.child(completePhoneNo).child("shopName").getValue(String.class);
                            String shopDescription = snapshot.child(completePhoneNo).child("shopDescription").getValue(String.class);

                            SessionManager sessionManager = new SessionManager(requireActivity());
                            sessionManager.createLoginSession(fullName, email_id, Password, phone);
                            sessionManager.createAddress(name, phoneN, pinCode, state, city, houseNo, roadName);
                            sessionManager.createShopInfo(shopName, shopDescription);

                            Intent intent = new Intent(requireActivity().getBaseContext(), HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            progressBar.setVisibility(View.GONE);

                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(requireActivity(), "Password does not match", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(requireActivity(), "Data does not exist", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(requireActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });



        return view;

    }
    private boolean valid_Phone() {
        String val = Objects.requireNonNull(phoneNo.getEditText()).getText().toString();
        if (val.isEmpty()) {
            phoneNo.setError("field cannot be Empty");
            return false;
        } else {
            phoneNo.setError(null);
            phoneNo.setErrorEnabled(false);
            return true;
        }
    }
    public boolean valid_Password() {
        String val = Objects.requireNonNull(password.getEditText()).getText().toString();
        if (val.isEmpty()) {
            password.setError("field cannot be Empty");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }
    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return (wifiInfo != null && wifiInfo.isConnected()) || (mobileInfo != null && mobileInfo.isConnected());
    }
    private void warningDialogue() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(requireActivity());
        alertDialog.setMessage("You are not currently connected to the Internet press 'Connect' for the Internet").setCancelable(false)
                .setPositiveButton("connect", (dialog, which) -> startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS)))
                .setNegativeButton("quit", (dialog, which) -> {
                }).show();
    }

}