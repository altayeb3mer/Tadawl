package com.example.tadawl.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.tadawl.Adapter.AdapterNewAds;
import com.example.tadawl.Model.ModelNewAds;
import com.example.tadawl.R;

import java.util.ArrayList;

public class MyAds extends AppCompatActivity {

    Spinner spinnerCategory;
    String[] array;
    RecyclerView recycler;
    AdapterNewAds adapterNewAds;
    ArrayList<ModelNewAds> arrayList;
    GridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ads);
        initSpinner();
        init();
    }

    private void init() {
        gridLayoutManager = new GridLayoutManager(this,3,GridLayoutManager.VERTICAL,false);
        recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(gridLayoutManager);

        initAdapterNewAds();

    }
    private void initAdapterNewAds() {
        arrayList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ModelNewAds modelNewAds=new ModelNewAds();
            modelNewAds.setId(i+"");
            arrayList.add(modelNewAds);
        }
        adapterNewAds = new AdapterNewAds(this,arrayList);
        recycler.setAdapter(adapterNewAds);

    }

    private void initSpinner(){

        //month array
        spinnerCategory = findViewById(R.id.spinner);
        array = new String[]{"عرض الكل", "عقارات", "سيارات"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, R.layout.spinner_item, array) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = null;
                v = super.getDropDownView(position, null, parent);
                // If this is the selected item position
//                if (position == 0) {
//                    v.setBackgroundColor(Color.WHITE);
//                } else {
//                    if (position % 2 == 0) {
//                        v.setBackgroundColor(getResources().getColor(R.color.spinner_bg_design1));
//                    } else {
//                        v.setBackgroundColor(getResources().getColor(R.color.spinner_bg_design2));
//                    }
//
//                }
                return v;
            }
        };
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter1);
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
//                    s_month = "";
                } else {
//                    s_month = array_month[position];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}