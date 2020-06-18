package com.azoroapps.calcVault;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.MyViewHolder> {
    ArrayList<String> data;
    AlbumAdapter(ArrayList<String> string){
        this.data=string;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.album_list, parent,false);
        return new MyViewHolder(view);
    }
    //Created a new Custom ViewHolder

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.textView.setText(data.get(position));
        holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                return false;
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
            linearLayout = itemView.findViewById(R.id.photo_id);
            imageView = itemView.findViewById(R.id.album_image);
            textView = itemView.findViewById(R.id.album_name);

        }
    }
}
