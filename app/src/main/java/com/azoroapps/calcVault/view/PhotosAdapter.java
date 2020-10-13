package com.azoroapps.calcVault.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.azoroapps.calcVault.ImageDetails;
import com.azoroapps.calcVault.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.PhotoViewHolder> {
    Context context;
    ArrayList<ImageDetails> images;

    public PhotosAdapter(Context context, ArrayList<ImageDetails> image) {
        this.context=context;
        this.images=image;
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
        ImageDetails obj = images.get(position);
        Picasso.with(context).load(obj.getUri()).placeholder(R.drawable.placeholder).fit().centerCrop().into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toasty.success(context,"working"+images.get(position),Toasty.LENGTH_SHORT).show();
            }
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
            imageView.setPadding(10,10,10,5);
        }
    }
}
