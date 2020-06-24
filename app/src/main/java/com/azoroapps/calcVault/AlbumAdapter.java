package com.azoroapps.calcVault;
import android.content.Intent;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;


public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.MyViewHolder> {
    ArrayList<String> data;
    Context context;

    AlbumAdapter(Context context, ArrayList<String> data){
        this.data=data;
        this.context=  context;
    }

    @Override
    @NonNull
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.album_list, parent,false);
        return new MyViewHolder(view);
    }
    //Created a new Custom ViewHolder

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.textView.setText(data.get(position));
        final String albumName =data.get(position);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context,Photos.class);
                intent.putExtra("AlbumName",albumName);
                context.startActivity(intent);
                Toasty.success(context,"Clicked on "+albumName,Toasty.LENGTH_SHORT).show();
            }
        });

        holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toasty.success(context,"Long Clicked on "+albumName,Toasty.LENGTH_SHORT).show();

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
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
