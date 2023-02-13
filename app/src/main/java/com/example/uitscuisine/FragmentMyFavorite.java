package com.example.uitscuisine;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieDrawable;
import com.amrdeveloper.lottiedialog.LottieDialog;
import com.bumptech.glide.Glide;
import com.example.uitscuisine.custom_textview.PoppinsMediumTextView;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentMyFavorite extends Fragment {
    private CircleImageView userAvatar;
    private TextView guest;
    LottieDialog Dialog;
    AccountHome mAccountHome;
    private PoppinsMediumTextView getting;
    private ListView favouriteRecipesListView;
    private ArrayList<FavouriteRecipe> favouriteRecipesDataArrayList = new ArrayList<>();
    private FavouriteRecipeAdapter favouriteRecipeAdapter;
    String phone_getDataFDB;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myfavorite, container, false);
        guest = (TextView)view.findViewById(R.id.guest);
        userAvatar = (CircleImageView)view.findViewById(R.id.userAvatar);
        mAccountHome = (AccountHome) getActivity();
        getting        = (PoppinsMediumTextView)view.findViewById(R.id.getting);
        favouriteRecipesListView = (ListView) view.findViewById(R.id.recipesList);
        Calendar calendar = Calendar.getInstance();
        int jam = calendar.get(Calendar.HOUR_OF_DAY);
        if(jam >= 0 && jam <16){
            getting.setText("Good Morning");
        }else if(jam >=12 && jam <16){
            getting.setText("Good Afternoon");
        }else if(jam >=16 && jam <21){
            getting.setText("Good Evening");
        }else if(jam >=21 && jam <24){
            getting.setText("Good Night");
        }else {
            getting.setText("UIT's Cuisine Hello");
        }
        Dialog = new LottieDialog(getActivity())
                .setAnimation(R.raw.pan)
                .setAnimationRepeatCount(LottieDrawable.INFINITE)
                .setAutoPlayAnimation(true)
                .setMessage("Menu Planner Loading...")
                .setMessageTextSize(30)
                .setDialogBackground(Color.WHITE)
                .setAnimationViewHeight(400)
                .setCancelable(false);
        displayData();
        GetDataFromDB();
        recipesListListener();
        return view;

    }
    private void AddRecipeFavourite(String strDurationFDB, String strDifficultyFDB,
                                    String strRecipeNameFDB,String strPosterNameFDB, String imageFDB,
                                    String imageFDB2,String strRecipeIdFDB){
        favouriteRecipesDataArrayList.add(new FavouriteRecipe(strDurationFDB,strDifficultyFDB,strRecipeNameFDB,
                                            strPosterNameFDB,imageFDB,imageFDB2,strRecipeIdFDB));
        favouriteRecipeAdapter = new FavouriteRecipeAdapter(getActivity(),R.layout.item_favourite,favouriteRecipesDataArrayList);
        favouriteRecipesListView.setAdapter(favouriteRecipeAdapter);

    }
    public void GetDataFromDB(){
        Dialog.show();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User")
                .child(phone_getDataFDB).child("MyFavourite");
        Query query = databaseReference.orderByKey();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot allFavourite) {
                for (DataSnapshot oneFavourite: allFavourite.getChildren()){
                    String strRecipeName = oneFavourite.child("titleText").getValue().toString();
                    String strDuration = oneFavourite.child("neededTime").getValue().toString();
                    String strDifficulty = oneFavourite.child("level").getValue().toString();
                    String imageFromDB = oneFavourite.child("titleImage").getValue().toString();
                    String strPosterName = oneFavourite.child("posterName").getValue().toString();
                    String imageFromDB2 = oneFavourite.child("posterAvatar").getValue().toString();
                    String strRecipeId = oneFavourite.child("recipeId").getValue().toString();
                    AddRecipeFavourite(strDuration,strDifficulty,strRecipeName,strPosterName,imageFromDB,imageFromDB2,strRecipeId);
                    Dialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void displayData() {
        Dialog.show();
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
                                guest.setText(NameUser);
                                Glide.with(mAccountHome).load(url).into(userAvatar);
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
        }
        else{
            try {
                Dialog.show();
                assert mAccountHome != null;
                String userName_check_login = mAccountHome.getUserName();
                String password_check_login = mAccountHome.getPassword();
                String phone_check_login = mAccountHome.getMobile();
                phone_getDataFDB = phone_check_login;
                DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Profile");
                DatabaseReference database2 = FirebaseDatabase.getInstance().getReference().child("Posts");
                Query checkUser = database.orderByKey();
                Query checkUser2 = database2.orderByKey();

                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean foundUser = false;
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            String phoneFromDB = dataSnapshot.child("phone").getValue().toString();
                            if (phone_check_login.equals(phoneFromDB)) {
                                String nameFromDB = dataSnapshot.child("name").getValue().toString();
                                String imageFromDB = dataSnapshot.child("imageURl").getValue().toString();
                                guest.setText(nameFromDB);
                                Glide.with(mAccountHome).load(imageFromDB).into(userAvatar);
                                Dialog.dismiss();
                            }
                        }
                        if (foundUser == true) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(mAccountHome);
                            alert.setTitle("Failed!!");
                            alert.setMessage("You haven't registered profile information");
                            alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(mAccountHome, Login.class);
                                    startActivity(intent);
                                }
                            });
                            alert.show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });

            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
        Dialog.dismiss();
    }
    public void recipesListListener() {
        favouriteRecipesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(mAccountHome, RecipeDetails.class);
                assert mAccountHome != null;
                String phone_check_login = mAccountHome.getMobile();
                TextView recipeId = (TextView) view.findViewById(R.id.recipeId);
                intent.putExtra("recipeId", recipeId.getText());
                intent.putExtra("mobile",phone_check_login);
                startActivity(intent);
            }
        });
    }
}
