package com.azoroapps.calcVault.adapter;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.azoroapps.calcVault.view.Album;
import com.azoroapps.calcVault.view.Photos;
import com.azoroapps.calcVault.R;
import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;

import es.dmoral.toasty.Toasty;
public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.MyViewHolder>  {
    ArrayList<String> albumNames;
    ArrayList<String> albumPhotos;
    Context context;
    String albumName;

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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        albumName =albumNames.get(position);
        File f = new File(Environment.getExternalStorageDirectory().getPath()+"/.vault/.Photos/"+albumName+"/");
        ArrayList<File> files = new ArrayList<>();
        Collections.addAll(files, f.listFiles());
        int length=files.size();

        holder.textView.setText(albumNames.get(position).substring(1)+"("+length+")");
        if(files.size()>0){
            Glide.with(context)
                    .load(f.getPath()+"/"+files.get(0).getName())
                    .centerInside()
                    .placeholder(R.drawable.album)
                    .into(holder.imageView);
        }

        //holder.imageView.setImageResource());
        holder.relativeLayout.setOnClickListener(v -> {
            Intent intent = new Intent(context, Photos.class);
            intent.putExtra("AlbumName",albumName);
            context.startActivity(intent);
            ((Album)context).finish();
        });

        holder.relativeLayout.setOnLongClickListener(v -> {
            PopupMenu popup= new PopupMenu(context, holder.relativeLayout);
            popup.inflate(R.menu.menu_album_longclickoptions);
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    File f1 = new File(Environment.getExternalStorageDirectory().getPath()+"/.vault/.Photos/"+albumName+"/");
                    String outputPath =Environment.getExternalStorageDirectory().getPath()+"/DCIM/Photos/";
                    if (item.getItemId() == R.id.album_delete) {
                        AlertDialog.Builder builder = new AlertDialog.Builder((context));
                        builder.setIcon(R.drawable.ic_error_48px);
                        builder.setTitle("Warning: Delete");
                        builder.setMessage("Delete the file permanently ?");
                        builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(f1.exists()){
                                    String[]entries = f1.list();
                                    for(String s: entries){
                                        File currentFile = new File(f1.getPath(),s);
                                        currentFile.delete();
                                    }
                                    boolean b = f1.delete();
                                    if(b)
                                        Toasty.success(context, "Deleted the Album and the Contents", Toasty.LENGTH_SHORT).show();
                                    albumNames.remove(holder.getAdapterPosition());
                                    notifyItemRemoved(holder.getAdapterPosition());
                                }
                            }
                        });
                        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        builder.create().show();

                    }
                    if(item.getItemId()==R.id.album_unhide){
                        AlertDialog.Builder builder = new AlertDialog.Builder((context));
                        //builder.setIcon(R.drawable.ic_error_48px);
                        builder.setTitle("Un-Hide");
                        builder.setMessage("Move the Album back to the \nDCIM folder?");
                        builder.setPositiveButton("Un-Hide", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(f1.exists()){
                                    String[]entries = f1.list();
                                    for(String s: entries){
                                        File currentFile = new File(f1.getPath(),s);
                                        moveFile(currentFile.getParent()+"/",currentFile.getName(),outputPath+albumName.substring(1)+"/");
                                    }
                                    boolean b = f1.delete();
                                    if(b) Toasty.success(context, "Moved the Album and the Contents", Toasty.LENGTH_SHORT).show();
                                    albumNames.remove(holder.getAdapterPosition());
                                    notifyItemRemoved(holder.getAdapterPosition());
                                }

                            }
                        });
                        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        builder.create().show();
                    }
                    if(item.getItemId()==R.id.album_rename){
                        //TODO one popup that asks the user for the new FOlder name, store it in a string and use it below
                        AlertDialog.Builder alert = new AlertDialog.Builder(
                                context);
                        alert.setTitle("Rename");
                        final EditText input = new EditText(context);
                        alert.setView(input);
                        alert.setPositiveButton("OK", (dialog, whichButton) -> {
                            String srt1 = input.getEditableText().toString();
                            File oldFolder = new File(f1.getPath());
                            File newFolder = new File(f1.getParent(),"."+srt1);
                            boolean success = oldFolder.renameTo(newFolder);
                            if(success){
                                ((Album)context).finish();
                                context.startActivity(((Album) context).getIntent());
                                Toasty.success(context,"Folder Renamed, Go Back and come to see Changes",Toasty.LENGTH_SHORT).show();
                            }
                        });
                        alert.setNegativeButton("CANCEL",
                                (dialog, whichButton) -> dialog.cancel());
                        AlertDialog alertDialog = alert.create();
                        alertDialog.show();
                        return false;
                    }
                    return true;
                }
            });
            popup.show();
            return true;
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
        }
        catch (FileNotFoundException f) {
            Log.e("File", f.getMessage());
        }
        catch (Exception e) {
            Log.e("tag", e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return albumNames.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;
        RelativeLayout relativeLayout;
        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            relativeLayout = itemView.findViewById(R.id.album_id);
            imageView = itemView.findViewById(R.id.album_image);
            textView = itemView.findViewById(R.id.album_name);

        }
    }
}
