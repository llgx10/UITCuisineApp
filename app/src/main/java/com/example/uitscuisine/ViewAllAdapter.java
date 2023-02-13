package com.example.uitscuisine;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.uitscuisine.custom_textview.PoppinsMediumTextView;


import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewAllAdapter extends RecyclerView.Adapter<ViewAllAdapter.ViewAllRecipeViewHolder> {
    private List<ViewAllRecipe> mViewAll;
    Context mcontext2;
    private String recipeID;
    private String phone;
    ViewAll viewAll;
    public void setData(List<ViewAllRecipe> list){
        this.mViewAll = list;
        notifyDataSetChanged();
    }
    public ViewAllAdapter(Context context){
        this.mcontext2 = context;
    }
    @NonNull
    @Override
    public ViewAllAdapter.ViewAllRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_viewallpost,parent,false);

        return new ViewAllRecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewAllRecipeViewHolder holder, int position) {
        ViewAllRecipe viewAllRecipe = mViewAll.get(position);
        if(viewAllRecipe == null){
            return;
        }

        Glide.with(mcontext2).load(mViewAll.get(position).getImage()).into(holder.posterAvatar);
        Glide.with(mcontext2).load(mViewAll.get(position).getPost()).into(holder.image_viewAll);
        holder.posterName.setText(viewAllRecipe.getPosterName());
        holder.neededTime.setText(viewAllRecipe.getNeededTime());
        holder.category.setText(viewAllRecipe.getCategory());
        holder.level.setText(viewAllRecipe.getLevel());
        holder.name_viewAll.setText(viewAllRecipe.getNameRecipe());
        holder.recipeId.setText(viewAllRecipe.getRecipeId());
        holder.itemViewAllPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickGoToDetail(viewAllRecipe);
            }
        });

    }
    public void onClickGoToDetail(ViewAllRecipe viewAllRecipe){
        recipeID = viewAllRecipe.getRecipeId();
        phone   = viewAllRecipe.getPhone();
        Intent intent = new Intent(mcontext2,RecipeDetails.class);
        intent.putExtra("recipeId", recipeID);
        intent.putExtra("mobile",phone);
        mcontext2.startActivity(intent);
    }
    @Override
    public int getItemCount() {
        if(mViewAll != null){
            return mViewAll.size();
        }
        return 0;
    }

    public class ViewAllRecipeViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView posterAvatar;
        private PoppinsMediumTextView posterName;
        private ImageView image_viewAll;
        private PoppinsMediumTextView name_viewAll;
        private PoppinsMediumTextView neededTime;
        private PoppinsMediumTextView level;
        private PoppinsMediumTextView category;
        private TextView recipeId;
        private RelativeLayout itemViewAllPost;
        public ViewAllRecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            posterAvatar = (CircleImageView) itemView.findViewById(R.id.viewAllAvatar);
            posterName   = (PoppinsMediumTextView) itemView.findViewById(R.id.posterName);
            image_viewAll= (ImageView)itemView.findViewById(R.id.image_viewAll);
            name_viewAll = (PoppinsMediumTextView) itemView.findViewById(R.id.name_viewAll);
            neededTime   = (PoppinsMediumTextView) itemView.findViewById(R.id.neededTime);
            level        = (PoppinsMediumTextView) itemView.findViewById(R.id.level);
            category     = (PoppinsMediumTextView) itemView.findViewById(R.id.category);
            recipeId     = (TextView) itemView.findViewById(R.id.recipeId);
            itemViewAllPost = (RelativeLayout) itemView.findViewById(R.id.itemViewAllPost);
        }
    }
}
