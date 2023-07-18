package com.example.mychatapp.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.example.mychatapp.R;
import com.example.mychatapp.databinding.ActivitySetUpProfileBinding;
import com.example.mychatapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

//13.
public class SetUpProfileActivity extends AppCompatActivity {
    ActivitySetUpProfileBinding binding;
    //14.
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    //18.
    Uri selectedImage;
        //20.
    ProgressDialog  dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivitySetUpProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //20*
        dialog =  new ProgressDialog(this);
        dialog.setMessage("Uploading Profile.....");
        dialog.setCancelable(false);

        //14*
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage =  FirebaseStorage.getInstance();

        //15 Image wiew par click ke liye
        binding.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");//to select all type of images
                startActivityForResult(intent,45);//yaha se image ka refernce milega
            }
        });
        //19 onclick of button
        binding.btnSetUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.nameBox.getText().toString();//to get value from field
                if (name.isEmpty())
                {
                    binding.nameBox.setError("plese type name");
                    return;
                }
                //20*
                dialog.show();

                if (selectedImage != null)
                {
                    //storage me Profiles nam ka folder banayega or usme store karega
                    StorageReference reference = storage.getReference().child("Profiles").child(auth.getUid());// child(auth.getUid()) us file ka nam ky hoga FA me vo decide karta hai
                    reference.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful())
                            {
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imageUrl =  uri.toString();
                                        String uid = auth.getUid();
                                        String phone = auth.getCurrentUser().getPhoneNumber();
                                        String name =  binding.nameBox.getText().toString();

                                        User user =  new User(uid,name,phone,imageUrl);

                                        //agar image load ho gae to
                                        database.getReference()
                                                .child("users")
                                                .child(uid)
                                                .setValue(user)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        //20*
                                                        dialog.dismiss();
                                                        Intent intent = new Intent(SetUpProfileActivity.this,MainActivity.class);
                                                        startActivity(intent);
                                                        finish();

                                                    }
                                                });
                                    }
                                });
                            }
                        }
                    });
                }
                else {
                    String uid = auth.getUid();
                    String phone = auth.getCurrentUser().getPhoneNumber();

                    User user =  new User(uid,name,phone,"No Image");

                    //agar image load ho gae to
                    database.getReference()
                            .child("users")
                            .child(uid)
                            .setValue(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    //20*
                                    dialog.dismiss();
                                    Intent intent = new Intent(SetUpProfileActivity.this,MainActivity.class);
                                    startActivity(intent);
                                    finish();

                                }
                            });
                }

            }
        });

    }
    //16 ya par image ka referance milega
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if (data != null)
            {
                if (data.getData() != null)
                {
                    binding.imageView.setImageURI(data.getData());
                    //18* Image
                    selectedImage = data.getData();//jo bhi imaga select ki uska ref selectedImage me dikhega/store hoga
                }
            }
    }
}