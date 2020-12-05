package com.azoroapps.calcVault.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.azoroapps.calcVault.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class FullSizeAdapter extends PagerAdapter {
    Context context;
    String[] imagesLocation;
    LayoutInflater inflator;
    public FullSizeAdapter(Context context, String[] images) {
        this.context=context;
        this.imagesLocation=images;
    }

    @Override
    public int getCount() {
        return imagesLocation.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        inflator= (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.full_item,null);
        ImageView imageView=v.findViewById(R.id.img);
        Glide.with(context).load(imagesLocation[position]).apply(new RequestOptions().centerInside()).into(imageView);
        ViewPager vp=(ViewPager)container;
         vp.addView(v,0);
         return v;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        //super.destroyItem(container, position, object);
        ViewPager viewPager=(ViewPager)container;
        View v=(View)object;
        viewPager.removeView(v);
    }
}
