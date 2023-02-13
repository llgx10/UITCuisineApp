package com.example.uitscuisine;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

public class ChipFragment extends Fragment {

    private ChipGroup chipGroup;

    public ChipFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chip_entry, container, false);
        chipGroup = view.findViewById(R.id.chipitem);
        Chip chip = createChip("John Doe", "https://i1.sndcdn.com/avatars-000197335271-mztyvq-t500x500.jpg");
        chipGroup.addView(chip);
        return view;
    }

    private Chip createChip(String name, String avatarUrl) {
        GlideChip chip = new GlideChip(getContext());
        chip.setText(name);
        chip.setIconUrl(avatarUrl, getResources().getDrawable(R.drawable.ic_close));
        chip.setClickable(true);
        chip.setFocusable(true);
        return chip;
    }
    public class GlideChip extends Chip {

        public GlideChip(Context context) {
            super(context);
        }

        public GlideChip(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        /**
         * Set an image from an URL for the {@link Chip} using {@link com.bumptech.glide.Glide}
         * @param url icon URL
         * @param errDrawable error icon if Glide return failed response
         */
        public GlideChip setIconUrl(String url, Drawable errDrawable) {
            Glide.with(this)
                    .load(url)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            setChipIcon(errDrawable);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            setChipIcon(resource);
                            return false;
                        }
                    }).preload();
            return this;
        }

    }
}
