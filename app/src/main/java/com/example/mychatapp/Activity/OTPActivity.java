package com.example.mychatapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.mychatapp.databinding.ActivityOTPBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.mukesh.OnOtpCompletionListener;


import java.util.concurrent.TimeUnit;

public class OTPActivity extends AppCompatActivity {
    ActivityOTPBinding binding;
    //4.fireBASEAUTH
    FirebaseAuth auth;

    //7.
    String varificationId;

    //11.to show progress dialog
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOTPBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //11.*
        dialog = new ProgressDialog(this);
        dialog.setTitle("Sending Otp");
        dialog.setCancelable(false);
        dialog.show();


        //4.*
        auth = FirebaseAuth.getInstance();

        getSupportActionBar().hide();//to hide action bar

     //10.to open keyboard
       // binding.otpView.requestFocus();


        //2.get the data from putExtra of PhoneActivity
        String phoneNumber = getIntent().getStringExtra("phoneNumber");

        //3.to bind the number on TextView
        binding.phonelbl.setText("Varify:" +phoneNumber);


        //5.to set otp timing
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(OTPActivity.this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onCodeSent(@NonNull String varifyId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(varifyId, forceResendingToken);

                        dialog.dismiss();
                        //8.set
                        varificationId = varifyId;
                        //10.if keyboard is not opning to open keyboard
                        InputMethodManager imm = (InputMethodManager)   getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                        binding.otpView.requestFocus();


                    }
                }).build();
        //6.To varify the phone no.
        //ye line uper ki  methods ko follow karke  ek code send kar degi humare no par
        PhoneAuthProvider.verifyPhoneNumber(options);

        //9.otp jo send kiya vo phone number se match bhi kar raha ya nahi continue button par click nhi karna padega
        binding.otpView.setOtpCompletionListener(new OnOtpCompletionListener()
        {
            @Override
            public void onOtpCompleted(String otp)
            {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(varificationId,otp);
                //to sign in firebase authentication will check ki hum log signin kar pa rahe ya nhi
                auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            //17.
                            Intent intent = new Intent(OTPActivity.this, SetUpProfileActivity.class);
                            startActivity(intent);
                            finishAffinity();//sabhi activity ko finish karega finishActivity() sirf ek activity ko finish karega
                           // Toast.makeText(OTPActivity.this, "success", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(OTPActivity.this, "failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });





    }
}