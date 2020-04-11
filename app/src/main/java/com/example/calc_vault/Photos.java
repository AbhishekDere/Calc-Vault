package com.example.calc_vault;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.widget.Toolbar;

import java.util.ArrayList;

public class Photos extends AppCompatActivity {
    private ArrayList albums;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
    }

}
