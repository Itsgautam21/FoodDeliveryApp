package com.example.seller.Login.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Parcel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.example.seller.R;
import com.example.seller.databinding.FragmentCodeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.OAuthCredential;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;


public class CodeFragment extends Fragment {


    FragmentCodeBinding binding;

    String name, email, password, phoneNo;
    String codeSentBySystem;

    PinView pinView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding =  FragmentCodeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        TextView showPhoneNo = binding.phoneNo;
        pinView = binding.pinView;
        Button verifyCodeButton = binding.verifyCode;


        if (getArguments() != null) {
            CodeFragmentArgs args = CodeFragmentArgs.fromBundle(getArguments());
            name = args.getName();
            email = args.getEmail();
            password = args.getPassword();
            phoneNo = args.getPhoneNo();
        }
        sendVerificationCodeToUser(phoneNo);


        verifyCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = Objects.requireNonNull(pinView.getText()).toString();
                if (!code.isEmpty())
                    verifyCode(code);
            }
        });


        return view;
    }

    private void sendVerificationCodeToUser(String _phoneNo) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                .setPhoneNumber(_phoneNo)                // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(requireActivity())                          // Activity (for callback binding)
                .setCallbacks(mCallbacks)                   // OnVerificationStateChangedCallbacks
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            codeSentBySystem = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                pinView.setText(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(requireActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };



    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener((Executor) requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(requireActivity(), "Verification Completed", Toast.LENGTH_LONG).show();
                        storeNewUserData();
                    } else {
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                            Toast.makeText(requireActivity(), "Verification is Not Completed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void verifyCode(String code) {
        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(codeSentBySystem, code);
        signInWithPhoneAuthCredential(phoneAuthCredential);
    }

    private void storeNewUserData() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("name", name);
        hashMap.put("email", email);
        hashMap.put("password", password);
        hashMap.put("phone", phoneNo);
        FirebaseDatabase.getInstance().getReference("Seller").child(phoneNo).child("Information").setValue(hashMap);
    }

}