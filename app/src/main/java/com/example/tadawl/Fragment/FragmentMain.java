package com.example.tadawl.Fragment;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tadawl.Adapter.AdapterTabAds;
import com.google.android.material.tabs.TabLayout;

import com.example.tadawl.R;
import com.example.tadawl.Utils.CustomViewPager;
import com.example.tadawl.Utils.ViewPagerAdapter;


public class FragmentMain extends Fragment {


    public FragmentMain() {
        // Required empty public constructor
    }
    Activity activity;
    public FragmentMain(Activity activity) {
        // Required empty public constructor
        this.activity=activity;
    }

    View view;
    TabLayout tableLayout;
    CustomViewPager customViewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_main2, container, false);
        init();


        return view;
    }

    private void init() {
        tableLayout = view.findViewById(R.id.tabLayout);
        customViewPager = view.findViewById(R.id.viewpager);

        initTabLayout();

    }

    private void initTabLayout() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());

        FragmentTabLay fragmentTabLay1 = new FragmentTabLay("1");
        FragmentTabLay fragmentTabLay2 = new FragmentTabLay("2");
        FragmentTabLay fragmentTabLay3 = new FragmentTabLay("3");

        adapter.addFragment(fragmentTabLay1,"لك");
        adapter.addFragment(fragmentTabLay2,"اعلانات متداولة");
        adapter.addFragment(fragmentTabLay3,"كل الاعلانات");
        customViewPager.setAdapter(adapter);
        tableLayout.setupWithViewPager(customViewPager);

    }

}