package com.example.uitscuisine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ImageForSliderAdapter extends PagerAdapter {
    private Context context;
    private List<ImageForSlider> list;

    public ImageForSliderAdapter(Context context, List<ImageForSlider> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.image_slider, container, false);
        ImageView myImageSlider = view.findViewById(R.id.myImageSlider);
        TextView sliderText = view.findViewById(R.id.sliderText);

        ImageForSlider imageForSlider = list.get(position);
        if (imageForSlider != null) {
            Glide.with(context).load(imageForSlider.getResourceID()).into(myImageSlider);
            sliderText.setText(imageForSlider.getSliderText());
        }

        // Add view to viewgroup
        container.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        // Remove view from viewgroup
        container.removeView((View) object);
    }
}
