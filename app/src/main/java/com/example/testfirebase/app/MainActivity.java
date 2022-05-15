package com.example.testfirebase.app;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.testfirebase.R;
import com.example.testfirebase.entity.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private Button btnAdd;
    private EditText edtName;
    private EditText edtEmamil;
    private EditText edtDC;
    private EditText edtSdt;
    private EditText edtPass;
    private ProgressBar progressBar;
    //upload data to firebase
    User user;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    private FirebaseAuth maAuth;
    //upload image to firebase
    private Button btnAddimage;
    private ImageView imageView;

    FirebaseStorage storage;
    StorageReference storageReference;

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
        imageView = findViewById(R.id.image);
        btnAddimage = findViewById(R.id.btnAddImg);
        progressBar = findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.INVISIBLE);
        btnAddimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });
    }

    ActivityResultLauncher<Intent> startForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent intent = result.getData();
                Uri uri = intent.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                    Bitmap resizeBitmap = Bitmap.createScaledBitmap(bitmap, 680, 500, false);
                    imageView.setImageBitmap(resizeBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    });

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startForResult.launch(intent);
    }

    private void uploadImage() {
        Bitmap capture = Bitmap.createBitmap(imageView.getWidth(), imageView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas captureCanvas = new Canvas(capture);
        imageView.draw(captureCanvas);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        capture.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] data = outputStream.toByteArray();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference("images/" + UUID.randomUUID() + ".png");
        UploadTask uploadTask = storageReference.putBytes(data);
        progressBar.setVisibility(View.VISIBLE);
        btnAdd.setEnabled(false);
        uploadTask.addOnCompleteListener(MainActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "đã tải ảnh lên storage!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Task<Uri> getDowloadUri = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return storageReference.getDownloadUrl();
            }
        });
        getDowloadUri.addOnCompleteListener(MainActivity.this, new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri dowloadUri = task.getResult();
                    createUser(dowloadUri.toString());
                    progressBar.setVisibility(View.INVISIBLE);
                    btnAdd.setEnabled(true);

                }
            }
        });
    }

    private void createUser(String image) {
        String name = edtName.getText().toString();
        final String email = edtEmamil.getText().toString();
        String sdt = edtSdt.getText().toString();
        String dc = edtDC.getText().toString();
        String pass = edtPass.getText().toString();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("user");
        maAuth = FirebaseAuth.getInstance();
        maAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String id = maAuth.getUid();
                            user = new User(id, name, email, sdt, dc, pass, image);
                            databaseReference.child(id).setValue(user);
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

}