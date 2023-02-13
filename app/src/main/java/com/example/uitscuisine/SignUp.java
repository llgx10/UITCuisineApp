package com.example.uitscuisine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieDrawable;
import com.amrdeveloper.lottiedialog.LottieDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SignUp extends AppCompatActivity {

    //Variables
    Animation topAmin , bottomAmin, randomAmin,bounceAmin,lefttorightAmin;
    ImageView logo_signup;
    ImageButton ivback;
    Button button_signup;
    EditText username_register,password_register,email_register,phone_register;
    TextView text_user,text_pass,text_email,text_phone,text_login,text_alr,sign_up;
    LottieDialog Dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);
        //mapped animation
        lefttorightAmin = AnimationUtils.loadAnimation(this,R.anim.lefttoright);
        bounceAmin = AnimationUtils.loadAnimation(this,R.anim.bounce);
        topAmin = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAmin = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        randomAmin = AnimationUtils.loadAnimation(this,R.anim.random_animation);
        //mapped variables
        logo_signup       = (ImageView) findViewById(R.id.logo_signup);
        ivback            = (ImageButton) findViewById(R.id.ivBack);
        button_signup     = (Button)   findViewById(R.id.button_signup);
        username_register = (EditText) findViewById(R.id.username_register);
        password_register = (EditText) findViewById(R.id.password_register);
        email_register    = (EditText) findViewById(R.id.email_register);
        phone_register    = (EditText) findViewById(R.id.phone_register);
        text_user         = (TextView) findViewById(R.id.text_user);
        text_pass         = (TextView) findViewById(R.id.text_pass);
        text_email        = (TextView) findViewById(R.id.text_email);
        text_phone        = (TextView) findViewById(R.id.text_phone);
        text_alr          = (TextView) findViewById(R.id.text_alr);
        text_login        = (TextView) findViewById(R.id.text_login);
        sign_up           = (TextView) findViewById(R.id.sign_up);
        //Set items animation
        logo_signup.setAnimation(topAmin);
        ivback.setAnimation(topAmin);
        username_register.setAnimation(topAmin);
        text_user.setAnimation(topAmin);
        password_register.setAnimation(topAmin);
        text_pass.setAnimation(topAmin);
        sign_up.setAnimation(topAmin);
        email_register.setAnimation(bottomAmin);
        text_email.setAnimation(bottomAmin);
        phone_register.setAnimation(bottomAmin);
        text_phone.setAnimation(bottomAmin);
        button_signup.setAnimation(bottomAmin);
        text_alr.setAnimation(bottomAmin);
        text_login.setAnimation(bottomAmin);

        //ivBack button handle --> comeback login activity
        ivback.setOnClickListener(this::ivback_handle);
        text_login.setOnClickListener(this::ivback_handle);
        button_signup.setOnClickListener(this::Signup);

        Dialog = new LottieDialog(SignUp.this)
                .setAnimation(R.raw.pan)
                .setAnimationRepeatCount(LottieDrawable.INFINITE)
                .setAutoPlayAnimation(true)
                .setMessage("Signup Loading...")
                .setMessageTextSize(30)
                .setDialogBackground(Color.WHITE)
                .setAnimationViewHeight(400)
                .setCancelable(false);

    }
    public void ivback_handle(View view) {
        ivback.startAnimation(bounceAmin);
        Toast.makeText(this, "LOGIN", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(SignUp.this, Login.class);
        startActivity(intent);
    }
    private Boolean checkName(){
        String val = username_register.getText().toString();
        String noWhiteSpace = "\\A\\w{4,20}\\z";
        if(val.isEmpty()){
            username_register.setError("Field cannot be empty");
            return false;
        }
        else if(val.length() >= 15){
            username_register.setError("Name is too long");
            return false;
        }
        else{
            username_register.setError(null);
            return true;
        }
    }
    private Boolean checkPassword(){
        String val = password_register.getText().toString();
        if(val.isEmpty()){
            password_register.setError("Field cannot be empty");
            return false;
        }

        else if(val.contains(" ")){
            password_register.setError("No white space please");
            return false;
        }
        else{
            password_register.setError(null);
            return true;
        }
    }
    private Boolean checkEmail(){
        String email_check = email_register.getText().toString();
        String email_pattern = "[a-zA-z0-9._-]+@[a-z]+\\.+[a-z]+";

        if(email_check.isEmpty()){
            email_register.setError("Field cannot be empty");
            return false;
        }
        else if(!email_check.matches(email_pattern)){
            email_register.setError("Invalid email address");
            return false;
        }
        else{
            email_register.setError(null);
            return true;
        }
    }
    private Boolean checkPhoneNumber (){
        String phone_check    = phone_register.getText().toString();
        if(phone_check.isEmpty()){
            phone_register.setError("Field cannot be empty");
            return false;
        }
        else if(phone_check.length() != 10){
            phone_register.setError("Must be 10 numbers");
            return false;
        }
        else{
            phone_register.setError(null);
            return true;
        }
    }


    public void Signup(View view){
        try {
            Dialog.show();
            //If input data is not correct
            if(!checkName() || !checkPassword() || !checkEmail() || !checkPhoneNumber()){
                Dialog.dismiss();
                return;
            }else{
                //Get all the value from the form
                String username_signup = username_register.getText().toString();
                String password_signup = password_register.getText().toString();
                String email_signup    = email_register.getText().toString();
                String phone_signup    = phone_register.getText().toString();
                User user = new User(username_signup,password_signup,email_signup,phone_signup);
                DatabaseReference database = FirebaseDatabase.getInstance().getReference("User");
                Query checkUser = database.orderByChild("mobile").equalTo(phone_signup);
                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            phone_register.setError("Phone account already exists !");
                            phone_register.requestFocus();
                            AlertDialog.Builder alert = new AlertDialog.Builder(SignUp.this);
                            alert.setTitle("Failed!!");
                            alert.setMessage("Phone account already exists ! You should change username");
                            alert.setNeutralButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                            Dialog.dismiss();
                            alert.show();
                        }else{
                            database.child(phone_signup).setValue(user);
                            Toast.makeText(SignUp.this, "New user registered successfully!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignUp.this,EditProfile.class);
                            intent.putExtra("mobile",phone_signup);
                            intent.putExtra("email",email_signup);
                            Dialog.dismiss();
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.createUserWithEmailAndPassword(email_signup,password_signup).
                        addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Dialog.dismiss();
                                    Toast.makeText(SignUp.this, "New user registered successfully"
                                            , Toast.LENGTH_SHORT).show();
                                    FirebaseUser firebaseUser = auth.getCurrentUser();
                                    firebaseUser.sendEmailVerification();
                                    Dialog.dismiss();
                                }
                            }
                        });
            }
        }catch (IllegalStateException e){
            e.printStackTrace();
        }


    }
}