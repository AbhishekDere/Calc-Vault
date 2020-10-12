package com.azoroapps.calcVault.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.azoroapps.calcVault.R;
import com.azoroapps.calcVault.VideoDetails;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.myVideoHolder> {
    Context context;
    ArrayList<VideoDetails> videos;

    public VideoAdapter(Context context, ArrayList<VideoDetails> videos){
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
        holder.videoName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playVideo(obj.getUri());
            }
        });
    }
    protected void playVideo(Uri fileUri){
        Uri photoURI = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", new File(String.valueOf(fileUri)));
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(photoURI)));
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(intent);
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
