package com.example.seller.ui.slideshow;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.seller.R;
import com.example.seller.SessionManager;
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

import static android.app.Activity.RESULT_OK;


public class AddCategeryFragment extends Fragment {

    Uri uri;
    Bitmap bitmap;
    ImageView imageView;
    Uri downloadImage;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_catogery, container, false);

        TextInputLayout categoryName = view.findViewById(R.id.categoryName);
        Button addCategory = view.findViewById(R.id.addCategoryButton);
        imageView = view.findViewById(R.id.categoryImage);

        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = categoryName.getEditText().getText().toString();
                SessionManager sessionManager = new SessionManager(requireActivity());
                String phone = sessionManager.getUserDetailFromSession().get(SessionManager.KEY_PHONE_NO);
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("categoryName", name);
                hashMap.put("categoryImage", downloadImage.toString());

                FirebaseDatabase.getInstance().getReference("Seller").child(phone).child("Category").push().setValue(hashMap);
                Toast.makeText(requireActivity(), "Category Added", Toast.LENGTH_SHORT).show();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withContext(requireActivity())
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
                        }).check();
            }
        });

        return view;
    }

    private void uploadImage() {

        ProgressDialog dialog = new ProgressDialog(requireActivity());
        dialog.setTitle("File Uploading...");
        dialog.show();
        String phoneNo = new SessionManager(requireActivity()).getUserDetailFromSession().get(SessionManager.KEY_PHONE_NO);
        StorageReference storage = FirebaseStorage.getInstance().getReference("SellerInfo").child(phoneNo).child("categoryImage").child("image");

        storage.putFile(uri)
                .addOnSuccessListener(taskSnapshot -> {
                    storage.getDownloadUrl().addOnSuccessListener(uri -> downloadImage = uri);
                    dialog.dismiss();
                    Toast.makeText(requireActivity(), "File uploaded", Toast.LENGTH_SHORT).show();
                })
                .addOnProgressListener(snapshot -> {
                    float percent = (100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                    dialog.setMessage("Uploaded : " + (int) percent + "%");
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            assert data != null;
            uri = data.getData();
            try {
                if (uri != null) {
                    InputStream inputStream = requireContext().getContentResolver().openInputStream(uri);
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    imageView.setImageBitmap(bitmap);
                    uploadImage();
                } else {
                    Toast.makeText(requireActivity(), "please select image" , Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(requireActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}