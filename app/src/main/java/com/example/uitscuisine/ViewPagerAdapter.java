package com.example.uitscuisine;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private Object myProfile;

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new FragmentHome();
            case 1:
                return new FragmentAddRecipe();
            case 2:
                return new FragmentMenuPlaner();
            case 3:
                return new FragmentShoppingList();
            case 4:
                break;
//            case 5:
//                return new FragmentSetting();
        }
        return new FragmentHome();
    }
    
    @Override
    public int getItemCount() {
        return 5;
    }
}
