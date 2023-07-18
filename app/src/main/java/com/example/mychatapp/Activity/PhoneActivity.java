package com.example.mychatapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.mychatapp.databinding.ActivityPhoneBinding;
import com.google.firebase.auth.FirebaseAuth;

public class PhoneActivity extends AppCompatActivity {
    ActivityPhoneBinding binding;
    //21.bar bar number  ke liye nhi bolna chahiye
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPhoneBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //20*.
        auth = FirebaseAuth.getInstance();
        //bar bar number  ke liye nhi bolna chahiye
        if (auth.getCurrentUser() != null)
        {
            Intent intent = new Intent(PhoneActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        getSupportActionBar().hide();//to hide action bar

        binding.phoneBox.requestFocus();//to open keyboard

        binding.btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PhoneActivity.this, OTPActivity.class);
                //1. agar hume kuch data bhejna hai to hum putExtra use karte hai
                intent.putExtra("phoneNumber", binding.phoneBox.getText().toString());
                startActivity(intent);

            }
        });

    }
}