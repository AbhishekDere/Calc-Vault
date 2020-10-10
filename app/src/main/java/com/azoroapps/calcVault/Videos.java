package com.azoroapps.calcVault;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
            //Toasty.info(this,"Files Hidden, Please Delete the Original Images",Toasty.LENGTH_SHORT).show();
        }
        catch (FileNotFoundException f) {
            Log.e("File", f.getMessage());
        }
        catch (Exception e) {
            Log.e("tag", e.getMessage());
        }
    }
}