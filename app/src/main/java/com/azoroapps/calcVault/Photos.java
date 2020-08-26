package com.azoroapps.calcVault;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class Photos extends AppCompatActivity {
    final int REQUEST_EXTERNAL_STORAGE = 100;
    RecyclerView recyclerView;
    PhotosAdapter photosAdapter;
    File photoPath;
    ArrayList<ImageDetails> img = new ArrayList<>();
    ImageDetails imageDetails;
    public static void moveFile(File from, File to) throws IOException {
//  Files.move(source, target, REPLACE_EXISTING);
//        File file_Source = new File(path_source);
//        File file_Destination = new File(path_destination);
//        FileChannel source = null;
//        FileChannel destination = null;
//        try {
//            source = new FileInputStream(file_Source).getChannel();
//            destination = new FileOutputStream(file_Destination).getChannel();
//
//            long count = 0;
//            long size = source.size();
//            while((count += destination.transferFrom(source, count, size-count))<size);
//            file_Source.delete();
//        }catch (Exception ex){
//            ex.printStackTrace();
//        } finally {
//            if(source != null) {
//                source.close();
//            }
//            if(destination != null) {
//                destination.close();
//            }
//        }
        from.renameTo(to);
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
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        String albumName = Objects.requireNonNull(intent.getExtras()).getString("AlbumName");
        photoPath = new File(Environment.getExternalStorageDirectory().getPath() + "/Vault/" + albumName+"/");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        listingPhotos();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK && requestCode == REQUEST_EXTERNAL_STORAGE) {
            ClipData clipData = data.getClipData();
            List<Bitmap> bitmaps = new ArrayList<>();
            if(clipData!=null){
                for(int i = 0; i < clipData.getItemCount(); i++){
                    Uri imageUri= clipData.getItemAt(i).getUri();
                    File imageFile = new File(imageUri.getPath());
                    Toasty.info(this,"Selected"+clipData.getItemCount()+"Images",Toasty.LENGTH_SHORT).show();
                    try {
                        InputStream is = getContentResolver().openInputStream(imageUri);
                        Bitmap bitmap= BitmapFactory.decodeStream(is);
                        bitmaps.add(bitmap);
                        //moveFile(imageFile.toString(),photoPath.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            else{
                Uri imageUri= data.getData();
                assert imageUri != null;
                File imageFile = new File(Environment.getExternalStorageDirectory().getPath()+imageUri.getPath());
                File p =  new File(photoPath.toString()+"/"+imageFile.getName());
                try {
                    Toasty.info(this,"Source:"+imageFile.toString(),Toasty.LENGTH_SHORT).show();
                    Toasty.info(this,"Dest:"+photoPath.toString(),Toasty.LENGTH_SHORT).show();
                    InputStream is = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap= BitmapFactory.decodeStream(is);
                    bitmaps.add(bitmap);
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        photoPath.mkdirs();
                        imageFile.renameTo(p);
                       //moveFile(imageFile,photoPath);
                    } else {
                        // Request permission from the user
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}

