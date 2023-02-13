package com.example.uitscuisine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieDrawable;
import com.amrdeveloper.lottiedialog.LottieDialog;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.uitscuisine.custom_textview.PoppinsBoldTextView;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ViewAll extends AppCompatActivity {
    private RecyclerView recyclerViewAll;
    private ViewAllAdapter viewAllAdapter;
    private ViewAllRecipe viewAllRecipe;
    private Context context;
    private ImageButton returnBack,reload;
    private PoppinsBoldTextView guest;
    private String Mobile;
    private String imageRecipe;
    private String nameRecipe;
    private String recipeID;
    private String neededTime;
    private String level;
    private String posterName;
    private String recipeCategory;
    private String posterAvatar;
    LottieDialog Dialog;
    private RecyclerView recipesList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);
        recipesList =(RecyclerView)findViewById(R.id.ViewALlPost);
        returnBack = (ImageButton) findViewById(R.id.returnBack);
        guest      = (PoppinsBoldTextView) findViewById(R.id.guest);
        reload     = (ImageButton)findViewById(R.id.reload);
        //Get intent
        Intent intent = getIntent();
        Mobile   = intent.getStringExtra("mobile");

        Dialog = new LottieDialog(ViewAll.this)
                .setAnimation(R.raw.pan)
                .setAnimationRepeatCount(LottieDrawable.INFINITE)
                .setAutoPlayAnimation(true)
                .setMessage("Loading...")
                .setMessageTextSize(30)
                .setDialogBackground(Color.WHITE)
                .setAnimationViewHeight(400)
                .setCancelable(false);
        returnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animatoo.animateSlideLeft(ViewAll.this);
                finish();
            }
        });


        setLayout();
        login_check();
        displayDataFromDB();
    }
    public void setLayout(){
        recyclerViewAll = (RecyclerView)findViewById(R.id.ViewALlPost);
        viewAllAdapter = new ViewAllAdapter(this);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(ViewAll.this,
                RecyclerView.VERTICAL,
                false);
        recyclerViewAll.setLayoutManager(linearLayoutManager);
        viewAllAdapter.setData(getDataView());
        recyclerViewAll.setAdapter(viewAllAdapter);
        viewAllAdapter.notifyDataSetChanged();

        recyclerViewAll.setVisibility(View.GONE);
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerViewAll.setVisibility(View.VISIBLE);
                Toast.makeText(ViewAll.this, "Successfully", Toast.LENGTH_SHORT).show();
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                reload.performClick();
            }
        }, 3000);

    }
    public List<ViewAllRecipe>getDataView(){
        List<ViewAllRecipe> list = new ArrayList<>();
        Dialog.show();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Posts");
        Query checkUser2 = reference.orderByKey();
        checkUser2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    nameRecipe  = dataSnapshot.child("recipeName").getValue().toString();
                    recipeID    = dataSnapshot.child("recipeId").getValue().toString();
                    neededTime  = dataSnapshot.child("duration").getValue().toString();
                    level       = dataSnapshot.child("difficulty").getValue().toString();
                    posterName  = dataSnapshot.child("posterName").getValue().toString();
                    Uri image = Uri.parse(dataSnapshot.child("recipeImage").getValue().toString());
                    Uri post = Uri.parse(dataSnapshot.child("posterAvatar").getValue().toString());
                    String category= dataSnapshot.child("category").getValue().toString();
                    list.add(0,new ViewAllRecipe(neededTime,level,nameRecipe,posterName
                            ,recipeID,category,image,post,Mobile));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Collections.shuffle(list);
        Dialog.dismiss();
        return  list;
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
    public void displayDataFromDB() {
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
                                guest.setText("Hello, "+NameUser);
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
                DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Profile");
                Query checkUser = database.orderByKey();
                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean foundUser = false;
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            String phoneFromDB = dataSnapshot.child("phone").getValue().toString();
                            if (Mobile.equals(phoneFromDB)) {
                                String nameFromDB = dataSnapshot.child("name").getValue().toString();
                                guest.setText("Hello, "+nameFromDB);
                                Dialog.dismiss();
                            }
                        }
                        if (foundUser == true) {
                            Toast.makeText(ViewAll.this, "Error", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Animatoo.animateSlideLeft(this); //fire the slide left animation
    }
}