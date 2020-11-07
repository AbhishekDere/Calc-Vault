package com.azoroapps.calcVault.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.azoroapps.calcVault.R;
import com.azoroapps.calcVault.view.VideoDetails;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.myVideoHolder> {
    Context context;
    ArrayList<VideoDetails> videos;
    View view;

    public VideoAdapter(Context context, ArrayList<VideoDetails> videos){
        this.context=context;
        this.videos=videos;
    }

    @NonNull
    @Override
    public myVideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        view=layoutInflater.inflate(R.layout.video_list, parent,false);
        return new myVideoHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull myVideoHolder holder, int position) {
        VideoDetails obj = videos.get(position);
        Glide.with(context).load(obj.getUri()).into(holder.icon);
        holder.videoName.setText(obj.getName());
        holder.view.setBackgroundColor(obj.isSelected() ? Color.CYAN : Color.WHITE);
        //onClicks on very item
        holder.videoName.setOnClickListener(v -> playVideo(obj.getUri()));
        holder.more_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context, holder.more_btn);
                //inflating menu from xml resource
                popup.inflate(R.menu.menu_longclickoptions);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.option_1:
                                //handle menu1 click
                                break;
                            case R.id.option_2:
                                //handle menu2 click
                                break;
                            case R.id.option_3:
                                //handle menu3 click
                                break;
                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.show();
            }
        });
        holder.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.more_btn.setVisibility(View.GONE);
                obj.setSelected(!obj.isSelected());
                holder.view.setBackgroundColor(obj.isSelected() ? Color.CYAN : Color.WHITE);
            }
        });
    }

    protected void playVideo(Uri fileUri){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(String.valueOf(fileUri)), "video/mp4");
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public static class myVideoHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView videoName;
        View view = itemView.findViewById(R.id.video_layout);
        ImageView more_btn= itemView.findViewById(R.id.ic_more_btn);

        public myVideoHolder(@NonNull View itemView) {
            super(itemView);
            icon= itemView.findViewById(R.id.video_icon);
            videoName=itemView.findViewById(R.id.videoTitle);
        }
    }
}
