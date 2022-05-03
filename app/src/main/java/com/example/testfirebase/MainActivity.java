package com.example.testfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private Button btnAdd;
    private EditText edtName;
    private EditText edtEmamil;
    private EditText edtDC;
    private EditText edtSdt;
    private EditText edtPass;
    private
    User user;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    private FirebaseAuth maAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnAdd = findViewById(R.id.btnAdd);
        edtName = findViewById(R.id.txtName);
        edtEmamil = findViewById(R.id.txtemail);
        edtSdt = findViewById(R.id.txtsdt);
        edtDC = findViewById(R.id.txtDC);
        edtPass = findViewById(R.id.txtPass);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edtName.getText().toString();
                final String email = edtEmamil.getText().toString();
                String sdt = edtSdt.getText().toString();
                String dc = edtDC.getText().toString();
                String pass = edtPass.getText().toString();
                user = new User(name, email, sdt, dc, pass);
                database = FirebaseDatabase.getInstance();
                databaseReference = database.getReference("user");
                maAuth = FirebaseAuth.getInstance();
                maAuth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    databaseReference.child(sdt).setValue(user);
                                    AlertDialog.Builder showmessgae = new AlertDialog.Builder(MainActivity.this);
                                    showmessgae.setMessage("mày đã thành công lưu được 1 thằng nghịch tử vô sổ tử!!").
                                            setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.dismiss();
                                                    edtName.setText("");
                                                    edtEmamil.setText("");
                                                    edtSdt.setText("");
                                                    edtDC.setText("");
                                                    edtPass.setText("");
                                                }
                                            })
                                            .create();
                                    showmessgae.show();
                                }
                            }
                        });
            }
        });
    }
}