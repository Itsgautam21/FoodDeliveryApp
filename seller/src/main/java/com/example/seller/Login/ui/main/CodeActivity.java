package com.example.seller.Login.ui.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.example.seller.R;
import com.example.seller.databinding.ActivityCodeBinding;
import com.example.seller.databinding.ActivityLoginBinding;
import com.example.seller.databinding.FragmentCodeBinding;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public class CodeActivity extends AppCompatActivity {

    @NonNull ActivityCodeBinding binding;

    String name, email, password, phoneNo;
    String codeSentBySystem;

    PinView pinView;

    public CodeActivity() {
        binding = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCodeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        TextView showPhoneNo = binding.phoneNo;
        pinView = binding.pinView;
        Button verifyCodeButton = binding.verifyCode;

        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        phoneNo = getIntent().getStringExtra("phoneNo");
        Log.i("phone", phoneNo);

        sendVerificationCodeToUser(phoneNo);


        verifyCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = Objects.requireNonNull(pinView.getText()).toString();
                if (!code.isEmpty())
                    verifyCode(code);

            }
        });

    }

    private void sendVerificationCodeToUser(String _phoneNo) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                .setPhoneNumber(_phoneNo)                // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this)                          // Activity (for callback binding)
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
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };



    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Verification Completed", Toast.LENGTH_LONG).show();
                        storeNewUserData();
                        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                        intent.putExtra("phone", phoneNo);
                        intent.putExtra("name", name);
                        intent.putExtra("email", email);
                        intent.putExtra("password", password);
                        startActivity(intent);
                        finish();
                    } else {
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                            Toast.makeText(this, "Verification is Not Completed", Toast.LENGTH_SHORT).show();
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
        FirebaseDatabase.getInstance().getReference("Seller").child(phoneNo).setValue(hashMap);
        //FirebaseDatabase.getInstance().getReference("Seller").child(phoneNo).child("phone").setValue(phoneNo);
    }
}