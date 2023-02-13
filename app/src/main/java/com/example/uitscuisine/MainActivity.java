package com.example.uitscuisine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;


import com.airbnb.lottie.LottieDrawable;
import com.amrdeveloper.lottiedialog.LottieDialog;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class MainActivity extends AppCompatActivity {

    private AHBottomNavigation bottomNav;
    private ViewPager2 changeScreen;
    private long backPressedTime;
    private Toast backToast;
    private String Logged_In_Username;
    private String Logged_In_Password;
    private String Logged_In_Mobile;
    private String Logged_In_Email;

    LottieDialog Dialog;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);


        //Get intent
        Intent intent = getIntent();
        Logged_In_Username = intent.getStringExtra("username");
        Logged_In_Password = intent.getStringExtra("password");
        Logged_In_Mobile = intent.getStringExtra("mobile");
        Logged_In_Email = intent.getStringExtra("email");
        createChangeScreen();
        createBottomNav();

        Dialog = new LottieDialog(MainActivity.this)
                .setAnimation(R.raw.pan)
                .setAnimationRepeatCount(LottieDrawable.INFINITE)
                .setAutoPlayAnimation(true)
                .setMessage("Loading...")
                .setMessageTextSize(30)
                .setDialogBackground(Color.WHITE)
                .setAnimationViewHeight(400)
                .setCancelable(false);

        login_check();
    }

    public String getUserName() {
        return Logged_In_Username;
    }

    public String getPassword() {
        return Logged_In_Password;
    }

    public String getMobile() {
        return Logged_In_Mobile;
    }

    public String getEmail() {
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
                DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("User");
                Query checkUser = database.orderByKey();
                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            String phoneFromDB = dataSnapshot.child("mobile").getValue().toString();
                            if (phone_check_login.equals(phoneFromDB)) {

                                Toast.makeText(MainActivity.this, "Login successfully", Toast.LENGTH_SHORT).show();
                                Dialog.dismiss();
                            }
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

    private void createBottomNav() {
        bottomNav = (AHBottomNavigation) findViewById(R.id.bottomNav);

        bottomNav.setForceTint(true);

        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.action_home, R.drawable.home24, R.color.myOrange);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.action_add_recipe, R.drawable.add24, R.color.myOrange);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.action_planner, R.drawable.ic_fastfood, R.color.myOrange);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(R.string.action_shopping, R.drawable.ic_shopping_cart, R.color.myOrange);
        AHBottomNavigationItem item5 = new AHBottomNavigationItem(R.string.action_account, R.drawable.user24, R.color.myOrange);
        //AHBottomNavigationItem item6 = new AHBottomNavigationItem(R.string.action_account, R.drawable.user24, R.color.myOrange);
        //AHBottomNavigationItem item6 = new AHBottomNavigationItem(R.string.action_setting, R.drawable.ic_baseline_settings_24, R.color.myOrange);

        bottomNav.addItem(item1);
        bottomNav.addItem(item2);
        bottomNav.addItem(item3);
        bottomNav.addItem(item4);
        bottomNav.addItem(item5);
        // bottomNav.addItem(item6);
        // bottomNav.addItem(item6);

        bottomNav.setColored(false);
        bottomNav.setDefaultBackgroundColor(getResources().getColor(R.color.white));
        bottomNav.setAccentColor(getResources().getColor(R.color.myOrange));
        bottomNav.setInactiveColor(getResources().getColor(R.color.black));

        bottomNav.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                switch (position) {
                    case 0:
                        changeScreen.setCurrentItem(0);
                        break;
                    case 1:
                        changeScreen.setCurrentItem(1);
                        break;
                    case 2:
                        changeScreen.setCurrentItem(2);
                        break;
                    case 3:
                        changeScreen.setCurrentItem(3);
                        break;
                    case 4:
                        Intent intent = new Intent(MainActivity.this, AccountHome.class);
                        intent.putExtra("mobile", Logged_In_Mobile);
                        intent.putExtra("username", Logged_In_Username);
                        intent.putExtra("email", Logged_In_Email);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });
        bottomNav.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
            @Override
            public void onPositionChange(int y) {
                // Manage the new y position
            }
        });
    }

    private void createChangeScreen() {
        changeScreen = (ViewPager2) findViewById(R.id.changeScreen);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        changeScreen.setAdapter(viewPagerAdapter);
    }

    @Override
    public void onBackPressed() {

        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
//            moveTaskToBack(true);
//            android.os.Process.killProcess(android.os.Process.myPid());
//            System.exit(1);
        } else {
            backToast = Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }


}