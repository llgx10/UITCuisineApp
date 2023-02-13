package com.example.uitscuisine;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HighlightAdapter extends RecyclerView.Adapter<HighlightAdapter.HighlightViewHolder>{

    private List<Highlight> mHighlight;
    private ArrayList<Highlight>list;
    private Activity mActivity;
    private  Context mcontext;
    private String recipeID;
    private String phone;
    public void setData(List<Highlight>list){
        this.mHighlight =list;
        notifyDataSetChanged();
    }
    public HighlightAdapter(Context context){
        this.mcontext = context;

    }
    @NonNull
    @Override
    public HighlightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_highlight,parent,false);
        return new HighlightViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HighlightViewHolder holder, int position) {
            Highlight highlight = mHighlight.get(position);
            if(highlight == null){
                return;
            }

            Glide.with(mcontext).load(mHighlight.get(position).getImage()).into(holder.image);
            holder.name_highlight.setText(highlight.getHname());
            holder.IDrecipe.setText(highlight.getRecipeId());
            holder.itemHighlight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickGoToDetail(highlight);
                }
            });
    }
    public void onClickGoToDetail(Highlight highlight){
        recipeID = highlight.getRecipeId();
        phone    = highlight.getPhone();
        Intent intent = new Intent(mcontext,RecipeDetails.class);
        intent.putExtra("recipeId", recipeID);
        intent.putExtra("mobile",phone);
        mcontext.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        if(mHighlight != null){
            return mHighlight.size();
        }
        return 0;
    }

    public class HighlightViewHolder extends RecyclerView.ViewHolder{
            private CircleImageView image;
            private TextView name_highlight, IDrecipe;
            private RelativeLayout itemHighlight;
            public HighlightViewHolder(@NonNull View itemView) {
                super(itemView);
                image =itemView.findViewById(R.id.image_highlight);
                name_highlight = itemView.findViewById(R.id.name_highlight);
                itemHighlight  = (RelativeLayout)itemView.findViewById(R.id.itemHighlight);
                IDrecipe       = (TextView)itemView.findViewById(R.id.recipeId);
            }
        }

}
