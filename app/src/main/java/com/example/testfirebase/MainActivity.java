package com.example.testfirebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private Button btnAdd;
    private EditText edtName;
    private EditText edtEmamil;
    private EditText edtDC;
    private EditText edtSdt;
    private EditText edtPass;
    //upload data to firebase
    User user;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    private FirebaseAuth maAuth;
    //upload image to firebase
    private Button btnAddimage;
    private ImageView imageView;
    private Uri uri;
    String picturePath;
    Uri selectedImage;
    Bitmap photo;
    String ba1;
    private int PICK_IMAGE_REQUEST = 1;

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
        btnAddimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickpic();
            }
        });
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

    private void upload() {
        // image location URL
        Log.e("path", "........" + picturePath);
        // image
        Bitmap bm = BitmapFactory.decodeFile(picturePath);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 90, bao);
        byte[] ba = bao.toByteArray();

        Log.e("base64", "...." + ba1);

    }

    private void clickpic() {
        Intent intent = new Intent();
        // Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");//image bitmap file
            Bitmap resizeBitmap = Bitmap.createScaledBitmap(bitmap,680,500,false);
            imageView.setImageBitmap(resizeBitmap);
        }
        else if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));

                //ImageView imageView = (ImageView) findViewById(R.id.imageView);
                Bitmap resizeBitmap = Bitmap.createScaledBitmap(bitmap,680,500,false);
                imageView.setImageBitmap(resizeBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


}