package com.example.seller.ui.gallery;

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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.seller.Login.ui.main.AddressActivity;
import com.example.seller.Login.ui.main.LoginActivity;
import com.example.seller.R;
import com.example.seller.SessionManager;
import com.example.seller.databinding.FragmentGalleryBinding;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;
    ImageView shopImage;
    Uri uri;
    Bitmap bitmap;
    String phoneNo;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        shopImage = binding.shopImage;
        ImageButton editShopImage = binding.editShopImage;
        TextView shopNameView = binding.shopName;
        TextView shopDescriptionView = binding.shopDescription;
        TextView profileName = binding.profileName;
        TextView profileEmail = binding.profileEmail;
        TextView profilePhoneNo = binding.profilePhoneNo;
        TextView addressView = binding.addressTextView;
        TextView stateView = binding.stateTextView;
        Button updateInfo = binding.profileUpdateButton;
        Button changeAddress = binding.profileChangeOrAddButton;
        Button viewMenu = binding.viewMenu;

        CardView logoutFromApp = binding.logoutApp;



        SessionManager sessionManager = new SessionManager(requireActivity());
        phoneNo = sessionManager.getUserDetailFromSession().get(SessionManager.KEY_PHONE_NO);
        assert phoneNo != null;
        Query query = FirebaseDatabase.getInstance().getReference("Seller").child(phoneNo);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String image = snapshot.child("shopImage").getValue(String.class);
                    if (image != null) Glide.with(shopImage.getContext()).load(image).into(shopImage);

                    shopNameView.setText(snapshot.child("shopName").getValue(String.class));
                    shopDescriptionView.setText(snapshot.child("shopDescription").getValue(String.class));
                    profileName.setText(snapshot.child("Information").child("name").getValue(String.class));
                    profileEmail.setText(snapshot.child("Information").child("email").getValue(String.class));
                    profilePhoneNo.setText(snapshot.child("Information").child("phone").getValue(String.class));
                    String address = snapshot.child("Address").child("houseNo").getValue(String.class) + ", " +
                            snapshot.child("Address").child("roadName").getValue(String.class) + ", " +
                            snapshot.child("Address").child("city").getValue(String.class);
                    String state = snapshot.child("Address").child("state").getValue(String.class) + " - " +
                            snapshot.child("Address").child("pinCode").getValue(String.class);
                    addressView.setText(address);
                    stateView.setText(state);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(requireActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        editShopImage.setOnClickListener(new View.OnClickListener() {
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


        updateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateInfoFragment updateInfoFragment = new UpdateInfoFragment();
                updateInfoFragment.show(requireActivity().getSupportFragmentManager(), updateInfoFragment.getTag());
            }
        });

        changeAddress.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), AddressActivity.class);
            intent.putExtra("phone", phoneNo);
            intent.putExtra("profilePage", "update");
            startActivity(intent);
        });

        viewMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.nav_slideshow);
            }
        });


        logoutFromApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                sessionManager.logoutUserFromSession();
                Intent intent = new Intent(requireActivity().getBaseContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        return view;

    }

    private void uploadShopImage() {

        ProgressDialog dialog = new ProgressDialog(requireActivity());
        dialog.setTitle("File Uploading...");
        dialog.show();
        StorageReference storage = FirebaseStorage.getInstance().getReference("SellerInfo")
                .child(phoneNo).child("SellerImage").child("image");

        storage.putFile(uri)
                .addOnSuccessListener(taskSnapshot -> {
                    storage.getDownloadUrl().addOnSuccessListener(uri -> {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("shopImage", uri.toString());
                        FirebaseDatabase.getInstance().getReference("Seller").child(phoneNo).updateChildren(hashMap);
                    });
                    dialog.dismiss();
                    Toast.makeText(requireActivity(), "File uploaded", Toast.LENGTH_SHORT).show();
                })
                .addOnProgressListener(snapshot -> {
                    float percent = (100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                    dialog.setMessage("Uploaded : " + (int) percent + "%");
                });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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
                    shopImage.setImageBitmap(bitmap);
                    uploadShopImage();
                }
            } catch (Exception e) {
                Toast.makeText(requireActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }
}