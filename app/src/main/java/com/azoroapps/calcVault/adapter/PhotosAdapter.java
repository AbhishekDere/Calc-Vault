package com.azoroapps.calcVault.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.azoroapps.calcVault.FullScreenActivity;
import com.azoroapps.calcVault.R;
import com.azoroapps.calcVault.view.ImageDetails;
import com.azoroapps.calcVault.view.Photos;
import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.PhotoViewHolder> {
    Context context;
    ArrayList<ImageDetails> images;
    ImageDetails obj;
    public String[] names;
    Photos photos;

    public PhotosAdapter(Context context, ArrayList<ImageDetails> image, String[] names) {
        this.context=context;
        this.images=image;
        this.names=names;
        photos= (Photos) context;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_list, parent,false);
        PhotoViewHolder photoViewHolder=new PhotoViewHolder(view,photos);
        return photoViewHolder;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        obj = images.get(position);
        Glide.with(context).load(obj.getUri()).centerInside().placeholder(R.drawable.placeholder).into(holder.imageView);

        if(!photos.is_photos_in_action_mode){
            holder.checkBox.setVisibility(View.GONE);
        }
        else{
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setChecked(false);
        }
       // holder.imageView.setPadding(10,10,10,10);
            holder.imageView.setOnLongClickListener(v -> {
                PopupMenu popup = new PopupMenu(context, holder.imageView);
                //inflating menu from xml resource
                popup.inflate(R.menu.menu_longclickoptions);
                //adding click listener
                popup.setOnMenuItemClickListener(item -> {
                    File file = new File(obj.getUri().getPath());
                    switch (item.getItemId()) {
                        case R.id.option_delete:
                            boolean b =file.delete();
                            if(b){
                                images.remove(holder.getAdapterPosition());
                                notifyItemRemoved(holder.getAdapterPosition());
                                Toasty.info(context,"Deleted "+file.getName(),Toasty.LENGTH_LONG).show();
                            }
                            break;
                        case R.id.option_unhide:
                            moveFile(file.getParent()+"/", file.getName(), Environment.getExternalStorageDirectory().getPath()+"/DCIM/Photos/");
                            images.remove(holder.getAdapterPosition());
                            notifyItemRemoved(holder.getAdapterPosition());
                            Toasty.success(context,"Video is Unhidden Successfully\n You can find it in /DCIM/Photos folder ",Toasty.LENGTH_LONG).show();
                            break;
                    }
                    return false;
                });
                //displaying the popup
                popup.show();
                return false;
            });
            holder.imageView.setOnClickListener(v -> {
                //Full Screen Activity
                Intent i = new Intent(context, FullScreenActivity.class);
                i.putExtra("images", names);
                i.putExtra("position",position);
                context.startActivity(i);
            });
    }

    private void moveFile(String inputPath, String inputFile, String outputPath) {
        InputStream in;
        OutputStream out;
        try {
            //create output directory if it doesn't exist
            File dir = new File (outputPath);
            if (!dir.exists())
            {
                boolean b =dir.mkdirs();
                if(b){
                    Toasty.info(context,"Hiding Images",Toasty.LENGTH_SHORT).show();
                }
            }
            in = new FileInputStream(inputPath + inputFile);
            out = new FileOutputStream(outputPath + inputFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            // write the output file
            out.flush();
            out.close();
            // delete the original file
            boolean l = new File(inputPath + inputFile).delete();
            if(l)
                Toasty.success(context,"Successful",Toasty.LENGTH_SHORT).show();
        }
        catch (FileNotFoundException f) {
            Log.e("File", f.getMessage());
        }
        catch (Exception e) {
            Log.e("tag", e.getMessage());
        }
    }

    public void updateAdapter(ArrayList<ImageDetails> list){
        for(ImageDetails img:list){
            String path= img.getUri().getPath();
            File f = new File(path);
            images.remove(img);
            f.delete();
            Toasty.success(context,"Videos are Deleted Successfully",Toasty.LENGTH_LONG).show();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        Photos photos;
        CheckBox checkBox;
        public PhotoViewHolder(@NonNull View itemView, Photos photos) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            checkBox=itemView.findViewById(R.id.checkbox_photo);
            this.photos=photos;
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    photos.prepareSelection(v,getAdapterPosition());
                }
            });
        }
    }

}

