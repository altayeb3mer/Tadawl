package com.example.tadawl.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.tadawl.Adapter.ViewPagerAdapter;
import com.example.tadawl.Fragment.FragmentAddCar;
import com.example.tadawl.Fragment.FragmentAddEstate;
import com.example.tadawl.Fragment.FragmentTabLay;
import com.example.tadawl.R;
import com.example.tadawl.Utils.CustomViewPager;
import com.google.android.material.tabs.TabLayout;

public class AddNewPost extends AppCompatActivity {
    AppCompatButton btn;

    TabLayout tableLayout;
    CustomViewPager customViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_post);
        init();
        initTabLayout();
    }



    private void init() {
        tableLayout = findViewById(R.id.tabLayout);
        customViewPager = findViewById(R.id.viewpager);

    }
    private void initTabLayout() {
        FragmentManager fragmentManager=getSupportFragmentManager();
        ViewPagerAdapter adapter = new ViewPagerAdapter(fragmentManager);

        FragmentAddEstate fragmentAddEstate = new FragmentAddEstate();
        FragmentAddCar fragmentAddCar =new FragmentAddCar();

        adapter.addFragment(fragmentAddEstate,"اضافة عقار");
        adapter.addFragment(fragmentAddCar,"اضافة سيارة");

        customViewPager.setAdapter(adapter);
        tableLayout.setupWithViewPager(customViewPager);

    }
}