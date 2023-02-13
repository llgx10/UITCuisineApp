package com.example.uitscuisine;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Trace;
import android.service.autofill.LuhnChecksumValidator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import de.hdodenhof.circleimageview.CircleImageView;


public class FragmentUser extends Fragment {

    private RecyclerView recycleView_highlight,recyclerView_recucler, recyclerView_video;
    private HighlightAdapter highlightAdapter;
    private RecuclerAdapter recuclerAdapter;
    private VideoAdapter videoAdapter;
    private ImageButton my_pictures,my_videos,gotoPost;
    private PoppinsMediumTextView fullName;
    private CircleImageView image_homepage;
    private AccountHome mainActivity;
    private String imageRecipe;
    private String nameRecipe;
    private String recipeID;
    private Button Btn_follow;
    private TextView postCount;
    private int userPost_counter = 0;
    LottieDialog Dialog;
    MainActivity mainActivity1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        fullName       = (PoppinsMediumTextView) view.findViewById(R.id.fullName);
        image_homepage = (CircleImageView)view.findViewById(R.id.image_homepage);

        setMyLayoutUser(view);
        HandleGotoPostFragment(view);
        Dialog = new LottieDialog(getActivity())
                .setAnimation(R.raw.pan)
                .setAnimationRepeatCount(LottieDrawable.INFINITE)
                .setAutoPlayAnimation(true)
                .setMessage("Homepage loading...")
                .setMessageTextSize(30)
                .setDialogBackground(Color.WHITE)
                .setAnimationViewHeight(400)
                .setCancelable(false);

        displayData(view);

        recycleView_highlight = view.findViewById(R.id.recycleView_highlight);
        highlightAdapter = new HighlightAdapter(getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false);
        recycleView_highlight.setLayoutManager(linearLayoutManager);
        highlightAdapter.setData(getListHighlight());
        recycleView_highlight.setAdapter(highlightAdapter);
        highlightAdapter.notifyDataSetChanged();

