package com.example.uitscuisine;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.airbnb.lottie.LottieDrawable;
import com.amrdeveloper.lottiedialog.LottieDialog;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
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
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import me.relex.circleindicator.CircleIndicator;

public class FragmentHome extends Fragment {
    private TextView searchBar, guest;
    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private ImageForSliderAdapter sliderAdapter;
    private List<ImageForSlider> imageList;
    private Timer timer;
    private EditText search;
    private ListView recipesList;
    private ArrayList<NewestRecipe> recipesArray = new ArrayList<>();
    private NewestRecipeAdapter recipeAdapter;
    private CircleImageView userAvatar;
    LottieDialog Dialog;
    private  Handler mHandler;
    private  PoppinsMediumTextView getting, viewAll;
    private  SearchFragment searchFragment;
    MainActivity mainActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mHandler = new Handler();
        guest = (TextView)view.findViewById(R.id.guest);
        userAvatar = (CircleImageView)view.findViewById(R.id.userAvatar);
        mainActivity = (MainActivity) getActivity();

        recipesList = (ListView) view.findViewById(R.id.recipesList);
        getting        = (PoppinsMediumTextView)view.findViewById(R.id.getting);
        viewAll        = (PoppinsMediumTextView)view.findViewById(R.id.viewAll);
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
                .setMessage("Loading...")
                .setMessageTextSize(30)
                .setDialogBackground(Color.WHITE)
                .setAnimationViewHeight(400)
                .setCancelable(false);

        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mainActivity, ViewAll.class);
                assert mainActivity != null;
                String phone_check_login = mainActivity.getMobile();
                intent.putExtra("mobile",phone_check_login);
                startActivity(intent);
                Animatoo.animateFade(mainActivity);
            }
        });
        displayData();
        homeSlider(view);
        recipesListListener();
        gotoSearch(view);

        return view;
    }
    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {

            mHandler.postDelayed(this,5000);
        }
    };
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
                Query checkUser2 = database2.limitToLast(3);

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

                checkUser2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            String strRecipeName = dataSnapshot.child("recipeName").getValue().toString();
                            String strDuration = dataSnapshot.child("duration").getValue().toString();
                            String strDifficulty = dataSnapshot.child("difficulty").getValue().toString();
                            String imageFromDB = dataSnapshot.child("recipeImage").getValue().toString();
                            String strPosterName = dataSnapshot.child("posterName").getValue().toString();
                            String imageFromDB2 = dataSnapshot.child("posterAvatar").getValue().toString();
                            String strRecipeId = dataSnapshot.child("recipeId").getValue().toString();
                            createRecipesList(strRecipeName, strDuration, strDifficulty, strPosterName, imageFromDB, imageFromDB2, strRecipeId);
                            Dialog.dismiss();
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

    public void homeSlider(View view) {
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        viewPager.setClipToOutline(true);
        circleIndicator = (CircleIndicator) view.findViewById(R.id.circleIndicator);

        imageList = createImageList();
        sliderAdapter = new ImageForSliderAdapter(getActivity(), imageList);
        viewPager.setAdapter(sliderAdapter);

        circleIndicator.setViewPager(viewPager);
        sliderAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());

        autoScroll();
    }

    public List<ImageForSlider> createImageList() {
        List<ImageForSlider> list = new ArrayList<>();
        list.add(new ImageForSlider(R.drawable.food1, "Japanese BBQ"));
        list.add(new ImageForSlider(R.drawable.food2, "Blueberry Ice Cream"));
        list.add(new ImageForSlider(R.drawable.food3, "Sashimi Salad"));
        list.add(new ImageForSlider(R.drawable.food4, "Chinese Meetball"));
        list.add(new ImageForSlider(R.drawable.food5, "Thai Hotpot"));
        list.add(new ImageForSlider(R.drawable.food6, "Korean Kimbap"));
        list.add(new ImageForSlider(R.drawable.food7,"Chicken Fire"));
        list.add(new ImageForSlider(R.drawable.food9,"Chicken fire honey"));
        list.add(new ImageForSlider(R.drawable.food10,"Mì Spaghetti"));
        return list;
    }

    public void autoScroll() {
        if (imageList == null || imageList.isEmpty() || viewPager == null) {
            return;
        }
        if (timer == null) {
            timer = new Timer();
        }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        int current = viewPager.getCurrentItem();
                        int size = imageList.size() - 1;
                        if (current < size) {
                            current++;
                            viewPager.setCurrentItem(current);
                        }
                        else {
                            viewPager.setCurrentItem(0);
                        }
                    }
                });
            }
        }, 4000, 4000);
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

    private void recipesListListener() {
        recipesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(mainActivity, RecipeDetails.class);
                TextView recipeId = (TextView) view.findViewById(R.id.recipeId);
                assert mainActivity != null;
                String phone_check_login = mainActivity.getMobile();
                intent.putExtra("recipeId", recipeId.getText());
                intent.putExtra("mobile",phone_check_login);
                startActivity(intent);
            }
        });
    }

    public void gotoSearch(View view){
        searchBar = (TextView) view.findViewById(R.id.searchBar);

        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment selectedFragment = new SearchFragment();
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fr = fragmentManager.beginTransaction();
                fr.replace(R.id.fragment_home,selectedFragment);
                fr.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                fr.addToBackStack(null);
                fr.commit();
            }
        });

    }
}
