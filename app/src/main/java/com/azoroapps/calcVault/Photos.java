package com.azoroapps.calcVault;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.io.Files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class Photos extends AppCompatActivity {
    private static InputStream inStream;
    private static OutputStream outStream;
    final int REQUEST_EXTERNAL_STORAGE = 100;
    RecyclerView recyclerView;
    PhotosAdapter photosAdapter;
    File photoPath;
    ArrayList<ImageDetails> img = new ArrayList<>();
    ImageDetails imageDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent intent = getIntent();
        String albumName = Objects.requireNonNull(intent.getExtras()).getString("AlbumName");
        photoPath = new File(Environment.getExternalStorageDirectory() + "/Vault/" + albumName);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        listingPhotos();
    }

    private void listingPhotos() {
        photosAdapter = new PhotosAdapter(this, getData());
        recyclerView = findViewById(R.id.photo_id);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(photosAdapter);
    }

    private ArrayList<ImageDetails> getData() {
        if (photoPath.exists()) {
            File[] files = photoPath.listFiles();
            for (File file : files) {
                imageDetails = new ImageDetails();
                imageDetails.setName(file.getName());
                imageDetails.setUri(Uri.fromFile(file));
                img.add(imageDetails);
            }
        }
        return img;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_photos, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.AddNewPhotos) {
            launchGalleryIntent();
        }
        return super.onOptionsItemSelected(item);
    }

    public void launchGalleryIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_EXTERNAL_STORAGE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK && requestCode == REQUEST_EXTERNAL_STORAGE) {
            if(data.getClipData() != null) {
                ClipData clipData = data.getClipData();
                int count = clipData.getItemCount();
                for(int i = 0; i < count; i++){
                    Uri uri = clipData.getItemAt(i).getUri();
                        File src = new File(uri.getPath());
                        try {
                            //InputStream src=getContentResolver().openInputStream(uri);
                            Files.move(src,photoPath);
                            Toasty.error(this,"File Moved",Toasty.LENGTH_SHORT).show();
                        } catch (FileNotFoundException e) {
                            Toasty.error(this,"File Not Found",Toasty.LENGTH_SHORT).show();
                            e.printStackTrace();
                        } catch (IOException e) {
                            Toasty.error(this,"IO Exception",Toasty.LENGTH_SHORT).show();
                        }
                }
                Toasty.info(this,"Selected"+count+"Images",Toasty.LENGTH_SHORT).show();
            }
        }
    }
}

