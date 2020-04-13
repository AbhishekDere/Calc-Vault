package com.example.calc_vault;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.widget.Toolbar;

import java.util.ArrayList;

public class Photos extends AppCompatActivity {
    private ArrayList albums;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] albums ={"aaa","bb","cc","dd","ee","ff","gg","hh","ii","jj"};
        setContentView(R.layout.activity_photos);
        RecyclerView recyclerView= findViewById(R.id.recycler_id);
        AlbumAdapter albumAdapter = new AlbumAdapter(albums);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(albumAdapter);
    }

}