        recyclerView_recucler = view.findViewById(R.id.recucler_view_pictures);
        recuclerAdapter = new RecuclerAdapter(getActivity());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),3);
        recyclerView_recucler.setLayoutManager(gridLayoutManager);
        recuclerAdapter.setData(getListRecucler());
        recyclerView_recucler.setAdapter(recuclerAdapter);
        recuclerAdapter.notifyDataSetChanged();

        recyclerView_video = view.findViewById(R.id.recucler_view_video);
        videoAdapter = new VideoAdapter(getActivity());
        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(getActivity(),2);
        recyclerView_video.setLayoutManager(gridLayoutManager1);
        videoAdapter.setData1(getListVideo());
        recyclerView_video.setAdapter(videoAdapter);
        videoAdapter.notifyDataSetChanged();


        return  view;
    }
    public void displayData(View view) {
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
                                mainActivity = (AccountHome) getActivity();
                                String NameUser = object.getString("name");
                                String url = object.getJSONObject("picture").getJSONObject("data").getString("url");
                                fullName.setText(NameUser);
                                Glide.with(mainActivity).load(url).into(image_homepage);
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
                mainActivity = (AccountHome) getActivity();
                assert mainActivity != null;
                String userName_check_login = mainActivity.getUserName();
                String password_check_login = mainActivity.getPassword();
                String phone_check_login = mainActivity.getMobile();
                DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Profile");
                Query checkUser = database.orderByKey();
                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean foundUser = false;
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            String phoneFromDB = dataSnapshot.child("phone").getValue().toString();
                            if (phone_check_login.equals(phoneFromDB)) {
                                String nameFromDB = dataSnapshot.child("name").getValue().toString();
                                String imageFromDB = dataSnapshot.child("imageURl").getValue().toString();
                                fullName.setText(nameFromDB);
                                Glide.with(mainActivity).load(imageFromDB).into(image_homepage);
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
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }
    public List<Highlight> getListHighlight () {
            List<Highlight> list = new ArrayList<>();
            Dialog.show();
            mainActivity = (AccountHome) getActivity();
            assert mainActivity != null;
            String phone_check_login = mainActivity.getMobile();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Posts");
            Query check = reference.orderByChild("posterPhone").equalTo(phone_check_login);
            check.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    boolean foundUser = false;
                    if(snapshot.exists()){
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            foundUser = true;
                            nameRecipe = dataSnapshot.child("recipeName").getValue().toString();
                            imageRecipe = dataSnapshot.child("recipeImage").getValue().toString();
                            recipeID    = dataSnapshot.child("recipeId").getValue().toString();
                            String phone = phone_check_login;
                            list.add(new Highlight(nameRecipe,imageRecipe,recipeID,phone));
                        }
                        Dialog.dismiss();
                    }else{
                        Toast.makeText(mainActivity, "No Post", Toast.LENGTH_SHORT).show();
                        Dialog.dismiss();
                    } if(!foundUser){
                        Toast.makeText(mainActivity, "Error", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            Dialog.dismiss();
            return list;
        }
    public List<RecuclerView> getListRecucler(){
        List<RecuclerView> list2 = new ArrayList<>();
        mainActivity = (AccountHome) getActivity();
        assert mainActivity != null;
        String phone_check_login = mainActivity.getMobile();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Posts");
        Query check = reference.orderByChild("posterPhone").equalTo(phone_check_login);
        check.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean foundUser = false;
                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        foundUser = true;
                        imageRecipe = dataSnapshot.child("recipeImage").getValue().toString();
                        recipeID    = dataSnapshot.child("recipeId").getValue().toString();
                        String phone = phone_check_login;
                        list2.add(new RecuclerView(imageRecipe,recipeID,phone));
                    }
                    Dialog.dismiss();
                }else{
                    Toast.makeText(mainActivity, "No Post", Toast.LENGTH_SHORT).show();
                    Dialog.dismiss();
                } if(!foundUser){
                    Toast.makeText(mainActivity, "Huhu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return list2;
    }
    public void setMyLayoutUser(View view){
        recyclerView_recucler = (RecyclerView) view.findViewById(R.id.recucler_view_pictures);
        recyclerView_video    = (RecyclerView) view.findViewById(R.id.recucler_view_video);
        recycleView_highlight = (RecyclerView)view.findViewById(R.id.recycleView_highlight);
        my_pictures = (ImageButton) view.findViewById(R.id.my_pictures);
        my_videos   = (ImageButton) view.findViewById(R.id.my_videos);
        Btn_follow  = (Button)view.findViewById(R.id.Btn_follow);
        postCount   = (TextView)view.findViewById(R.id.postCount);
        recycleView_highlight.setVisibility(View.GONE);
        recyclerView_recucler.setVisibility(View.GONE);
        recyclerView_video.setVisibility(View.VISIBLE);
        my_pictures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView_recucler.setVisibility(View.VISIBLE);
                recycleView_highlight.setVisibility(View.VISIBLE);
                recyclerView_video.setVisibility(View.GONE);
            }
        });
        my_videos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView_recucler.setVisibility(View.GONE);
                recyclerView_video.setVisibility(View.VISIBLE);
                recycleView_highlight.setVisibility(View.GONE);
            }
        });
        mainActivity = (AccountHome) getActivity();
        assert mainActivity != null;
        String phone_check_login = mainActivity.getMobile();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Posts");
        Query check = reference.orderByChild("posterPhone").equalTo(phone_check_login);
        check.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean foundUser = false;
                if(snapshot.exists()){
                    foundUser = true;
                    Btn_follow.setVisibility(View.GONE);
                }else{
                    Btn_follow.setVisibility(View.VISIBLE);
                } if(!foundUser){
                    Toast.makeText(mainActivity, "No Account", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        //Calculate number of posts
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("Posts");
        Query check1 = reference1.orderByKey();
        check1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot allPosts) {
                for (DataSnapshot eachPost : allPosts.getChildren()){
                    String phoneFDB = eachPost.child("posterPhone").getValue().toString();
                    if(phone_check_login.equals(phoneFDB)){
                        userPost_counter++;
                    }
                }
                postCount.setText(""+userPost_counter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void HandleGotoPostFragment(View view){
        gotoPost = (ImageButton) view.findViewById(R.id.gotoPost);
        String phone_check_login = mainActivity.getMobile();
        gotoPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Intent intent = new Intent(getActivity(),MainActivity.class);
                intent.putExtra("mobile",phone_check_login);
              startActivity(intent);
            }
        });
    }
    public Vector<Video> getListVideo(){
        Vector<Video> list1 = new Vector<>();
        list1.add(new Video("<iframe width=\"200\" height=\"200\" src=\"https://www.youtube.com/embed/_CTkh6jIp10\" title=\"YouTube video player\" frameborder=\"0.2\" allow=\"accelerometer;  clipboard-write; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>"));
        list1.add(new Video("<iframe width=\"200\" height=\"200\" src=\"https://www.youtube.com/embed/iujzmNKQN74\" title=\"YouTube video player\" frameborder=\"0.2\" allow=\"accelerometer;  clipboard-write; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>"));
        list1.add(new Video("<iframe width=\"200\" height=\"200\" src=\"https://www.youtube.com/embed/X5TzCsHOVr0\" title=\"YouTube video player\" frameborder=\"0.2\" allow=\"accelerometer;  clipboard-write; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>"));
        list1.add(new Video("<iframe width=\"200\" height=\"200\" src=\"https://www.youtube.com/embed/D_PZdH0ixTw\" title=\"YouTube video player\" frameborder=\"0.2\" allow=\"accelerometer;  clipboard-write; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>"));
        list1.add(new Video("<iframe width=\"200\" height=\"200\" src=\"https://www.youtube.com/embed/MbGTm3qZilY\" title=\"YouTube video player\" frameborder=\"0.2\" allow=\"accelerometer;  clipboard-write; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>"));
        list1.add(new Video("<iframe width=\"200\" height=\"200\" src=\"https://www.youtube.com/embed/AEhBwiIEdj4\" title=\"YouTube video player\" frameborder=\"0.2\" allow=\"accelerometer;  clipboard-write; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>"));
        list1.add(new Video("<iframe width=\"200\" height=\"200\" src=\"https://www.youtube.com/embed/T81DONmNE4Q\" title=\"YouTube video player\" frameborder=\"0.2\" allow=\"accelerometer;  clipboard-write; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>"));
        list1.add(new Video("<iframe width=\"200\" height=\"200\" src=\"https://www.youtube.com/embed/SQ8HyKDkiR8\" title=\"YouTube video player\" frameborder=\"0.2\" allow=\"accelerometer;  clipboard-write; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>"));
        return list1;
    }

}