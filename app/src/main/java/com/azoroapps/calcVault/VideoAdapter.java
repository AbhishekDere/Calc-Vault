package com.azoroapps.calcVault;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.myVideoHolder> {
    Context context;
    ArrayList<VideoDetails> videos;



    VideoAdapter(Context context, ArrayList<VideoDetails> videos){
        this.context=context;
        this.videos=videos;
    }
    @NonNull
    @Override
    public myVideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.video_list, parent,false);
        return new myVideoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myVideoHolder holder, int position) {
        VideoDetails obj = videos.get(position);
        Glide.with(context).load(obj.getUri()).into(holder.icon);
        holder.videoName.setText(obj.getName());

    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public static class myVideoHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView videoName;
        public myVideoHolder(@NonNull View itemView) {
            super(itemView);
            icon= itemView.findViewById(R.id.video_icon);
            videoName=itemView.findViewById(R.id.videoTitle);
        }
    }
}
