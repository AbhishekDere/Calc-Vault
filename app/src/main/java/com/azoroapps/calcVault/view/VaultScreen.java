package com.azoroapps.calcVault.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.azoroapps.calcVault.DriveActivity;
import com.azoroapps.calcVault.R;
import com.azoroapps.calcVault.TempActivity;

import java.io.File;
import java.io.IOException;
import es.dmoral.toasty.Toasty;

public class VaultScreen extends AppCompatActivity {

    ImageView button_photo, button_videos, button_music,
            button_recordings, button_VPN, button_files,
            button_drive, button_settings;
    boolean success = true;
    String loc = ".Vault";
    File file = new File(Environment.getExternalStorageDirectory() + "/" + loc);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_vault);
        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(VaultScreen.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            int STORAGE_PERMISSION_CODE = 1;
            ActivityCompat.requestPermissions(VaultScreen.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
        File noMediaFile = new File(file+"/.nomedia");
        if(!noMediaFile.exists()){
            try {
                noMediaFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        initControl();
        initControlListener();
    }

    private void initControl() {
        //Row 1
        button_photo = findViewById(R.id.ivPhotos);
        button_videos = findViewById(R.id.ivVideos);
        //button_music = findViewById(R.id.ivMusic);
        //Row 2 recording,vpn, notes
        button_recordings = findViewById(R.id.ivRecordings);
        button_VPN = findViewById(R.id.ivVPN);
        //Row 3
        button_files = findViewById(R.id.ivDocuments);
        button_drive = findViewById(R.id.ivDrive);
        button_settings = findViewById(R.id.ivSettings);
    }

    private void initControlListener() {
        button_photo.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(VaultScreen.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                success = file.mkdirs();
                if (success) {
                    Toasty.info(VaultScreen.this, "Vault Created", Toast.LENGTH_SHORT).show();
                }
                //requestStoragePermission();
            }
            Intent intent = new Intent(getApplicationContext(), Album.class);
            intent.putExtra("name","/.Photos/");
            startActivity(intent);
        });

        button_videos.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Videos.class);
            startActivity(intent);

        });
        /*utton_music.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MusicPlayer.class);
            startActivity(intent);

        });*/
        button_recordings.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Recordings.class);
            intent.putExtra("name","Recordings");
            startActivity(intent);
        });
        button_VPN.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), VpnActivity.class);
            startActivity(intent);
        });

        button_files.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), NotesActivity.class);
            startActivity(intent);
        });
        button_drive.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Album.class);
            startActivity(intent);
        });
        button_settings.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vault, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == (R.id.settings_icon)) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
        if (item.getItemId() == (R.id.myDrive)) {
            Intent intent = new Intent(this, DriveActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        VaultScreen.this.finish();
        System.exit(0);

    }
}
