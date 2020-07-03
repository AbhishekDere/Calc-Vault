package com.azoroapps.calcVault;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;



public class Photos extends AppCompatActivity {
    ArrayList<ImageDetails> photoNames;
    RecyclerView recyclerView;
    PhotosAdapter photosAdapter;
    String albumName;
    File photoPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        String albumName = Objects.requireNonNull(intent.getExtras()).getString("AlbumName");
        photoPath = new File(Environment.getExternalStorageDirectory()+"/Vault/"+albumName);
        //photoNames=new ArrayList<>(Arrays.asList(Objects.requireNonNull(albumPath.listFiles())));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        listingPhotos();
    }
    private void listingPhotos(){
        photosAdapter = new PhotosAdapter(this,getData());
        recyclerView= findViewById(R.id.photo_id);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setAdapter(photosAdapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_photos,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
    private ArrayList<ImageDetails> getData(){

        ArrayList<ImageDetails> img = new ArrayList<>();
        ImageDetails imageDetails;
        if(photoPath.exists()){
            File[] files = photoPath.listFiles();
            for(int i =0;i<files.length;i++){
                File file=files[i];
                imageDetails=new ImageDetails();
                imageDetails.setName(file.getName());
                imageDetails.setUri(Uri.fromFile(file));
                img.add(imageDetails);
                            }
        }
        return img;
    }
}