package com.example.uitscuisine;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.uitscuisine.Category_item;

import java.util.ArrayList;

public class categoryRVAdapter extends RecyclerView.Adapter<categoryRVAdapter.CustomViewHolder> {
    private ArrayList<Category_item> items;
    private Context context;

    public categoryRVAdapter(ArrayList<Category_item> items, Context context) {
        this.items = items;
        this.context = context;
    }


    @Override
    public void onBindViewHolder(@NonNull categoryRVAdapter.CustomViewHolder holder, int position) {

    }


    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    //    @NonNull
//    @Override
//    public void onBindViewHolder(@NonNull categoryRVAdapter.CustomViewHolder holder, int position) {
//        holder.textView.setText(items.get(position).getname().toUpperCase());
//        Picasso.get().load(items.get(position).getImg()).placeholder(R.drawable.empty23).error(R.drawable.empty23).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).into(holder.img);
//
//    }
//
//
    @Override
    public int getItemCount() {
        return items.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;
        ImageView img;

        public CustomViewHolder(View view) {
            super(view);
//            img = view.findViewById(R.id.category_image);
//            textView = view.findViewById(R.id.category_tv);
//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(context, CourseByCategory.class);
//                    intent.putExtra("name", items.get(getAdapterPosition()).getname());
//                    intent.putExtra("ID", items.get(getAdapterPosition()).getID());
//                    Toast.makeText(context, items.get(getAdapterPosition()).getID(), Toast.LENGTH_SHORT).show();
//                    context.startActivity(intent);
//                }
//            });
        }
    }
}
