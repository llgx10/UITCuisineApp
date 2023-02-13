package com.example.uitscuisine;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telecom.TelecomManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.airbnb.lottie.LottieDrawable;
import com.amrdeveloper.lottiedialog.LottieDialog;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import me.relex.circleindicator.CircleIndicator;

public class FragmentMenuPlaner extends Fragment {

    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private ImageForSliderAdapter sliderAdapter;
    private List<ImageForSlider> imageList;
    private Timer timer;
    private Button shuffle_btn;
    private ListView recipesList;
    private ArrayList<NewestRecipe> recipesArray = new ArrayList<>();
    private NewestRecipeAdapter recipeAdapter;
    private  PoppinsMediumTextView getting;
    private View footerView;
    private boolean isLoading = false;
//    private myHandler handler;
    private int currentPage = 1, stopPage = 2;
    private long backPressedTime;
    private Toast backToast;
    private CircleImageView userAvatar;
    private TextView guest;
    LottieDialog Dialog;
    MainActivity mainActivity;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_planer, container, false);
        guest = (TextView)view.findViewById(R.id.guest);
        userAvatar = (CircleImageView)view.findViewById(R.id.userAvatar);
        mainActivity = (MainActivity) getActivity();
        shuffle_btn  = (Button)view.findViewById(R.id.shuffle_btn);
        recipesList = (ListView) view.findViewById(R.id.recipesList);
        getting        = (PoppinsMediumTextView)view.findViewById(R.id.getting);
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

        recipesListListener();
        shuffleRecipeListListener();
        return view;
    }
    private void displayData() {
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
                                Glide.with(mainActivity).load(url).into(userAvatar);
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
                assert mainActivity != null;
                String userName_check_login = mainActivity.getUserName();
                String password_check_login = mainActivity.getPassword();
                String phone_check_login = mainActivity.getMobile();
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
                                Glide.with(mainActivity).load(imageFromDB).into(userAvatar);
                                Dialog.dismiss();
                            }
                        }
                        if (foundUser == true) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(mainActivity);
                            alert.setTitle("Failed!!");
                            alert.setMessage("You haven't registered profile information");
                            alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(mainActivity, Login.class);
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
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void createRecipesList(String strRecipeName, String strDuration, String strDifficulty, String strPosterName, String imageFromDB, String imageFromDB2, String strRecipeId) {
        recipesArray.add(new NewestRecipe(strDuration, strDifficulty, strRecipeName, strPosterName, imageFromDB, imageFromDB2, strRecipeId));
        recipeAdapter = new NewestRecipeAdapter(getActivity(), R.layout.newest_recipe, recipesArray);
        recipesList.setAdapter(recipeAdapter);
    }
    public void recipesListListener() {
        recipesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(mainActivity, RecipeDetails.class);
                assert mainActivity != null;
                String phone_check_login = mainActivity.getMobile();
                TextView recipeId = (TextView) view.findViewById(R.id.recipeId);
                intent.putExtra("recipeId", recipeId.getText());
                intent.putExtra("mobile",phone_check_login);
                startActivity(intent);
            }
        });
    }
    public void shuffleRecipeListListener() {
        shuffle_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog.show();
                recipesArray.clear();
                DatabaseReference database2 = FirebaseDatabase.getInstance().getReference().child("Posts");
                Query checkUser2 = database2.orderByKey();
                checkUser2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Random random = new Random();
                        int index1 = 0;
                        int index2 = 0;
                        int index3 = 0;
                        int total  =(int)snapshot.getChildrenCount();
                        while (true){
                            index1 = random.nextInt(total);
                            index2 = random.nextInt(total);
                            index3 = random.nextInt(total);
                            if(index1 != index2 && index3 != index2 && index1 != index3){
                                break;
                            }else{
                                continue;
                            }
                        }
                        int count = 0;
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            if (count == index1 || count == index2 || count == index3) {
                                String strRecipeName = dataSnapshot.child("recipeName").getValue().toString();
                                String strDuration = dataSnapshot.child("duration").getValue().toString();
                                String strDifficulty = dataSnapshot.child("difficulty").getValue().toString();
                                String imageFromDB = dataSnapshot.child("recipeImage").getValue().toString();
                                String strPosterName = dataSnapshot.child("posterName").getValue().toString();
                                String imageFromDB2 = dataSnapshot.child("posterAvatar").getValue().toString();
                                String strRecipeId = dataSnapshot.child("recipeId").getValue().toString();
                                createRecipesList(strRecipeName, strDuration, strDifficulty, strPosterName, imageFromDB, imageFromDB2, strRecipeId);
                                Dialog.dismiss();
                            }count++;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                Toast.makeText(mainActivity, "Shuffle Recipe", Toast.LENGTH_SHORT).show();
            }
        });
    }
}