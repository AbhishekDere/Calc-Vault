package com.example.calc_vault;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

public class Vault extends AppCompatActivity {




    @Override
    protected void onCreate (Bundle savedInstanceState) {
        setContentView(R.layout.activity_vault);
        //Setting the Buttons
        ImageView button_photo = findViewById(R.id.imageView);
        ImageView button_videos = findViewById(R.id.imageView2);
        ImageView button_files = findViewById(R.id.imageView3);
        ImageView button_audio = findViewById(R.id.imageView4);
        ImageView button_settings = findViewById(R.id.imageView5);
        ImageView button_encryption = findViewById(R.id.imageView6);
        //Setting OnClickListeners to Each Buttons
        button_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Album.class);
                startActivity(intent);
            }
        });
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.vaultmenu,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case(R.id.settings_icon):
                Intent intent = new Intent(getApplicationContext(),SettingsActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
