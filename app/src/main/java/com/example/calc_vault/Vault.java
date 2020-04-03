package com.example.calc_vault;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class Vault extends AppCompatActivity {




    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                Intent intent = new Intent(getApplicationContext(),Photos.class);
                startActivity(intent);
            }
        });
    }
}
