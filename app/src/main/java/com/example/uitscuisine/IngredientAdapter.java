package com.example.uitscuisine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class IngredientAdapter extends BaseAdapter {
    public Context context;
    public int layout;
    public List<Ingredient> list;

    public IngredientAdapter(Context context, int layout, List<Ingredient> list) {
        this.context = context;
        this.layout = layout;
        this.list = list;
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
        TextView ingredient;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);
            holder = new ViewHolder();

            holder.ingredient = (TextView) view.findViewById(R.id.ingredient);

            view.setTag(holder);
        }
        else {
            holder = (ViewHolder) view.getTag();
        }

        Ingredient ingredient = list.get(i);

        holder.ingredient.setText(ingredient.getIngredient());

        return view;
    }
}
