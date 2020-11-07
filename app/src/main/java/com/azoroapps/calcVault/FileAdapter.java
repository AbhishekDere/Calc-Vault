package com.azoroapps.calcVault;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.azoroapps.calcVault.utilities.FileItem;

import java.util.List;

public class FileAdapter extends BaseAdapter {
    private final Context context;
    private final List<FileItem> fileDataset;
    private final LayoutInflater inflater;

    public FileAdapter(Context context, List<FileItem> fileDataset) {
        this.context = context;
        this.fileDataset = fileDataset;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return fileDataset.size();
    }

    @Override
    public FileItem getItem(int position) {
        return fileDataset.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View root;
        if(convertView == null){
            root = inflater.inflate(R.layout.file_item,parent,false);
        }else{
            root = convertView;
        }

        ((ImageView)root.findViewById(R.id.imgIcon)).setImageResource(fileDataset.get(position).imgId);
        ((TextView)root.findViewById(R.id.txtFileName)).setText(fileDataset.get(position).txtFlName);
        ((TextView)root.findViewById(R.id.txtFileLastModified)).setText(fileDataset.get(position).txtFlMdfd);
        ((TextView)root.findViewById(R.id.txtFileSize)).setText(fileDataset.get(position).txtFsz);

        return root;
    }
}
