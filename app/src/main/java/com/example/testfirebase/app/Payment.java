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

import com.example.testfirebase.R;
import com.example.testfirebase.entity.Bill;
import com.example.testfirebase.entity.Food;
import com.example.testfirebase.entity.OrderDetail;
import com.example.testfirebase.entity.Ram;
import com.example.testfirebase.entity.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.UUID;

public class Payment extends AppCompatActivity {
    RecyclerView rcView;
    ArrayList<OrderDetail> arrayList = new ArrayList<>();
    PaymentAdapter paymentAdapter;
    TextView tvTienFood, tvPhiShip, tvTongTien;
    Button btnHT;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment);
        rcView = findViewById(R.id.recyclerView_Payment);
        tvTienFood = findViewById(R.id.tv_TienFood);
        tvPhiShip = findViewById(R.id.tv_PhiShip);
        double aa = 20000;
        tvPhiShip.setText(String.valueOf(aa) + "đ");
        tvTongTien = findViewById(R.id.tv_TongTien);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Payment.this);
        rcView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(Payment.this, DividerItemDecoration.VERTICAL);
        rcView.addItemDecoration(dividerItemDecoration);
        paymentAdapter = new PaymentAdapter(arrayList, new PaymentAdapter.onClick() {
            @Override
            public void remove(OrderDetail orderDetail) {
                removeFood(orderDetail);
            }
        }, this);
        rcView.setAdapter(paymentAdapter);
        btnHT = findViewById(R.id.btn_HT);
        btnHT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hoanThanh();
            }
        });
        getIdBill();
    }

    void getIdBill() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String id_user = mAuth.getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRef = database.getReference("ram/" + id_user);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Ram ram = snapshot.getValue(Ram.class);
                if (ram != null) {
                    getData(ram.getId_bill().getId());
                }
                setTinhTien();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    void getData(String id_bill) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("orderDetail/" + id_bill);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    OrderDetail orderDetail = dataSnapshot.getValue(OrderDetail.class);
                    arrayList.add(orderDetail);
                }
                paymentAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Payment.this, "load data failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void setTinhTien() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String id_user = mAuth.getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRef = database.getReference("ram/" + id_user);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Ram ram = snapshot.getValue(Ram.class);
                if (ram != null) {
                    tinhTien(ram.getId_bill().getId());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void tinhTien(String id_Bill) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("orderDetail/" + id_Bill);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double ship = 20000;
                double tienFood = 0;
                double tong = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    OrderDetail orderDetail = dataSnapshot.getValue(OrderDetail.class);
                    String aa = String.valueOf(orderDetail.getCount());
                    tienFood = (Double.parseDouble(aa) * orderDetail.getId_Food().getPrice()) + tienFood;
                    tong = tienFood + ship;
                }
                tvTienFood.setText(String.valueOf(tienFood) + "đ");
                tvTongTien.setText(String.valueOf(tong) + "đ");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Payment.this, "load data failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void hoanThanh() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String id_user = mAuth.getUid();
        FirebaseDatabase database1 = FirebaseDatabase.getInstance();
        DatabaseReference mRef1 = database1.getReference("ram/" + id_user);
        mRef1.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    String id_bill = UUID.randomUUID().toString();
                    Ram ram = new Ram(new User(id_user), new Bill(id_bill));
                    FirebaseDatabase database1 = FirebaseDatabase.getInstance();
                    DatabaseReference mRef1 = database1.getReference("ram");
                    mRef1.child(id_user).setValue(ram).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                removeBill(id_bill);
                            }
                        }
                    });
                }
            }
        });
    }

    void removeFood(OrderDetail orderDetail) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("orderDetail");
        int soL1 = orderDetail.getCount();
        if (soL1 >= 1) {
            int soL2 = soL1 - 1;
            myRef.child(orderDetail.getId_Bill().getId()).child(orderDetail.getId_Food().getId()).child("count").setValue(soL2);
            if (soL2 < 1) {
                myRef.child(orderDetail.getId_Bill().getId()).child(orderDetail.getId_Food().getId()).removeValue();
            }
        }
        setLai();
        setTinhTien();
    }

    void setLai() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String id_user = mAuth.getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRef = database.getReference("ram/" + id_user);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Ram ram = snapshot.getValue(Ram.class);
                if (ram != null) {
                    String id_Bill = ram.getId_bill().getId();
                    getData1(id_Bill);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void getData1(String id_Bill) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("orderDetail/" + id_Bill);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    OrderDetail orderDetail = dataSnapshot.getValue(OrderDetail.class);
                    arrayList.add(orderDetail);
                }
                paymentAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Payment.this, "load data failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void removeBill(String id_bill) {
        FirebaseDatabase database1 = FirebaseDatabase.getInstance();
        DatabaseReference mRef1 = database1.getReference("bill/" + id_bill);
        mRef1.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    removeOrderDetail(id_bill);
                }
            }
        });
    }

    void removeOrderDetail(String id_bill) {
        FirebaseDatabase database1 = FirebaseDatabase.getInstance();
        DatabaseReference mRef1 = database1.getReference("orderDetail/" + id_bill);
        mRef1.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(Payment.this, List_Food.class);
                    intent.putExtra("key","0");
                    Payment.this.startActivity(intent);
                }
            }
        });
    }
}
