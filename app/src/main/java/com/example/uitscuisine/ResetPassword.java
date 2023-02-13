package com.example.uitscuisine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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

public class ResetPassword extends AppCompatActivity {
    ImageView forget_password_back_btn;
    Button forget_password_next_btn;
    EditText password,password2;
    LottieDialog Dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        forget_password_back_btn = (ImageView) findViewById(R.id.forget_password_back_btn);
        forget_password_next_btn = (Button)findViewById(R.id.forget_password_next_btn);
        password                 = (EditText)findViewById(R.id.password);
        password2                = (EditText)findViewById(R.id.password2);
        forget_password_back_btn.setOnClickListener(this::backLogin);
        forget_password_next_btn.setOnClickListener(this::Reset_Password);

        Dialog = new LottieDialog(ResetPassword.this)
                .setAnimation(R.raw.pan)
                .setAnimationRepeatCount(LottieDrawable.INFINITE)
                .setAutoPlayAnimation(true)
                .setMessage("Update Password Loading...")
                .setMessageTextSize(30)
                .setDialogBackground(Color.WHITE)
                .setAnimationViewHeight(400)
                .setCancelable(false);
        Intent intent = getIntent();

    }
    public void backLogin(View view){
        Intent intent1 = new Intent(ResetPassword.this, Login.class);
        startActivity(intent1);
    }
    public void Reset_Password(View view){
        Dialog.show();
            try{

                if(!checkPass1() ||!checkPass2()){
                    Dialog.dismiss();
                    return;
                }else {
                    Intent intent = getIntent();
                    String phone_check_login = intent.getStringExtra("mobile");
                    String new_pass = password2.getText().toString();
                    DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("User");
                    Query checkUser = database.orderByKey();
                    checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                String phoneFromDB = dataSnapshot.child("mobile").getValue().toString();
                                if(phone_check_login.equals(phoneFromDB)){
                                    String username = dataSnapshot.child("username").getValue().toString();
                                   // String mobile   = dataSnapshot.child("mobile").getValue().toString();
                                    String email    = dataSnapshot.child("email").getValue().toString();
                                    User user  = new User(username,new_pass,email,phoneFromDB);
                                    database.child(phoneFromDB).setValue(user);
                                    Toast.makeText(ResetPassword.this, "Reset password successfully!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ResetPassword.this, Login.class);
                                    intent.putExtra("mobile",phoneFromDB);
                                    //intent.putExtra("email",email_signup);
                                    Dialog.dismiss();
                                    startActivity(intent);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }catch(IllegalStateException e){
                e.printStackTrace();
            }
        }
    private Boolean checkPass1 (){
        String phone_check    = password.getText().toString();
        if(phone_check.isEmpty()){
            password.setError("Field cannot be empty");
            return false;
        }
        else{
            password.setError(null);
            return true;
        }
    }
    private Boolean checkPass2 (){
        String phone_check1    = password.getText().toString();
        String phone_check2    = password2.getText().toString();
        if(phone_check2.isEmpty()){
            password2.setError("Field cannot be empty");
            return false;
        }
        else if(!phone_check2.equals(phone_check1)){
            password2.setError("Must be Password matches New Password");
            return false;
        }
        else{
            password.setError(null);
            return true;
        }
    }
}