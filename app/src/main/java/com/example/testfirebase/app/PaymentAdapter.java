package com.example.testfirebase.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testfirebase.R;
import com.example.testfirebase.entity.OrderDetail;

import java.util.ArrayList;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.PaymentViewHoler> {
    ArrayList<OrderDetail> arrayList;
    onClick abc;
    Context ctx;

    public interface onClick {
        void remove(OrderDetail orderDetail);
    }

    public PaymentAdapter(ArrayList<OrderDetail> arrayList, onClick abc, Context ctx) {
        this.arrayList = arrayList;
        this.abc = abc;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public PaymentViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
        return new PaymentViewHoler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentViewHoler holder, int position) {
        OrderDetail orderDetail = arrayList.get(position);
        if (orderDetail == null) {
            return;
        }
        holder.tv_SL.setText(String.valueOf(orderDetail.getCount()));
        holder.tv_Name.setText(orderDetail.getId_Food().getName());
        holder.tv_Price.setText(String.valueOf(orderDetail.getId_Food().getPrice()));
        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abc.remove(orderDetail);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (arrayList != null) {
            return arrayList.size();
        }
        return 0;
    }

    public class PaymentViewHoler extends RecyclerView.ViewHolder {
        TextView tv_SL, tv_Name, tv_Price;
        Button btnRemove;

        public PaymentViewHoler(@NonNull View itemView) {
            super(itemView);
            tv_SL = itemView.findViewById(R.id.tv_SL_food);
            tv_Name = itemView.findViewById(R.id.tv_Name_food_payment);
            tv_Price = itemView.findViewById(R.id.tv_Price_food_payment);
            btnRemove = itemView.findViewById(R.id.btn_X);
        }
    }
}
