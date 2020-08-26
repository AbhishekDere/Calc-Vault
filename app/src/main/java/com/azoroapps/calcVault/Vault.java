package com.azoroapps.calcVault;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;

import es.dmoral.toasty.Toasty;

public class Vault extends AppCompatActivity {

    ImageView button_photo, button_videos, button_music, button_recordings, button_VPN, button_files, button_drive, button_settings;
    boolean success = true;
    String loc = "Vault";
    File file = new File(Environment.getExternalStorageDirectory() + "/" + loc);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_vault);
        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(Vault.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            int STORAGE_PERMISSION_CODE = 1;
            ActivityCompat.requestPermissions(Vault.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
        initControl();
        initControlListener();
    }

    private void initControl() {
        //Row 1
        button_photo = findViewById(R.id.ivPhotos);
        button_videos = findViewById(R.id.ivVideos);
        button_music = findViewById(R.id.ivMusic);
        //Row 2 recording,vpn, notes
        button_recordings = findViewById(R.id.ivRecordings);
        button_VPN = findViewById(R.id.ivVPN);
        //Row 3
        button_files = findViewById(R.id.ivDocuments);
        button_drive = findViewById(R.id.ivDrive);
        button_settings = findViewById(R.id.ivSettings);
    }

    private void initControlListener() {
        button_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(Vault.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Toasty.success(Vault.this, "Authorised", Toast.LENGTH_SHORT).show();
                    success = file.mkdirs();
                    if (success) {
                        Toasty.info(Vault.this, "Vault Created", Toast.LENGTH_SHORT).show();
                    }
                    //requestStoragePermission();
                }
                Intent intent = new Intent(getApplicationContext(), Album.class);
                startActivity(intent);
            }
        });

        button_videos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Album.class);
                startActivity(intent);

            }
        });
        button_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Album.class);
                startActivity(intent);
            }
        });
        button_recordings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Recordings.class);
                startActivity(intent);
            }
        });
        button_VPN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Album.class);
                startActivity(intent);
            }
        });

        button_files.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Album.class);
                startActivity(intent);
            }
        });
        button_drive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Album.class);
                startActivity(intent);
            }
        });
        button_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
            }
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
        Vault.this.finish();
        System.exit(0);

    }
}
