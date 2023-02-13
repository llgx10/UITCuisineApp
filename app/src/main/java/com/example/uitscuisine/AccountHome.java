package com.example.uitscuisine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieDrawable;
import com.amrdeveloper.lottiedialog.LottieDialog;
import com.bumptech.glide.Glide;
import com.example.uitscuisine.custom_textview.FragmentMyShoppingList;
import com.example.uitscuisine.custom_textview.PoppinsMediumTextView;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private String Logged_In_Username;
    private String Logged_In_Password;
    private String Logged_In_Mobile;
    private String Logged_In_Email;
    LottieDialog Dialog;
    PoppinsMediumTextView nav_name;
    CircleImageView imageViewCircle1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_home);

        Dialog = new LottieDialog(AccountHome.this)
                .setAnimation(R.raw.pan)
                .setAnimationRepeatCount(LottieDrawable.INFINITE)
                .setAutoPlayAnimation(true)
                .setMessage("Loading...")
                .setMessageTextSize(30)
                .setDialogBackground(Color.WHITE)
                .setAnimationViewHeight(400)
                .setCancelable(false);

        Intent intent = getIntent();
        Logged_In_Username = intent.getStringExtra("username");
        Logged_In_Password = intent.getStringExtra("password");
        Logged_In_Mobile   = intent.getStringExtra("mobile");
        Logged_In_Email    = intent.getStringExtra("email");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        nav_name = (PoppinsMediumTextView) navigationView.getHeaderView(0).findViewById(R.id.nav_name);
        imageViewCircle1 = (CircleImageView) navigationView.getHeaderView(0).findViewById(R.id.imageViewCircle1);
        login_check();
    }
    public String getUserName(){
        return Logged_In_Username;
    }
    public String getPassword(){
        return Logged_In_Password;
    }
    public String getMobile(){
        return Logged_In_Mobile;
    }
    public String getEmail(){
        return Logged_In_Email;
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
                            try {
                                String NameUser = object.getString("name");
                                String url = object.getJSONObject("picture").getJSONObject("data").getString("url");
                                nav_name.setText(NameUser);
                                Glide.with(AccountHome.this).load(url).into(imageViewCircle1);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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
                Intent intent1 = getIntent();
                String phone_check_login = intent1.getStringExtra("mobile");
                DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Profile");
                Query checkUser = database.orderByChild("phone").equalTo(phone_check_login);
                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            displayDataFromDB1();
                            Toast.makeText(AccountHome.this, "YOUR PROFILE", Toast.LENGTH_SHORT).show();
                        } else {
                            Dialog.dismiss();
                            AlertDialog.Builder alert = new AlertDialog.Builder(AccountHome.this);
                            alert.setTitle("Failed!!");
                            alert.setMessage("Something Wrong Your Profile Information, Please fill in your profile fully");
                            alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(AccountHome.this, Login.class);
                                    startActivity(intent);
                                }
                            });
                            alert.show();
                            Toast.makeText(AccountHome.this, "YOU MUST CREATE PROFILE INFORMATION", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AccountHome.this, Login.class);
                            startActivity(intent);
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
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    boolean foundUser = false;
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        String phoneFromDB = dataSnapshot.child("phone").getValue().toString();
                        if(phone_check_login.equals(phoneFromDB)){
                            String nameFromDB = dataSnapshot.child("name").getValue().toString();
                            String imageFromDB = dataSnapshot.child("imageURl").getValue().toString();
                            nav_name.setText(nameFromDB);
                            Glide.with(AccountHome.this).load(imageFromDB).into(imageViewCircle1);
                            Dialog.dismiss();
                        }
                    }
                    if(foundUser == true){
                        AlertDialog.Builder alert = new AlertDialog.Builder(AccountHome.this);
                        alert.setTitle("Failed!!");
                        alert.setMessage("You haven't registered profile information");
                        alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(AccountHome.this, Login.class);
                                startActivity(intent);
                            }
                        });
                        alert.show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.account_home,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.homepage){
            Intent intent = new Intent(AccountHome.this,MainActivity.class);
            intent.putExtra("mobile",Logged_In_Mobile);
            intent.putExtra("username",Logged_In_Username);
            intent.putExtra("password",Logged_In_Password);
            intent.putExtra("email",Logged_In_Email);
            startActivity(intent);
        }else if(item.getItemId()==R.id.homepage2){
            LoginManager.getInstance().logOut();
            Intent intent = new Intent(AccountHome.this,Login.class);
            Toast.makeText(this, "LOG OUT", Toast.LENGTH_SHORT).show();
            startActivity(intent);
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FragmentUser()).commit();
                break;
            case R.id.nav_myfavorite:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FragmentMyFavorite()).commit();
                break;
            case R.id.nav_myprofile:
                Intent intent2 = new Intent(AccountHome.this,myProfile.class);
                intent2.putExtra("mobile",Logged_In_Mobile);
                intent2.putExtra("username",Logged_In_Username);
                intent2.putExtra("password",Logged_In_Password);
                intent2.putExtra("email",Logged_In_Email);
                Toast.makeText(this, "Now My Profile", Toast.LENGTH_SHORT).show();
                startActivity(intent2);
                break;
            case R.id.nav_editprofile:
                Intent intent1 = new Intent(AccountHome.this,EditProfile.class);
                intent1.putExtra("mobile",Logged_In_Mobile);
                intent1.putExtra("username",Logged_In_Username);
                intent1.putExtra("password",Logged_In_Password);
                intent1.putExtra("email",Logged_In_Email);
                Toast.makeText(this, "Move Edit Profile", Toast.LENGTH_SHORT).show();
                startActivity(intent1);
                break;
            case R.id.nav_aboutus:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FragmentAboutus()).commit();
                break;
            case R.id.nav_contactus:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FragmentContactus()).commit();
                break;
            case R.id.nav_termofservice:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FragmentTermofservice()).commit();
                break;
            case R.id.nav_logout:
                LoginManager.getInstance().logOut();
                Intent intent = new Intent(AccountHome.this,Login.class);
                Toast.makeText(this, "LOG OUT", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            //super.onBackPressed();
        } else {

            Intent intent = new Intent(AccountHome.this,MainActivity.class);
            intent.putExtra("mobile",Logged_In_Mobile);
            intent.putExtra("username",Logged_In_Username);
            intent.putExtra("password",Logged_In_Password);
            intent.putExtra("email",Logged_In_Email);
            startActivity(intent);
        }
    }
    //New Code Here
}