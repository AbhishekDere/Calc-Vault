package com.azoroapps.calcVault.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.azoroapps.calcVault.FullScreenActivity;
import com.azoroapps.calcVault.view.ImageDetails;
import com.azoroapps.calcVault.R;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.PhotoViewHolder> {
    Context context;
    public ArrayList<ImageDetails> images;
    ImageDetails obj;
    public String[] names;

    public PhotosAdapter(Context context, ArrayList<ImageDetails> image, String[] names) {
        this.context=context;
        this.images=image;
        this.names=names;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.photo_list, parent,false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        obj = images.get(position);
        Glide.with(context).load(obj.getUri()).centerInside().placeholder(R.drawable.placeholder).into(holder.imageView);
       // holder.imageView.setPadding(10,10,10,10);

        holder.imageView.setOnClickListener(v -> {
            //Full Screen Activity
            Intent i = new Intent(context, FullScreenActivity.class);
            i.putExtra("images", names);
            i.putExtra("position",position);
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        //TextView textView;
        LinearLayout linearLayout;
        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.video_layout);
            imageView = itemView.findViewById(R.id.image);
        }
    }
}
