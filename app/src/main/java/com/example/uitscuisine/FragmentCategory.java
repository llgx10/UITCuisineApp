package com.example.uitscuisine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class FragmentCategory extends Fragment {

    GridView categoriesGrid;
    ArrayList<Category> categoriesArray = new ArrayList<>();
    CategoryAdapter categoryAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        createCategoriesGrid(view);

        return view;
    }

    public void createCategoriesGrid(View view) {
        categoriesGrid = (GridView) view.findViewById(R.id.categoriesGrid);
        for (int i = 1; i <= 2; i++) {
            categoriesArray.add(new Category("Japanese BBQ", R.drawable.food1));
            categoriesArray.add(new Category("Blueberry Ice Cream", R.drawable.food2));
            categoriesArray.add(new Category("Sashimi Salad", R.drawable.food3));
            categoriesArray.add(new Category("Chinese Meetball", R.drawable.food4));
            categoriesArray.add(new Category("Thai Hotpot", R.drawable.food5));
            categoriesArray.add(new Category("Korean Kimbap", R.drawable.food6));
            categoriesArray.add(new Category("Chicken Fire",R.drawable.food7));
            categoriesArray.add(new Category("Chicken Fire Honey",R.drawable.food9));
            categoriesArray.add(new Category("Mì ý Spaghetti",R.drawable.food10));
        }
        categoryAdapter = new CategoryAdapter(getActivity(), R.layout.category, categoriesArray);
        categoriesGrid.setAdapter(categoryAdapter);
    }
}
