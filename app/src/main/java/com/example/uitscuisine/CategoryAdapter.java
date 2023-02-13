package com.example.uitscuisine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CategoryAdapter extends BaseAdapter {
    public Context context;
    public int layout;
    public List<Category> list;

    public CategoryAdapter(Context context, int layout, List<Category> list) {
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
        TextView categoryName;
        ImageView categoryImage;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);
            holder = new ViewHolder();

            holder.categoryName = (TextView) view.findViewById(R.id.categoryName);
            holder.categoryImage = (ImageView) view.findViewById(R.id.categoryImage);

            view.setTag(holder);
        }
        else {
            holder = (ViewHolder) view.getTag();
        }

        Category category = list.get(i);

        holder.categoryName.setText(category.getCategoryName());
        holder.categoryImage.setImageResource(category.getCategoryImage());

        return view;
    }
}
