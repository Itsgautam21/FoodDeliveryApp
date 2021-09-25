package com.example.seller.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.seller.Models.CategoryModel;
import com.example.seller.R;
import com.example.seller.SessionManager;
import com.example.seller.ui.slideshow.SlideshowFragmentDirections;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;


public class CategoryAdapter extends FirebaseRecyclerAdapter<CategoryModel, CategoryAdapter.ViewHolder> {

    boolean isSelected;
    ArrayList<CategoryModel> data;
    Context context;
    public CategoryAdapter(@NonNull @NotNull FirebaseRecyclerOptions<CategoryModel> options) {
        super(options);
    }


    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_menu_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position, @NonNull @NotNull CategoryModel model) {
        Glide.with(holder.categoryImage.getContext()).load(model.getCategoryImage()).into(holder.categoryImage);
        holder.categoryName.setText(model.getCategoryName());

        holder.itemView.setOnClickListener(v -> {
            String phone = new SessionManager(context).getUserDetailFromSession().get(SessionManager.KEY_PHONE_NO);
            assert phone != null;
            String key = FirebaseDatabase.getInstance().getReference("Seller").child(phone).child("Menu").child(Objects.requireNonNull(getRef(position).getKey())).getKey();
            SlideshowFragmentDirections.ActionNavSlideshowToMenuFragment action =
                    SlideshowFragmentDirections.actionNavSlideshowToMenuFragment(key);
            Navigation.findNavController(v).navigate(action);
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {


                return false;

            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView categoryImage;
        TextView categoryName;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            categoryImage = itemView.findViewById(R.id.categoryImage);
            categoryName = itemView.findViewById(R.id.categoryName);
        }
    }
}
