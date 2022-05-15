package com.example.testfirebase.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testfirebase.entity.Food;
import com.example.testfirebase.R;
import com.example.testfirebase.entity.OrderDetail;
import com.example.testfirebase.entity.Ram;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class List_Food extends AppCompatActivity {
    private RecyclerView rvListUser;
    private FoodAdapter foodAdapter;
    private List<Food> mListFood;
    private TextView txt_int;
    Button btnPayment;

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.list_user);
        txt_int = findViewById(R.id.txt_int);
        rvListUser = findViewById(R.id.rvListUser);
        btnPayment = findViewById(R.id.btn_Payment);
        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(List_Food.this, Payment.class);
                startActivity(intent);
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(List_Food.this);
        rvListUser.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(List_Food.this, DividerItemDecoration.VERTICAL);
        rvListUser.addItemDecoration(dividerItemDecoration);
        mListFood = new ArrayList<>();
        foodAdapter = new FoodAdapter(mListFood, this, new FoodAdapter.onclick() {
            @Override
            public void add(Food food) {
                getDataItem(food);
            }
        });
        rvListUser.setAdapter(foodAdapter);
        getListFood();
        setSL();
        check();
    }

    void getListFood() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("food");
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Food food = snapshot.getValue(Food.class);
                if (food != null) {
                    mListFood.add(food);
                    foodAdapter.notifyDataSetChanged();
                } else {
                    return;
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void getDataItem(Food food) {
        Intent intent = new Intent(this, Food_infor.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("food_infor", food);
        intent.putExtras(bundle);
        this.startActivity(intent);

    }

    void setSL() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String id_user = mAuth.getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("ram/" + id_user);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Ram ram = snapshot.getValue(Ram.class);
                if (ram != null) {
                    String id_Bill = ram.getId_bill().getId();
                    getSLuong(id_Bill);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void getSLuong(String id_Bill) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("orderDetail/" + id_Bill);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() == null) {
                    return;
                } else {
                    String i = String.valueOf(snapshot.getChildrenCount());
                    txt_int.setText(i);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void check() {
        Intent intent = getIntent();
        String aa = intent.getStringExtra("key");
        if (aa == null) {
            return;
        } else
            txt_int.setText(aa);
    }


}
