package com.azoroapps.calcVault.adapter;
import android.content.Intent;
import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.azoroapps.calcVault.ImageDetails;
import com.azoroapps.calcVault.view.Photos;
import com.azoroapps.calcVault.R;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import es.dmoral.toasty.Toasty;
public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.MyViewHolder> {
    ArrayList<String> albumNames;
    ArrayList<String> albumPhotos;
    Context context;

   public AlbumAdapter(Context context, ArrayList<String> albumName,ArrayList<String> albumPhotos){
        this.albumNames=albumName;
        this.context=  context;
        this.albumPhotos=albumPhotos;
    }
    @Override
    @NonNull
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view=layoutInflater.inflate(R.layout.album_list, parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
       ImageDetails obj = new ImageDetails();
       obj.getUri();
        final String albumName =albumNames.get(position);
        holder.textView.setText(albumNames.get(position).substring(1));
        Glide.with(context)
                .load(obj.getUri())
                .into(holder.imageView);
        //holder.imageView.setImageResource());
        holder.linearLayout.setOnClickListener(v -> {
            Intent intent = new Intent(context, Photos.class);
            intent.putExtra("AlbumName",albumName);
            context.startActivity(intent);
            Toasty.success(context,"Clicked on "+albumName,Toasty.LENGTH_SHORT).show();
        });

        holder.linearLayout.setOnLongClickListener(v -> {
            Toasty.success(context,"Long Clicked on "+albumName,Toasty.LENGTH_SHORT).show();

            return true;
        });
    }

        @Override
    public int getItemCount() {
        return albumNames.size();
    }

     static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;
        LinearLayout linearLayout;
        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.album_id);
            imageView = itemView.findViewById(R.id.album_image);
            textView = itemView.findViewById(R.id.album_name);

        }
    }
}
