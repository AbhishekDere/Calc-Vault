package com.example.calc_vault;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.util.Set;

public class Vault extends AppCompatActivity {

    ImageView button_photo,button_videos,button_music,button_recordings,button_VPN,button_notes,button_files,button_drive,button_settings;


    @Override
    protected void onCreate (Bundle savedInstanceState) {
        setContentView(R.layout.activity_vault);
        super.onCreate(savedInstanceState);
        initControl();
        initControlListener();
    }

    private void initControl(){
        //Row 1
        button_photo = findViewById(R.id.ivPhotos);
        button_videos = findViewById(R.id.ivVideos);
        button_music = findViewById(R.id.ivMusic);
        //Row 2 recording,vpn, notes
        button_recordings = findViewById(R.id.ivRecordings);
        button_VPN=findViewById(R.id.ivVPN);
        button_notes=findViewById(R.id.ivNotes);
        //Row 3
        button_files = findViewById(R.id.ivDocuments);
        button_drive = findViewById(R.id.ivDrive);
        button_settings = findViewById(R.id.ivSettings);
    }

    private void initControlListener() {
            button_photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(),Album.class);
                    startActivity(intent);
                }
            });

            button_videos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(),Album.class);
                    startActivity(intent);
                }
            });
            button_music.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(),Album.class);
                    startActivity(intent);
                }
            });
            button_recordings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(),Album.class);
                    startActivity(intent);
                }
            });
            button_VPN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(),Album.class);
                    startActivity(intent);
                }
            });
            button_notes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(),Album.class);
                    startActivity(intent);
                }
            });
            button_files.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(),Album.class);
                    startActivity(intent);
                }
            });
            button_drive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(),Album.class);
                    startActivity(intent);
                }
            });
            button_settings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(),SettingsActivity.class);
                    startActivity(intent);
                }
            });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.vaultmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==(R.id.settings_icon)){
            Intent intent = new Intent(this,SettingsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
