package com.azoroapps.calcVault.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.azoroapps.calcVault.R;
import com.azoroapps.calcVault.utilities.RealPathUtil;
import com.azoroapps.calcVault.adapter.VideoAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
@SuppressLint("SetTextI18n")
public class Videos extends AppCompatActivity implements View.OnLongClickListener{
    File videoPath= new File((Environment.getExternalStorageDirectory().getPath() + "/.Vault/.Videos/"));
    ArrayList<VideoDetails> vid = new ArrayList<>();
    VideoDetails videoDetails;
    RecyclerView recyclerView;
    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public boolean is_in_action_mode=false;
    TextView counter_text_view;


    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    VideoAdapter videoAdapter;
    Toolbar toolbar;
    ArrayList<VideoDetails> selection_list= new ArrayList<>();
    ArrayList<Uri> videoUris=new ArrayList<>();
    int counter=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);
        toolbar=findViewById(R.id.toolbar_selection);
        setSupportActionBar(toolbar);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        counter_text_view=findViewById(R.id.counter_text);
        listingVideos();
    }

    private void listingVideos() {
        videoAdapter=new VideoAdapter(this,getData());
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.AddNewVideos) {
            launchGalleryIntent();
        }
        else if(item.getItemId()==R.id.video_select){
            toolbar.getMenu().clear();
            toolbar.inflateMenu(R.menu.menu_action_mode);
            counter_text_view.setVisibility(View.VISIBLE);
            is_in_action_mode=true;
            videoAdapter.notifyDataSetChanged();
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }
        else if(item.getItemId()==R.id.video_delete){
            is_in_action_mode=false;
            videoAdapter.updateAdapter(selection_list);
            clearActionMode();
        }
        else if(item.getItemId()==R.id.video_unHide){
            is_in_action_mode=false;
            videoAdapter.unHideAdapter(selection_list);
            clearActionMode();
        }
        else if (item.getItemId()==R.id.video_share){
            is_in_action_mode=false;
            clearActionMode();
            videoAdapter.shareAdapter(selection_list);
            clearActionMode();
        }
        else if(item.getItemId()==android.R.id.home){
            clearActionMode();
            videoAdapter.notifyDataSetChanged();
        }
        return true;
    }

    public void clearActionMode(){
        is_in_action_mode=false;
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.menu_videos);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        counter_text_view.setVisibility(View.GONE);
        counter_text_view.setText("0 Items Selected");
        counter=0;
        selection_list.clear();
    }

    public void prepareSelection(View view, int position){
        if(((CheckBox)view).isChecked()){
            selection_list.add(vid.get(position));
            counter=counter+1;
            videoUris.add(vid.get(position).getUri());
        }
        else{
            selection_list.remove(vid.get(position));
            videoUris.remove(vid.get(position).getUri());
            counter=counter-1;
        }
        updateCounter(counter);
    }
    public void updateCounter(int counter){
        if(counter==0){
            counter_text_view.setText("0 Items Selected");
        }
        else{
            counter_text_view.setText(counter+"Items Selected");
        }
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
                //Single Video Selection
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
                //Multiple Videos Selection
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
        finish();
        startActivity(getIntent());
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
            if(l)
                Toasty.success(this,"Deleted",Toasty.LENGTH_SHORT).show();
        }
        catch (FileNotFoundException f) {
            Log.e("File", f.getMessage());
        }
        catch (Exception e) {
            Log.e("tag", e.getMessage());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.menu_action_mode);
        counter_text_view.setVisibility(View.VISIBLE);
        is_in_action_mode=true;
        videoAdapter.notifyDataSetChanged();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        return false;
    }

    @Override
    public void onBackPressed() {
        if(is_in_action_mode){
            clearActionMode();
            videoAdapter.notifyDataSetChanged();
        }
        else {
            super.onBackPressed();
        }
    }
}