package com.example.uitscuisine;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class FragmentSetting extends Fragment {

    ListView settingsList;
    ArrayList<AccountSetting> settingsArray = new ArrayList<>();
    AccountSettingAdapter settingAdapter;
    ImageView settingForward;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        createSettingsList(view);
        editForward(view);
        return view;
    }

    public void createSettingsList(View view) {
        settingsList = (ListView) view.findViewById(R.id.settingsList);
        settingsArray.add(new AccountSetting("Languages", R.drawable.internet32));
        settingsArray.add(new AccountSetting("Privacy Policy", R.drawable.document32));
        settingsArray.add(new AccountSetting("Terms & Condition", R.drawable.info32));
        settingsArray.add(new AccountSetting("Rate Us", R.drawable.comment32));
        settingsArray.add(new AccountSetting("Share App", R.drawable.share32));
        settingAdapter = new AccountSettingAdapter(getActivity(), R.layout.account_setting, settingsArray);
        settingsList.setAdapter(settingAdapter);
    }
    public void editForward(View view){
        settingForward = (ImageView) view.findViewById(R.id.settingForward);
        settingForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),EditProfile.class);
                startActivity(intent);
            }
        });

    }

}
