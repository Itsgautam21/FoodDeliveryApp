package com.example.seller.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.seller.Models.FoodRvModel;
import com.example.seller.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import org.jetbrains.annotations.NotNull;

public class FoodRvAdapter extends FirebaseRecyclerAdapter<FoodRvModel, FoodRvAdapter.ViewHolder> {

    public FoodRvAdapter(@NonNull @NotNull FirebaseRecyclerOptions<FoodRvModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position, @NonNull @NotNull FoodRvModel model) {
        Glide.with(holder.foodImage.getContext()).load(R.drawable.pizza).into(holder.foodImage);
        holder.foodName.setText(model.getFoodName());
        holder.foodDescription.setText(model.getFoodDescription());
        holder.foodPrice.setText(model.getFoodPrice());
        holder.foodCount.setText(model.getFoodCount());

    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_food_rv, parent,false);
        return new ViewHolder(view);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView foodImage;
        TextView foodName, foodDescription, foodPrice, foodCount;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            foodImage = itemView.findViewById(R.id.foodImage);
            foodName = itemView.findViewById(R.id.foodName);
            foodDescription = itemView.findViewById(R.id.foodDescription);
            foodPrice = itemView.findViewById(R.id.foodPrice);
            foodCount = itemView.findViewById(R.id.foodCount);
        }
    }
}
