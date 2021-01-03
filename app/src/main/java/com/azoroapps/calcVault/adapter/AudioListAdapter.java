package com.azoroapps.calcVault.adapter;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.azoroapps.calcVault.R;
import com.azoroapps.calcVault.utilities.TimeAgo;
import com.azoroapps.calcVault.view.Album;
import com.azoroapps.calcVault.view.RecordingListFragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;

import es.dmoral.toasty.Toasty;

public class AudioListAdapter extends RecyclerView.Adapter<AudioListAdapter.AudioViewHolder> {

    private final File[] allFiles;
    private final ArrayList<File> musicFiles= new ArrayList<>();
    private TimeAgo timeAgo;
    Context context;
    private final onItemListClick onItemListClick;

    public AudioListAdapter(File[] allFiles, onItemListClick onItemListClick, Context context) {
        this.allFiles = allFiles;
        this.onItemListClick = onItemListClick;
        this.context=context;
    }

    @NonNull
    @Override
    public AudioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_list_item, parent, false);
        timeAgo = new TimeAgo();
        return new AudioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AudioViewHolder holder, int position) {
        holder.list_title.setText(allFiles[position].getName());
        holder.list_date.setText(timeAgo.getTimeAgo(allFiles[position].lastModified()));
        Collections.addAll(musicFiles, allFiles);
    }


    @Override
    public int getItemCount() {
        return allFiles.length;
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

    public interface onItemListClick {
        void onClickListener(File file, int position);
    }

    public class AudioViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView list_image;
        private final TextView list_title;
        private final TextView list_date;
        ConstraintLayout musicPlayerLayout;

        public AudioViewHolder(@NonNull View itemView) {
            super(itemView);
            musicPlayerLayout=itemView.findViewById(R.id.musicplayerlayout);
            list_image = itemView.findViewById(R.id.list_image_view);
            list_title = itemView.findViewById(R.id.list_title);
            list_date = itemView.findViewById(R.id.list_date);
            itemView.setOnClickListener(this);
            musicPlayerLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    PopupMenu popup = new PopupMenu(context, musicPlayerLayout);
                    popup.inflate(R.menu.recording_long_click);

                    popup.setOnMenuItemClickListener(item -> {
                        if (item.getItemId() == R.id.recording_delete) {
                            boolean b = allFiles[getAdapterPosition()].delete();
                            if (b) {
                                Toasty.info(context, "Deleted " + allFiles[getAdapterPosition()].getName(), Toasty.LENGTH_LONG).show();
                                ((FragmentActivity)context).finish();
                                context.startActivity(((FragmentActivity) context).getIntent());
                                //notifyDataSetChanged();
                                musicFiles.remove(getAdapterPosition());
                                notifyItemRemoved(getAdapterPosition());
                            }
                        }

                        return false;
                    });
                    popup.show();
                    return true;
                }
            });

        }
        @Override
        public void onClick(View v) {
            onItemListClick.onClickListener(allFiles[getAdapterPosition()], getAdapterPosition());
        }


    }
}