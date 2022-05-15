package com.example.testfirebase.app;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class Add_Food extends AppCompatActivity {
    private EditText edtName, edtPrice;
    TextView textView_Id;
    Button btnAdd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_food);
        edtName = findViewById(R.id.edt_Name_food);
        edtPrice = findViewById(R.id.edt_Price);
        textView_Id = findViewById(R.id.textView_Id);
        String id = UUID.randomUUID().toString();
        textView_Id.setText(id);
        btnAdd = findViewById(R.id.btn_Add_food);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFood();
            }
        });
    }

    private void addFood() {
        String id = textView_Id.getText().toString();
        String name = edtName.getText().toString();
        String price = edtPrice.getText().toString();
        Food food = new Food(id, name, Double.parseDouble(price));
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("food");
        myRef.child(id).setValue(food).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    String id1 = UUID.randomUUID().toString();
                    textView_Id.setText(id1);
                    edtName.setText("");
                    edtPrice.setText("");
                    Toast.makeText(Add_Food.this, "Thanh Cong!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
