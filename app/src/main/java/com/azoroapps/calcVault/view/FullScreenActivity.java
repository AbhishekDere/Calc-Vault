package com.azoroapps.calcVault.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.viewpager.widget.ViewPager;


import com.azoroapps.calcVault.R;
import com.azoroapps.calcVault.adapter.FullSizeAdapter;

import java.util.ArrayList;

public class FullScreenActivity extends Activity {
    String[]images;
    int position;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);

        if(savedInstanceState==null){
            Intent i = getIntent();
            images=i.getStringArrayExtra("images");
            position=i.getIntExtra("position",0);
        }
        viewPager=findViewById(R.id.photoviewpager);
        FullSizeAdapter fullSizeAdapter= new FullSizeAdapter(getApplicationContext(),images);
        viewPager.setAdapter(fullSizeAdapter);
        viewPager.setCurrentItem(position,true);

    }
}