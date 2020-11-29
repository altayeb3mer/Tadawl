package com.example.tadawl.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.tadawl.Adapter.SlideShow_adapter;
import com.example.tadawl.Model.ModelSlideImg;
import com.example.tadawl.R;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;

public class PostDetails extends AppCompatActivity {

    ViewPager viewPager;
    SlideShow_adapter slideShowAdapter;
    ArrayList<ModelSlideImg> arrayList;
    CircleIndicator circleIndicator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        init();
    }

    private void init() {
        arrayList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ModelSlideImg modelSlideImg = new ModelSlideImg();
            modelSlideImg.setId(i+"");
            arrayList.add(modelSlideImg);
        }
        viewPager = findViewById(R.id.viewpager);
        slideShowAdapter = new SlideShow_adapter(getApplicationContext(),arrayList);
        circleIndicator = findViewById(R.id.indicator);
        viewPager.setAdapter(slideShowAdapter);
        circleIndicator.setViewPager(viewPager);

    }
}