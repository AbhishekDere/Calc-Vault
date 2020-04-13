package com.example.calc_vault;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.MyViewHolder> {

    String[] data;
    public AlbumAdapter(String[] string){
        this.data=string;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.photo_list, parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.textView.setText(data[position]);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"hello "+data[position],Toast.LENGTH_LONG);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;
        LinearLayout linearLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.photo_id);
            imageView = itemView.findViewById(R.id.album_image);
            textView = itemView.findViewById(R.id.album_detail);

        }
    }
}
