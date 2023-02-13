package com.example.uitscuisine;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class RecuclerAdapter extends RecyclerView.Adapter<RecuclerAdapter.RecuclerViewHolder> {
    private List<RecuclerView> mRecucler;
    private Context mcontext;
    private String recipeID;
    private String phone;
    public void setData(List<RecuclerView>list){
        this.mRecucler =list;
        notifyDataSetChanged();
    }
    public RecuclerAdapter(Context context){
        this.mcontext = context;
    }
    @NonNull
    @Override
    public RecuclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recucler,parent,false);
        return new RecuclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecuclerViewHolder holder, int position) {
            RecuclerView recuclerView = mRecucler.get(position);
            if(recuclerView==null){
                return;
            }

            Glide.with(mcontext).load(recuclerView.getRimage()).into(holder.image_recucler);
            holder.IDrecipe.setText(recuclerView.getRecipeID());
            holder.itemRecucler.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickGoToDetail1(recuclerView);
                }
            });
    }
    public void onClickGoToDetail1(RecuclerView recuclerView){
        recipeID = recuclerView.getRecipeID();
        phone   = recuclerView.getPhone();
        Intent intent = new Intent(mcontext,RecipeDetails.class);
        intent.putExtra("mobile",phone);
        intent.putExtra("recipeId", recipeID);
        mcontext.startActivity(intent);
    }
    @Override
    public int getItemCount() {
        if(mRecucler!=null){
            return mRecucler.size();
        }
        return 0;
    }

    public class RecuclerViewHolder extends RecyclerView.ViewHolder{
        private final ImageView image_recucler;
        private TextView  IDrecipe;
        private RelativeLayout itemRecucler;
        public RecuclerViewHolder(@NonNull View itemView) {
            super(itemView);
            image_recucler = itemView.findViewById(R.id.image_recucler);
            IDrecipe       = (TextView)itemView.findViewById(R.id.recipeId);
            itemRecucler   = (RelativeLayout)itemView.findViewById(R.id.itemRecucler);
        }
    }
}
