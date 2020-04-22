package com.example.calc_vault;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Album extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        String[] albums ={"aaa","bb","cc","dd","ee","ff","gg","hh","ii","jj"};
        RecyclerView recyclerView= findViewById(R.id.recycler_id);
        AlbumAdapter albumAdapter = new AlbumAdapter(albums);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(albumAdapter);
    }

}
