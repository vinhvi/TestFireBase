package com.example.testfirebase.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testfirebase.entity.Food;
import com.example.testfirebase.R;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.UserViewHoler> {

    private List<Food> mListFood;
    private Context context;
    private onclick abc;

    public interface onclick {
        void add(Food food);
    }

    public FoodAdapter(List<Food> mListFood, Context context, onclick abc) {
        this.mListFood = mListFood;
        this.context = context;
        this.abc = abc;
    }

    @NonNull
    @Override
    public FoodAdapter.UserViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHoler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodAdapter.UserViewHoler holder, int position) {
        Food food = mListFood.get(position);
        if (food == null) {
            return;
        }
        holder.txtName.setText("Name:" + food.getName());
        holder.txtEmail.setText("Email: " + food.getPrice());
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abc.add(food);
            }
        });


    }


    @Override
    public int getItemCount() {
        if (mListFood != null) {
            return mListFood.size();
        }
        return 0;
    }

    public class UserViewHoler extends RecyclerView.ViewHolder {
        private TextView txtName;
        private TextView txtEmail;
        private Button btnadd;
        ConstraintLayout constraintLayout;

        public UserViewHoler(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.tv_Name);
            txtEmail = itemView.findViewById(R.id.tv_Email);
            btnadd = itemView.findViewById(R.id.btnAdd_user);
            constraintLayout = itemView.findViewById(R.id.itemLayout);
        }
    }
}
