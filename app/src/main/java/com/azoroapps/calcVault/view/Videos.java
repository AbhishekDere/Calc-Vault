package com.azoroapps.calcVault.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.azoroapps.calcVault.R;
import com.azoroapps.calcVault.RealPathUtil;
import com.azoroapps.calcVault.VideoDetails;
import com.azoroapps.calcVault.adapter.VideoAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class Videos extends AppCompatActivity {
    File videoPath= new File((Environment.getExternalStorageDirectory().getPath() + "/.Vault/.Videos/"));
    ArrayList<VideoDetails> vid = new ArrayList<>();
    VideoDetails videoDetails;
    RecyclerView recyclerView;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);
        listingVideos();
    }

    private void listingVideos() {
        VideoAdapter videoAdapter=new VideoAdapter(this,getData());
        recyclerView = findViewById(R.id.recyclerView_video);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(videoAdapter);
    }

    private ArrayList<VideoDetails> getData() {
        if (videoPath.exists()) {
            File[] files = videoPath.listFiles();
            for (File file : files) {
                videoDetails = new VideoDetails();
                videoDetails.setName(file.getName());
                videoDetails.setUri(Uri.fromFile(file));
                vid.add(videoDetails);
            }
        }
        return vid;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_videos, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.AddNewVideos) {
            launchGalleryIntent();
        }
        return super.onOptionsItemSelected(item);
    }

    public void launchGalleryIntent() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "Select Video"), 66);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==66 && resultCode==RESULT_OK&& data!=null) {
            int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        PERMISSIONS_STORAGE,
                        REQUEST_EXTERNAL_STORAGE
                );
            }
            ClipData clipData = data.getClipData();
            String outputPath= Environment.getExternalStorageDirectory().getAbsolutePath()+"/.Vault/.Videos/";
            if(clipData==null){
                //Single Image Selection
                Uri uri = data.getData();
                assert uri != null;
                String lp;
                lp = RealPathUtil.getRealPath(this, uri);
                // Get the file instance
                File file = new File(lp);
                String inputPath= file.getParent()+"/";
                String inputFile = file.getName();
                moveFile(inputPath,inputFile,outputPath);
            }
            else{
                //Multiple Images Selection
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    Uri uri = clipData.getItemAt(i).getUri();
                    String path;
                    path = RealPathUtil.getRealPath(Videos.this, uri);
                    // Get the file instance
                    File file = new File(path);
                    String inputPath= file.getParent() +"/";
                    String inputFile = file.getName();
                    moveFile(inputPath,inputFile,outputPath);
                }
            }
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
                    Toasty.info(this,"Hiding Images",Toasty.LENGTH_SHORT).show();
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