package com.azoroapps.calcVault;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class Photos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        Intent intent = getIntent();
        String AlbumName = Objects.requireNonNull(intent.getExtras()).getString("AlbumName");
        Toasty.success(this,"Files in "+AlbumName+" "+"Folder will be Displayed here",Toasty.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_photos,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}