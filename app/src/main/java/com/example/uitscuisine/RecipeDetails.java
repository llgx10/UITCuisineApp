package com.example.uitscuisine;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieDrawable;
import com.amrdeveloper.lottiedialog.LottieDialog;
import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class RecipeDetails extends AppCompatActivity {
    ListView ingredientsList;
    ArrayList<Ingredient> ingredientsArray = new ArrayList<>();
    IngredientAdapter ingredientAdapter;
    ListView stepsList;
    ArrayList<Step> stepsArray = new ArrayList<>();
    StepAdapter stepAdapter;
    FloatingActionButton returnBack;
    LottieDialog Dialog;
    TextView recipeName, serves, duration, difficulty, posterName;
    ImageView recipeImage, posterAvatar;
    ImageView heartButton1, heartButton2;
    TextView like_text;
    String  strRecipeNameFDB,strDifficultyFDB, strDurationFDB;
    String imageFDB;
    String strPosterNameFDB;
    String imageFDB2;
    String strRecipeIdFDB;
    Button addFavouriteButton;
    String phone_check_login2;
    String recipeCheck;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_recipe_details);

        Dialog = new LottieDialog(RecipeDetails.this)
                .setAnimation(R.raw.pan)
                .setAnimationRepeatCount(LottieDrawable.INFINITE)
                .setAutoPlayAnimation(true)
                .setMessage("Profile loading...")
                .setMessageTextSize(30)
                .setDialogBackground(Color.WHITE)
                .setAnimationViewHeight(400)
                .setCancelable(false);

        mapping();
        displayDataFromDB();
        returnHomeListener();
        hearButtonHandle();
        //checkLike();
        AddFavouriteHandler();
    }

    private void createIngredientsList(String strIngredients) {
        ingredientsList = (ListView) findViewById(R.id.ingredientsList);

        String concatStr = "";
        for (int i=0; i<strIngredients.length(); i++) {
            if (i == strIngredients.length()-1) {
                ingredientsArray.add(new Ingredient(concatStr));
            }else {
                if (strIngredients.charAt(i) == '\n') {
                    ingredientsArray.add(new Ingredient(concatStr));
                    concatStr = "";
                } else {
                    concatStr += strIngredients.charAt(i);
                }
            }
        }

        ingredientAdapter = new IngredientAdapter(RecipeDetails.this, R.layout.ingredients_list, ingredientsArray);
        ingredientsList.setAdapter(ingredientAdapter);
    }

    private void createStepsList(String strSteps) {
        stepsList = (ListView) findViewById(R.id.stepsList);

        String concatStr = "";
        int numStep = 1;
        for (int i=0; i<strSteps.length(); i++) {
            if (i == strSteps.length()-1) {
                stepsArray.add(new Step(concatStr, numStep));
            }else{
                if (strSteps.charAt(i) == '\n') {
                    stepsArray.add(new Step(concatStr, numStep));
                    concatStr = "";
                    numStep += 1;
                } else {
                    concatStr += strSteps.charAt(i);
                }
            }
        }

        stepAdapter = new StepAdapter(RecipeDetails.this, R.layout.steps_list, stepsArray);
        stepsList.setAdapter(stepAdapter);
    }

    public void returnHomeListener() {
        returnBack = (FloatingActionButton) findViewById(R.id.returnBack);
        returnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(RecipeDetails.this, MainActivity.class);
//                startActivity(intent);
                finish();
            }
        });
    }

    private void mapping() {
        recipeName = (TextView) findViewById(R.id.bigTitle);
        serves = (TextView) findViewById(R.id.servesContent);
        duration = (TextView) findViewById(R.id.durationContent);
        difficulty = (TextView) findViewById(R.id.difficultyContent);
        //category = (EditText) findViewById(R.id.postCategoryEdt);
        recipeImage = (ImageView) findViewById(R.id.recipeImage);
        posterName = (TextView) findViewById(R.id.detailsPosterName);
        posterAvatar = (ImageView) findViewById(R.id.detailsPosterAvatar);
        heartButton1 = (ImageView) findViewById(R.id.heartButton1);
        heartButton2 = (ImageView) findViewById(R.id.heartButton2);
        like_text    = (TextView) findViewById(R.id.like_text);
        addFavouriteButton = (Button) findViewById(R.id.addFavouriteButton);
    }

    private void checkPhone(){
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
        }else {
            try {
                Dialog.show();
                Intent intent = getIntent();
                String phone_check_login = intent.getStringExtra("mobile");
                DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Posts");
                Query checkUser = database.orderByChild("posterPhone").equalTo(phone_check_login);
                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    private void displayDataFromDB(){
        try {
            Dialog.show();
            Intent intent = getIntent();
            String recipeId_check = intent.getStringExtra("recipeId");
            DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Posts");
            Query checkUser = database.orderByKey();
            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String recipeIdFromDB = dataSnapshot.child("recipeId").getValue().toString();
                        if (recipeId_check.equals(recipeIdFromDB)) {
                            String recipeIdFromDB2 = dataSnapshot.child("recipeId").getValue().toString();
                            strRecipeIdFDB = recipeIdFromDB2;
                            String strRecipeName = dataSnapshot.child("recipeName").getValue().toString();
                            strRecipeNameFDB = strRecipeName;
                            String strServes = dataSnapshot.child("serves").getValue().toString();
                            String strDuration = dataSnapshot.child("duration").getValue().toString();
                            strDurationFDB = strDuration;
                            String strDifficulty = dataSnapshot.child("difficulty").getValue().toString();
                            strDifficultyFDB = strDifficulty;
                            String strIngredients = dataSnapshot.child("ingredients").getValue().toString();
                            String strSteps = dataSnapshot.child("steps").getValue().toString();
                            Uri imageFromDB = Uri.parse(dataSnapshot.child("recipeImage").getValue().toString());
                            imageFDB = String.valueOf(imageFromDB);
                            String strPosterName = dataSnapshot.child("posterName").getValue().toString();
                            strPosterNameFDB = strPosterName;
                            Uri imageFromDB2 = Uri.parse(dataSnapshot.child("posterAvatar").getValue().toString());
                            imageFDB2 = String.valueOf(imageFromDB2);
                            recipeName.setText(strRecipeName);
                            serves.setText(strServes);
                            duration.setText(strDuration);
                            difficulty.setText(strDifficulty);
                            createIngredientsList(strIngredients);
                            createStepsList(strSteps);
                            //recipeImage.setImageURI(imageFromDB);
                            Glide.with(RecipeDetails.this).load(imageFromDB).into(recipeImage);
                            posterName.setText(strPosterName);
                            Glide.with(RecipeDetails.this).load(imageFromDB2).into(posterAvatar);
                            Dialog.dismiss();
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) { }
            });
        }catch(IllegalStateException e){
            e.printStackTrace();
        }
    }
    int like = (int) Math.floor(((Math.random() * 899999) + 100000));
    int likeCount = 0;
    public void checkLike(){
        Intent intent = getIntent();
        String recipeId_check = intent.getStringExtra("recipeId");
        String phone_check_login = intent.getStringExtra("mobile");
        DatabaseReference database = FirebaseDatabase.getInstance()
                .getReference("Posts")
                .child("Recipe ID: "+ recipeId_check).child("like");
        Query check2 = database.orderByKey();
        check2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    like_text.setText("Like: "+snapshot.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void hearButtonHandle(){
        heartButton1.setVisibility(View.VISIBLE);
        heartButton2.setVisibility(View.GONE);
        Intent intent = getIntent();
        String recipeId_check = intent.getStringExtra("recipeId");
        String phone_check_login = intent.getStringExtra("mobile");
        phone_check_login2 = phone_check_login;
        recipeCheck = recipeId_check;
        DatabaseReference database = FirebaseDatabase.getInstance()
                .getReference("Posts")
                .child("Recipe ID: "+ recipeId_check).child("like");
        Query check = database.orderByKey().equalTo(phone_check_login);
        Query check2 = database.orderByKey();
        check.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    heartButton2.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        check2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    like_text.setText("Like: "+snapshot.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        heartButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                DatabaseReference database = FirebaseDatabase.getInstance()
                        .getReference("Posts")
                        .child("Recipe ID: "+ recipeId_check).child("like");
                Query check = database.orderByKey().equalTo(phone_check_login);
                check.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                database.child(phone_check_login).child("phoneNumber").setValue(phone_check_login);
                                heartButton2.setVisibility(View.VISIBLE);
                                checkLike();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }
    public void AddFavouriteHandler(){
        addFavouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference database3 = FirebaseDatabase.getInstance().getReference("User")
                        .child(phone_check_login2).child("MyFavourite");
                Query query = database3.orderByKey();
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        FavouriteRecipe favouriteRecipe = new FavouriteRecipe(strDurationFDB,strDifficultyFDB,
                                strRecipeNameFDB,strPosterNameFDB,imageFDB,imageFDB2,strRecipeIdFDB);
                        database3.child(strRecipeIdFDB).setValue(favouriteRecipe);
                        Toast.makeText(RecipeDetails.this, "Add Favourite Recipe Successfully", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}