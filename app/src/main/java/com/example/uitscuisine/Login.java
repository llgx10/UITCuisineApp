package com.example.uitscuisine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.amrdeveloper.lottiedialog.LottieDialog;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.github.ybq.android.spinkit.style.RotatingPlane;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class Login extends AppCompatActivity {
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
    //Variables
    Animation topAmin , bottomAmin, randomAmin,bounceAmin,lefttorightAmin;
    ImageView logo;
    TextView welcome,forgot_pass,sign_up,dont;
    EditText username,password;
    Button login_bt;
    FloatingActionButton f_bt,g_bt;
    TextInputLayout hintPass;
    ProgressDialog Dialog;
    LottieDialog dialog;
    String userFromDB;
    String passFromDB;
    String phoneFromDB;
    String emailFromDB;
    CallbackManager callbackManager =null;
    AccessToken accessToken = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        //LoginManager.getInstance().logOut();
        //mapped animation
        lefttorightAmin = AnimationUtils.loadAnimation(this,R.anim.lefttoright);
        bounceAmin = AnimationUtils.loadAnimation(this,R.anim.bounce);
        topAmin = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAmin = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        randomAmin = AnimationUtils.loadAnimation(this,R.anim.random_animation);
        //mapped variables
        logo = (ImageView) findViewById(R.id.imageSplash);
        welcome = (TextView) findViewById(R.id.welcome);
        forgot_pass = (TextView) findViewById(R.id.forgot_pass);
        sign_up = (TextView)findViewById(R.id.sign_up);
        dont = (TextView)findViewById(R.id.dont);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        login_bt = (Button) findViewById(R.id.login_bt);
       f_bt    =(FloatingActionButton) findViewById(R.id.f_bt);
       g_bt = (FloatingActionButton) findViewById(R.id.g_bt);
        hintPass = (TextInputLayout) findViewById(R.id.hintPass);

        dialog = new LottieDialog(this)
                .setAnimation(R.raw.pan)
                .setAnimationRepeatCount(LottieDrawable.INFINITE)
                .setAutoPlayAnimation(true)
                .setMessage("LOADING...")
                .setMessageTextSize(30)
                .setDialogBackground(Color.WHITE)
                .setAnimationViewHeight(400)
                .setCancelable(false);


        //Set Animation for items
        sign_up.setAnimation(bottomAmin);
        dont.setAnimation(bottomAmin);
        login_bt.setAnimation(bottomAmin);
        f_bt.setAnimation(bottomAmin);
        g_bt.setAnimation(bottomAmin);
        hintPass.setAnimation(topAmin);
        welcome.setAnimation(topAmin);
        forgot_pass.setAnimation(topAmin);
        username.setAnimation(topAmin);
        password.setAnimation(topAmin);



        login_bt.setOnClickListener(this::login_handle);
        sign_up.setOnClickListener(this::signup_handle);
        forgot_pass.setOnClickListener(this::forgot_handle);

        f_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                callbackManager = CallbackManager.Factory.create();
                accessToken = AccessToken.getCurrentAccessToken();
                LoginManager.getInstance().registerCallback(callbackManager,
                        new FacebookCallback<LoginResult>() {

                            @Override
                            public void onSuccess(LoginResult loginResult) {
                                dialog.show();
                                Intent intent1 = new Intent(Login.this, MainActivity.class);
                                startActivity(intent1);
                                dialog.dismiss();
                            }

                            @Override
                            public void onCancel() {
                                accessToken = null;
                            }
                            @Override
                            public void onError(FacebookException exception) {
                                accessToken = null;
                            }
                        });
                LoginManager.getInstance().logInWithReadPermissions(Login.this, Arrays.asList("public_profile"));
               dialog.dismiss();
            }
        });
    }


    public void forgot_handle(View view){
      dialog.show();
        Toast.makeText(this, "Forget password", Toast.LENGTH_SHORT).show();
      Intent intent1 = new Intent(Login.this, ForgetPassword.class);
      startActivity(intent1);
      dialog.dismiss();


    }
    //Assistant functions to check for empty input, validating username and password
    public boolean sign_in_screen_validate_username(){
        String val = username.getText().toString();
        if(val.isEmpty()){
            username.setError("Field cannot be empty");
            username.requestFocus();
            return false;
        }
        else{
            username.setError(null);
            return true;
        }
    }

    public boolean sign_in_screen_validate_password(){
        String val = password.getText().toString();
        if(val.isEmpty()){
            password.setError("Field cannot be empty");
            password.requestFocus();
            return false;
        }
        else{
            password.setError(null);
            return true;
        }
    }
    public void login_handle(View view) {
        try {
            dialog.show();
            if (!sign_in_screen_validate_username() || !sign_in_screen_validate_password())
            {
                AlertDialog.Builder alert = new AlertDialog.Builder(Login.this);
                alert.setTitle("Failed!!");
                alert.setMessage("Please input your username and password");
                alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                alert.show();
                dialog.dismiss();
            }
            else{
                login_bt.startAnimation(bounceAmin);
                if (username.getText().toString().equals("uitcuisine") && password.getText().toString().equals("uit123456")) {
                    Toast.makeText(this, "LOGIN SUCCESSFULLY", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(Login.this, MainActivity.class);
                    startActivity(intent1);
                }
                String userEnteredUsername = username.getText().toString();
                String userEnteredPassword = password.getText().toString();
                DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("User");
                Query checkUser = database.orderByKey();
                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean foundUser = false;
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            username.setError(null);
                            userFromDB = dataSnapshot.child("username").getValue().toString();
                            passFromDB = dataSnapshot.child("password").getValue().toString();
                            phoneFromDB = dataSnapshot.child("mobile").getValue().toString();
                            emailFromDB = dataSnapshot.child("email").getValue().toString();
                            if(userEnteredUsername.equals(userFromDB)){
                                if(userEnteredPassword.equals(passFromDB)){
                                    username.setError(null);
                                    foundUser = true;
                                    //Toast.makeText(Login.this, "LOGIN SUCCESSFULLY", Toast.LENGTH_SHORT).show();
                                    Intent intent1 = new Intent(Login.this,MainActivity.class);
                                    intent1.putExtra("username",userFromDB);
                                    intent1.putExtra("password",passFromDB);
                                    intent1.putExtra("mobile",phoneFromDB);
                                    intent1.putExtra("email",emailFromDB);
                                    startActivity(intent1);
                                    dialog.dismiss();
                                }else{

                                    dialog.dismiss();
                                    Toast.makeText(Login.this, "LOGIN FAILED !!!", Toast.LENGTH_SHORT).show();
                                    password.setError("Wrong password");
                                    password.requestFocus();
                                }
                            }
                        }
                        if(!foundUser){
                            dialog.dismiss();
                            AlertDialog.Builder alert = new AlertDialog.Builder(Login.this);
                            alert.setTitle("Failed!!");
                            alert.setMessage("Account hasn't register ! You should change username");
                            alert.setNeutralButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                            alert.show();
                            username.setError("No such user exist !");
                            username.requestFocus();
                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signInWithEmailAndPassword(userEnteredUsername,userEnteredPassword)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            dialog.dismiss();
                            Toast.makeText(Login.this, "User logged in successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Login.this, MainActivity.class));
                        }else{
                            dialog.dismiss();
                            //Toast.makeText(Login.this, "Log in Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            }catch(IllegalStateException e){
                e.printStackTrace();
            }
    }
    public void signup_handle(View view){
        sign_up.startAnimation(bounceAmin);
        Toast.makeText(this, "Move to Sign Up", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Login.this,SignUp.class);
        startActivity(intent);
    }

    //On Resume in case the user wants to sign out right from the dashboard screen, so we clear the edittext for them
    //Or when they just returned from signing up their account
    @Override
    protected void onResume() {
        super.onResume();
        //Clear the edit text
        AccessToken accessToken = null;
        //LoginManager.getInstance().logOut();
        callbackManager = null;
        username.getText().clear();
        password.getText().clear();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}