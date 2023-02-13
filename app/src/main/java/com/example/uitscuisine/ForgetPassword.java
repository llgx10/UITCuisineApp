package com.example.uitscuisine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.airbnb.lottie.LottieDrawable;
import com.amrdeveloper.lottiedialog.LottieDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ForgetPassword extends AppCompatActivity {
    ImageView forget_password_back_btn;
    Button forget_password_next_btn;
    EditText forget_pass_phone;
    String phone_check_login;
    LottieDialog Dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpassword);
        forget_pass_phone        = (EditText) findViewById(R.id.forget_pass_phone);
        forget_password_back_btn = (ImageView) findViewById(R.id.forget_password_back_btn);
        forget_password_next_btn = (Button)findViewById(R.id.forget_password_next_btn);
        Dialog = new LottieDialog(ForgetPassword.this)
                .setAnimation(R.raw.pan)
                .setAnimationRepeatCount(LottieDrawable.INFINITE)
                .setAutoPlayAnimation(true)
                .setMessage("Check Phone Loading...")
                .setMessageTextSize(30)
                .setDialogBackground(Color.WHITE)
                .setAnimationViewHeight(400)
                .setCancelable(false);
        forget_password_back_btn.setOnClickListener(this::backLogin);
        forget_password_next_btn.setOnClickListener(this::NextPass);
    }
    public void backLogin(View view){
        Toast.makeText(this, "LOGIN", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(ForgetPassword.this,Login.class);
        startActivity(intent);
    }
    public void NextPass(View view){
            if(!checkPhone()){
                return;
            }else{
                Toast.makeText(this, "Create your new password", Toast.LENGTH_SHORT).show();
            }
    }
    private Boolean checkPhone(){
        Dialog.show();
        String val = forget_pass_phone.getText().toString();
        Intent intent = getIntent();
        phone_check_login = intent.getStringExtra("mobile");
        if(val.isEmpty()){
            forget_pass_phone.setError("Field cannot be empty");
            forget_pass_phone.requestFocus();
            return false;
        } else if(val.length() != 10){
            forget_pass_phone.setError("Must be 10 numbers");
            return false;
        }
        else{
            DatabaseReference database = FirebaseDatabase.getInstance().getReference("User");
            Query checkUser = database.orderByChild("mobile").equalTo(val);
            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        Toast.makeText(ForgetPassword.this, "Correct your phone number", Toast.LENGTH_SHORT).show();
                        Intent intent1 = new Intent(ForgetPassword.this, ResetPassword.class);
                        intent1.putExtra("mobile",val);
                        startActivity(intent1);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            Dialog.dismiss();
            forget_pass_phone.setError(null);
            return true;
        }
    }
}
