
package com.azoroapps.calcVault.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.azoroapps.calcVault.R;
import com.azoroapps.calcVault.view.VideoDetails;
import com.azoroapps.calcVault.view.Videos;
import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.myVideoHolder> {

    Context context;
    ArrayList<VideoDetails> videos;
    Videos vid;

    public VideoAdapter(Context context, ArrayList<VideoDetails> videos){
        this.context=context;
        this.videos=videos;
        vid=(Videos)context;
    }

    @NonNull
    @Override
    public myVideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.video_list, parent,false);
        myVideoHolder mvh= new myVideoHolder(view,vid);
        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull myVideoHolder holder, int position) {
        VideoDetails obj = videos.get(position);
        Glide.with(context).load(obj.getUri()).into(holder.icon);
        holder.videoName.setText(obj.getName());
        //onClicks on very item
        holder.view.setOnClickListener(v -> playVideo(obj.getUri()));
        holder.more_btn.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(context, holder.more_btn);
            //inflating menu from xml resource
            popup.inflate(R.menu.menu_longclickoptions);
            //adding click listener
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @SuppressLint("NonConstantResourceId")
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    File file = new File(obj.getUri().getPath());
                    switch (item.getItemId()) {
                        case R.id.option_delete:
                                boolean b =file.delete();
                                if(b){
                                    videos.remove(holder.getAdapterPosition());
                                    notifyItemRemoved(holder.getAdapterPosition());
                                    Toasty.info(context,"Deleted "+file.getName(),Toasty.LENGTH_LONG).show();
                                }
                            break;
                        case R.id.option_unhide:
                            moveFile(file.getParent()+"/", file.getName(),Environment.getExternalStorageDirectory().getPath()+"/DCIM/Videos/");
                            videos.remove(holder.getAdapterPosition());
                            notifyItemRemoved(holder.getAdapterPosition());
                            Toasty.success(context,"Video is Unhidden Successfully\n You can find it in /DCIM/Videos/ folder ",Toasty.LENGTH_LONG).show();
                            break;
                    }
                    return false;
                }
            });
            //displaying the popup
            popup.show();
        });

        if(!vid.is_in_action_mode){
            holder.checkBox.setVisibility(View.GONE);
            holder.more_btn.setVisibility(View.VISIBLE);
        }
        else if(vid.is_in_action_mode){
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setChecked(false);
            holder.more_btn.setVisibility(View.GONE);
        }
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vid.prepareSelection(v,position);
            }
        });
    }

    protected void playVideo(Uri fileUri){
        if(!vid.is_in_action_mode){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(String.valueOf(fileUri)), "video/mp4");
            context.startActivity(intent);
        }
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public void updateAdapter(ArrayList<VideoDetails> list ){
        for(VideoDetails videoDetails:list){
            String path=videoDetails.getUri().getPath();
            File f = new File(path);
            videos.remove(videoDetails);
            f.delete();
            Toasty.success(context,"Videos are Deleted Successfully",Toasty.LENGTH_LONG).show();
        }
        notifyDataSetChanged();
    }

    public void unHideAdapter(ArrayList<VideoDetails> list ){
        for(VideoDetails videoDetails:list){
            //TODO (Remove the Files using
            String path=videoDetails.getUri().getPath();
            File f = new File(path);
            videos.remove(videoDetails);
            moveFile(f.getParent()+"/", f.getName(),Environment.getExternalStorageDirectory().getPath()+"/DCIM/Videos/");
            Toasty.success(context,"Videos are Unhidden Successfully\n You can find it in /DCIM/Videos/ folder ",Toasty.LENGTH_LONG).show();        }
        notifyDataSetChanged();
    }

    public void shareAdapter(ArrayList<VideoDetails> selection_list) {

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
        shareIntent.putExtra(Intent.EXTRA_STREAM,selection_list);
        shareIntent.setType("video/*");
        context.startActivity(Intent.createChooser(shareIntent, "Share Videos to.."));
    }

    public static class myVideoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView icon;
        TextView videoName;
        CheckBox checkBox;
        View view;
        ImageView more_btn;
        Videos vid;

        public myVideoHolder(@NonNull View itemView, Videos v) {
            super(itemView);
            more_btn= itemView.findViewById(R.id.ic_more_btn);
            view = itemView.findViewById(R.id.video_list);
            icon= itemView.findViewById(R.id.video_icon);
            videoName=itemView.findViewById(R.id.videoTitle);
            checkBox=itemView.findViewById(R.id.checkbox_video);
            vid=v;
            view.setOnLongClickListener(vid);
            checkBox.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            videoName.setOnClickListener(null);
            icon.setOnClickListener(null);
            vid.prepareSelection(v.getRootView(),getAdapterPosition());
        }
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
        }
        catch (FileNotFoundException f) {
            Log.e("File", f.getMessage());
        }
        catch (Exception e) {
            Log.e("tag", e.getMessage());
        }
    }

}
