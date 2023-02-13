package com.example.uitscuisine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class FavouriteRecipeAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<FavouriteRecipe> list;

    public FavouriteRecipeAdapter(Context context, int layout, List<FavouriteRecipe> list) {
        this.context = context;
        this.layout = layout;
        this.list = list;
    }

    public void addListItemAdapter(ArrayList<FavouriteRecipe> itemPlus) {
        list.addAll(itemPlus);
        this.notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return list.size();
    }
    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public class ViewHolder {
        TextView neededTime, level, titleText, posterName;
        ImageView titleImage, posterAvatar;
        TextView recipeId;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        FavouriteRecipeAdapter.ViewHolder holder;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);
            holder = new FavouriteRecipeAdapter.ViewHolder();

            holder.neededTime = (TextView) view.findViewById(R.id.neededTime);
            holder.level = (TextView) view.findViewById(R.id.level);
            holder.titleText = (TextView) view.findViewById(R.id.titleText);
            holder.posterName = (TextView) view.findViewById(R.id.posterName);
            holder.titleImage = (ImageView) view.findViewById(R.id.titleImage);
            holder.posterAvatar = (ImageView) view.findViewById(R.id.posterAvatar);
            holder.recipeId = (TextView) view.findViewById(R.id.recipeId);

            view.setTag(holder);
        }
        else {
            holder = (FavouriteRecipeAdapter.ViewHolder) view.getTag();
        }
        FavouriteRecipe recipe;
        try{
            recipe = list.get(i);
            holder.neededTime.setText(recipe.getNeededTime());
            holder.level.setText(recipe.getLevel());
            holder.titleText.setText(recipe.getTitleText());
            holder.posterName.setText(recipe.getPosterName());
            Glide.with(view).load(recipe.getTitleImage()).into(holder.titleImage);
            Glide.with(view).load(recipe.getPosterAvatar()).into(holder.posterAvatar);
            holder.recipeId.setText(recipe.getRecipeId());
        }catch (IndexOutOfBoundsException e){
            Toast.makeText(context, "SOMETHING WRONG", Toast.LENGTH_SHORT).show();
        }
        return view;
    }
}
