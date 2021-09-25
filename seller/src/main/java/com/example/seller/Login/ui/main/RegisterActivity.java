package com.example.seller.Login.ui.main;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.seller.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;

    ImageButton editImage;
    ImageView shopImage;
    Uri uri;
    Bitmap bitmap;
    String phoneNo;
    String name;
    String email;
    String password;
    Uri downloadImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        TextInputLayout nameView = binding.name;
        TextInputLayout description = binding.description;
        Button registerButton = binding.registerButton;
        editImage = binding.browseShopImage;
        shopImage = binding.shopImage;
        Button button = binding.uploadButton;

        phoneNo = getIntent().getStringExtra("phone");
        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");

        Log.i("value", name);

        registerButton.setOnClickListener(v -> {
            String shopName = Objects.requireNonNull(nameView.getEditText()).getText().toString();
            String decs = Objects.requireNonNull(description.getEditText()).getText().toString();
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("shopName", shopName);
            hashMap.put("shopDescription", decs);
            hashMap.put("shopImage", downloadImage.toString());
            hashMap.put("shopOpen", false);

            FirebaseDatabase.getInstance().getReference("Seller").child(phoneNo).setValue(hashMap);
            HashMap<String, Object> hashMap1 = new HashMap<>();
            hashMap1.put("name", this.name);
            hashMap1.put("email", email);
            hashMap1.put("password", password);
            hashMap1.put("phone", phoneNo);
            FirebaseDatabase.getInstance().getReference("Seller").child(phoneNo).child("Information").setValue(hashMap1)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Intent intent = new Intent(getApplicationContext(), AddressActivity.class);
                            intent.putExtra("phone", phoneNo);
                            intent.putExtra("profilePage", "set");
                            startActivity(intent);
                        }
                    });

        });

        editImage.setOnClickListener(v -> Dexter.withContext(getApplicationContext())
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent, "Please Select Image"), 1);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check());





    }

    private void uploadImage() {

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("File Uploading...");
        dialog.show();
        StorageReference storage = FirebaseStorage.getInstance().getReference("SellerInfo").child(phoneNo).child("SellerImage").child("image");

                storage.putFile(uri)
                .addOnSuccessListener(taskSnapshot -> {
                    storage.getDownloadUrl().addOnSuccessListener(uri -> downloadImage = uri);
                    dialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "File uploaded", Toast.LENGTH_SHORT).show();
                })
                .addOnProgressListener(snapshot -> {
                    float percent = (100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                    dialog.setMessage("Uploaded : " + (int) percent + "%");
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            assert data != null;
            uri = data.getData();
            try {
                if (uri != null) {
                    InputStream inputStream = getContentResolver().openInputStream(uri);
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    shopImage.setImageBitmap(bitmap);
                    uploadImage();
                }
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}