package com.example.testfirebase.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testfirebase.R;
import com.example.testfirebase.entity.Bill;
import com.example.testfirebase.entity.Ram;
import com.example.testfirebase.entity.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class Login extends AppCompatActivity {
    private EditText edtEmail, edtPassWord;
    private Button btnLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        edtEmail = findViewById(R.id.edt_Name_Lgoin);
        edtPassWord = findViewById(R.id.edt_Pass_Login);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

    }

    private void login() {
        String email = edtEmail.getText().toString();
        String pass = edtPassWord.getText().toString();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String id_user = mAuth.getUid();
                    String id_bill = UUID.randomUUID().toString();
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference mRef = database.getReference("ram");
                    Ram ram = new Ram(new User(id_user),new Bill(id_bill));
                    mRef.child(id_user).setValue(ram);
                    Toast.makeText(Login.this, "Thanh Cong!!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this, List_Food.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(Login.this, "That bai!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
