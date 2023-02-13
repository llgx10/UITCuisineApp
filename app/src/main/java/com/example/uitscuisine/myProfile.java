package com.example.uitscuisine;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.airbnb.lottie.LottieDrawable;
import com.amrdeveloper.lottiedialog.LottieDialog;
import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.android.gms.cast.framework.media.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class myProfile extends AppCompatActivity {
    Animation left_animation;
    TextView textName,textPhone, textEmail, textAddress,textBirthday,textGender,textWeight,textHeight, textNameProfile ;
    LinearLayout layoutName,layoutPhone,layoutEmail, layoutBirth, layoutAddress,layoutGender;
    LinearLayout layoutWeight,layoutHeight;
    CircleImageView imageViewCircle;
    Button EditProfileButton;
    ImageView ivReturn;
    LottieDialog Dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        Dialog = new LottieDialog(myProfile.this)
                .setAnimation(R.raw.pan)
                .setAnimationRepeatCount(LottieDrawable.INFINITE)
                .setAutoPlayAnimation(true)
                .setMessage("Profile Loading...")
                .setMessageTextSize(30)
                .setDialogBackground(Color.WHITE)
                .setAnimationViewHeight(400)
                .setCancelable(false);



        imageViewCircle = (CircleImageView)findViewById(R.id.imageViewCircle);
        EditProfileButton     = (Button)findViewById(R.id.EditProfile);
        ivReturn        = (ImageView) findViewById(R.id.ivReturn);
        textNameProfile = (TextView)findViewById(R.id.textNameProfile);
        textName        = (TextView)findViewById(R.id.textName);
        textPhone       = (TextView)findViewById(R.id.textPhone);
        textEmail       = (TextView)findViewById(R.id.textEmail);
        textAddress     = (TextView)findViewById(R.id.textAddress);
        textBirthday    = (TextView)findViewById(R.id.textBirthday);
        textGender      = (TextView)findViewById(R.id.textGender);
        textWeight      = (TextView)findViewById(R.id.textWeight);
        textHeight      = (TextView)findViewById(R.id.textHeight);
        layoutName = (LinearLayout)findViewById(R.id.layoutName);
        layoutPhone = (LinearLayout)findViewById(R.id.layoutPhone);
        layoutEmail = (LinearLayout)findViewById(R.id.layoutEmail);
        layoutBirth = (LinearLayout)findViewById(R.id.layoutBirth);
        layoutGender= (LinearLayout)findViewById(R.id.layoutGender);
        layoutAddress = (LinearLayout)findViewById(R.id.layoutAddress);
        layoutWeight = (LinearLayout)findViewById(R.id.layoutWeight);
        layoutHeight= (LinearLayout)findViewById(R.id.layoutHeight);
        left_animation = AnimationUtils.loadAnimation(this,R.anim.left_animation);
        layoutName.setAnimation(left_animation);
        layoutPhone.setAnimation(left_animation);
        layoutEmail.setAnimation(left_animation);
        layoutAddress.setAnimation(left_animation);
        layoutBirth.setAnimation(left_animation);
        layoutHeight.setAnimation(left_animation);
        layoutWeight.setAnimation(left_animation);
        layoutGender.setAnimation(left_animation);


        ivReturn.setOnClickListener(this::ivReturnMain);
        login_check();

    }




    public void ivReturnMain(View view){
        Intent intent = getIntent();
        String phone_check_login = intent.getStringExtra("mobile");
        String email_check_login = intent.getStringExtra("email");
        Intent intent1 = new Intent(myProfile.this,MainActivity.class);
        intent1.putExtra("mobile",phone_check_login);
        intent1.putExtra("email",email_check_login);
        startActivity(intent1);
    }

    public void login_check() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {

            GraphRequest request = GraphRequest.newMeRequest(
                    accessToken,
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {
                            // Application code
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,link,picture.type(large)");
            request.setParameters(parameters);
            request.executeAsync();
        } else {
            try {
                Dialog.show();
                Intent intent = getIntent();
                String phone_check_login = intent.getStringExtra("mobile");
                //phoneEditProfile.setText(phone_check_login);
                DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Profile");
                Query checkUser = database.orderByChild("phone").equalTo(phone_check_login);
                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            displayDataFromDB1();
                            Toast.makeText(myProfile.this, "YOUR PROFILE", Toast.LENGTH_SHORT).show();
                        } else {

                            Dialog.dismiss();
                            Toast.makeText(myProfile.this, "Welcome to your profile", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }
    public void displayDataFromDB1(){
        try {
            Dialog.show();
            Intent intent = getIntent();
            String phone_check_login = intent.getStringExtra("mobile");
            String email_check_login = intent.getStringExtra("email");
            DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Profile");
            Query checkUser = database.orderByKey();
            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    boolean foundUser = false;
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        textPhone.setError(null);
                        String phoneFromDB = dataSnapshot.child("phone").getValue().toString();
                        if(phone_check_login.equals(phoneFromDB)) {
                            String nameFromDB = dataSnapshot.child("name").getValue().toString();
                            String addressFromDB = dataSnapshot.child("address").getValue().toString();
                            String birthdayFromDB = dataSnapshot.child("birthday").getValue().toString();
                            String genderFromDB = dataSnapshot.child("gender").getValue().toString();
                            String imageFromDB = dataSnapshot.child("imageURl").getValue().toString();
                            String weightFromDB = dataSnapshot.child("weight").getValue().toString();
                            String heightFromDB = dataSnapshot.child("height").getValue().toString();
                            textNameProfile.setText(nameFromDB);
                            textName.setText(nameFromDB);
                            textAddress.setText(addressFromDB);
                            textBirthday.setText(birthdayFromDB);
                            textGender.setText(genderFromDB);
                            textWeight.setText(weightFromDB);
                            textHeight.setText(heightFromDB);
                            textPhone.setText(phone_check_login);
                            textEmail.setText(email_check_login);
                            Glide.with(myProfile.this).load(imageFromDB).into(imageViewCircle);
                            Dialog.dismiss();
                        }
                    }

                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }catch(IllegalStateException e){
            e.printStackTrace();
        }
    }
}