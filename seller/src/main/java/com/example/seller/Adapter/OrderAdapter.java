package com.example.seller.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.seller.Models.FoodRvModel;
import com.example.seller.Models.OrderModal;
import com.example.seller.R;
import com.example.seller.SessionManager;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class OrderAdapter extends FirebaseRecyclerAdapter<OrderModal, OrderAdapter.ViewHolder> {

    Context context;

    public OrderAdapter(@NonNull @NotNull FirebaseRecyclerOptions<OrderModal> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position, @NonNull @NotNull OrderModal model) {

        holder.buyerName.setText(model.getBuyerName());
        holder.buyerAddress.setText(model.getBuyerAddress());
        holder.buyerState.setText(model.getBuyerState());
        holder.buyerPhoneNo.setText(model.getBuyerPhoneNo());
        holder.totalPrice.setText(model.getBuyerTotalPrice());

        String phoneNo = new SessionManager(context).getUserDetailFromSession().get(SessionManager.KEY_PHONE_NO);
        assert phoneNo != null;
        FirebaseRecyclerOptions<FoodRvModel> options = new FirebaseRecyclerOptions.Builder<FoodRvModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference("Seller").child(phoneNo).child("Delivery order")
                        .child(getRef(position).getKey()).child("Food"), FoodRvModel.class)
                .build();
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        FoodRvAdapter adapter = new FoodRvAdapter(options);
        holder.recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_order, parent,false);
        return new ViewHolder(view);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView buyerName, buyerAddress, buyerState, buyerPhoneNo;
        RecyclerView recyclerView;
        TextView totalPrice;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            buyerName = itemView.findViewById(R.id.buyerName);
            buyerAddress = itemView.findViewById(R.id.buyerAddress);
            buyerState = itemView.findViewById(R.id.buyerState);
            buyerPhoneNo = itemView.findViewById(R.id.buyerPhoneNo);
            recyclerView = itemView.findViewById(R.id.foodRv);
            totalPrice = itemView.findViewById(R.id.totalPrice);

        }
    }
}
