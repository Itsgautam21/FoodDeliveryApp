package com.example.seller.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.seller.Models.MenuModel;
import com.example.seller.R;
import com.example.seller.SessionManager;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Objects;


public class MenuAdapter extends FirebaseRecyclerAdapter<MenuModel, MenuAdapter.ViewHolder> {

    Context context;
    String key;

    public MenuAdapter(@NonNull @NotNull FirebaseRecyclerOptions<MenuModel> options, String key) {
        super(options);
        this.key = key;
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position, @NonNull @NotNull MenuModel model) {
        Glide.with(holder.foodImage.getContext()).load(model.getFoodImage()).into(holder.foodImage);
        holder.foodName.setText(model.getFoodName());
        holder.foodDescription.setText(model.getFoodDescription());
        holder.foodPrice.setText(model.getFoodPrice());
        holder.aSwitch.setChecked(model.isAvailable());

        String phone = new SessionManager(context).getUserDetailFromSession().get(SessionManager.KEY_PHONE_NO);

        holder.aSwitch.setOnClickListener(v -> {

            if (holder.aSwitch.isChecked()) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("available", true);
                assert phone != null;
                FirebaseDatabase.getInstance().getReference("Seller").child(phone)
                        .child("Menu").child(Objects.requireNonNull(getRef(position).getKey()))
                        .updateChildren(hashMap);
            } else {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("available", false);
                assert phone != null;
                FirebaseDatabase.getInstance().getReference("Seller").child(phone)
                        .child("Menu").child(Objects.requireNonNull(getRef(position).getKey()))
                        .updateChildren(hashMap);
            }
        });
        holder.deleteMenu.setOnClickListener(v -> {
            assert phone != null;
            int pos = position;
            if (pos != 0) pos = position - 1;
            FirebaseDatabase.getInstance().getReference("Seller").child(phone).child("Menu")
                    .child(key).child("category").child(Objects.requireNonNull(getRef(pos).getKey())).removeValue();
            Toast.makeText(context, "Item Removed", Toast.LENGTH_SHORT).show();
        });
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_menu, parent, false);
        return new ViewHolder(view);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView foodImage;
        TextView foodName, foodDescription, foodPrice;
        ImageButton deleteMenu;

        @SuppressLint("UseSwitchCompatOrMaterialCode")
        SwitchCompat aSwitch;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            foodImage = itemView.findViewById(R.id.foodImage);
            foodName = itemView.findViewById(R.id.foodName);
            foodDescription = itemView.findViewById(R.id.foodDescription);
            foodPrice = itemView.findViewById(R.id.foodPrice);
            aSwitch = itemView.findViewById(R.id.SWEDISH);
            deleteMenu = itemView.findViewById(R.id.deleteMenu);
        }
    }
}
