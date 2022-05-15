package com.example.testfirebase.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testfirebase.entity.Food;
import com.example.testfirebase.R;
import com.example.testfirebase.entity.Bill;
import com.example.testfirebase.entity.OrderDetail;
import com.example.testfirebase.entity.Ram;
import com.example.testfirebase.entity.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class Food_infor extends AppCompatActivity {
    EditText edt_name, edt_price;
    TextView tvSL;
    Button cong, tru, add, back;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_create_bill);
        edt_name = findViewById(R.id.edt_id_food);
        edt_price = findViewById(R.id.edt_Price_food);
        back = findViewById(R.id.btnBack_foodInfor);
        tvSL = findViewById(R.id.tv_SL);
        tru = findViewById(R.id.btnn_Tru);
        cong = findViewById(R.id.btn_Cong);
        add = findViewById(R.id.bttn_Create_bill);
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            Toast.makeText(this, "Null!!", Toast.LENGTH_SHORT).show();
            return;
        }
        Food food = (Food) bundle.get("food_infor");
        edt_name.setText(food.getName());
        edt_price.setText(String.valueOf(food.getPrice()));
        String id_food = food.getId();
        String id_user = mAuth.getUid();
        tru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String soLuong = tvSL.getText().toString();
                int soLuong1 = Integer.parseInt(soLuong) - 1;
                if (soLuong1 <= 0) {
                    tvSL.setText("1");
                } else
                    tvSL.setText(String.valueOf(soLuong1));
            }
        });
        cong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String soLuong = tvSL.getText().toString();
                int soLuong2 = Integer.parseInt(soLuong) + 1;
                tvSL.setText(String.valueOf(soLuong2));
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getIDBill(id_food);
                Intent intent = new Intent(Food_infor.this, List_Food.class);
                startActivity(intent);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Food_infor.this, List_Food.class);
                startActivity(intent);
            }
        });

    }

    void getIDBill(String id_food) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String id_user = mAuth.getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRef = database.getReference("ram/" + id_user);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Ram ram = snapshot.getValue(Ram.class);
                if (ram != null) {
                    createBill(ram.getId_bill().getId());
                    createOrderDetail(ram.getId_bill().getId(), id_food);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void createBill(String id_bill) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String id_user = mAuth.getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRef = database.getReference("bill");
        Bill bill = new Bill(id_bill, new User(id_user));
        mRef.child(id_bill).setValue(bill);
    }

    void createOrderDetail(String id_bill, String id_food) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRef = database.getReference("orderDetail");
        String name = edt_name.getText().toString();
        double price = Double.parseDouble(edt_price.getText().toString());
        int soLuong = Integer.parseInt(tvSL.getText().toString());
        OrderDetail orderDetail = new OrderDetail(soLuong, new Food(id_food, name, price), new Bill(id_bill));
        mRef.child(id_bill).child(id_food).setValue(orderDetail);
    }

}
