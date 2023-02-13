package com.example.uitscuisine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {
    private List<Video> mvideo;
    private Context mcontext;
    public void setData1(List<Video> list){
        this.mvideo = list;
        notifyDataSetChanged();
    }
    public VideoAdapter(Context context){
        this.mcontext = context;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video,parent,false);
        return new VideoAdapter.VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
            Video videoViewHolder = mvideo.get(position);
            if(videoViewHolder == null){
                return;
            }
            holder.video_recucler.loadData(mvideo.get(position).getVideoUrl(),"text/html","utf-8");
    }

    @Override
    public int getItemCount() {
        if(mvideo != null){
            return mvideo.size();
        }
        return 0;
    }


    public class VideoViewHolder extends RecyclerView.ViewHolder{
        private final WebView video_recucler;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            video_recucler = (WebView)itemView.findViewById(R.id.video_recucler);
            video_recucler.getSettings().setJavaScriptEnabled(true);
            video_recucler.setWebChromeClient(new WebChromeClient());
        }
    }
}
