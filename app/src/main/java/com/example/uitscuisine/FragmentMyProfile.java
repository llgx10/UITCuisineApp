package com.example.uitscuisine;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.uitscuisine.custom_textview.PoppinsBlackTextView;
import com.example.uitscuisine.custom_textview.PoppinsMediumTextView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FragmentMyProfile extends Fragment {
    Animation left_animation;
    ImageView ivReturn;
    PoppinsMediumTextView textName,textPhone, textEmail, textAddress,textBirthday,textGender,textWeight,textHeight;
    PoppinsBlackTextView  textNameProfile;
    Button EditProfile1;
    LinearLayout layoutName,layoutPhone,layoutEmail, layoutBirth, layoutAddress,layoutGender;
    LinearLayout layoutWeight,layoutHeight;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    DatabaseReference firstDatabase = databaseReference.child("Profile");
    DatabaseReference secondDatabase = databaseReference.child("username");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myprofile, container, false);
        animation(view);
//        returnHome(view);
        forwardEditProfile(view);

        textNameProfile = (PoppinsBlackTextView) view.findViewById(R.id.textNameProfile);
        textName        = (PoppinsMediumTextView) view.findViewById(R.id.textName);
        textPhone       = (PoppinsMediumTextView) view.findViewById(R.id.textPhone);
        textEmail       = (PoppinsMediumTextView) view.findViewById(R.id.textEmail);
        textAddress     = (PoppinsMediumTextView) view.findViewById(R.id.textAddress);
        textBirthday    = (PoppinsMediumTextView) view.findViewById(R.id.textBirthday);
        textGender      = (PoppinsMediumTextView) view.findViewById(R.id.textGender);
        textWeight      = (PoppinsMediumTextView) view.findViewById(R.id.textWeight);
        textHeight      = (PoppinsMediumTextView) view.findViewById(R.id.textHeight);

        Bundle data = this.getArguments();
        if(data!=null){
            String name = data.getString("name");
            String phone = data.getString("phone");
            textName.setText(name);
            textNameProfile.setText(name);

        }
        return view;
    }
    public void forwardEditProfile(View view){
        EditProfile1 = (Button) view.findViewById(R.id.EditProfile);
        EditProfile1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),EditProfile.class);
                startActivity(intent);
            }
        });
    }
    public void  animation(View view){
        layoutName = (LinearLayout)view.findViewById(R.id.layoutName);
        layoutPhone = (LinearLayout)view.findViewById(R.id.layoutPhone);
        layoutEmail = (LinearLayout)view.findViewById(R.id.layoutEmail);
        layoutBirth = (LinearLayout)view.findViewById(R.id.layoutBirth);
        layoutGender= (LinearLayout)view.findViewById(R.id.layoutGender);
        layoutAddress = (LinearLayout)view.findViewById(R.id.layoutAddress);
        layoutWeight = (LinearLayout)view.findViewById(R.id.layoutWeight);
        layoutHeight= (LinearLayout)view.findViewById(R.id.layoutHeight);
        left_animation = AnimationUtils.loadAnimation(getActivity(),R.anim.left_animation);
        layoutName.setAnimation(left_animation);
        layoutPhone.setAnimation(left_animation);
        layoutEmail.setAnimation(left_animation);
        layoutAddress.setAnimation(left_animation);
        layoutBirth.setAnimation(left_animation);
        layoutHeight.setAnimation(left_animation);
        layoutWeight.setAnimation(left_animation);
        layoutGender.setAnimation(left_animation);


    }
}
