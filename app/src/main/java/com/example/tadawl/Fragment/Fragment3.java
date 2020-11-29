package com.example.tadawl.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tadawl.Adapter.AdapterNewAds;
import com.example.tadawl.Model.ModelNewAds;
import com.example.tadawl.R;

import java.util.ArrayList;


public class Fragment3 extends Fragment {


    public Fragment3() {
        // Required empty public constructor
    }

    View view;
    RecyclerView recycler;
    AdapterNewAds adapterNewAds;
    ArrayList<ModelNewAds> arrayList;
    GridLayoutManager gridLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_3, container, false);
        init();
        return view;
    }


    private void init() {
        gridLayoutManager = new GridLayoutManager(getContext(),3,GridLayoutManager.VERTICAL,false);
        recycler = view.findViewById(R.id.recycler);
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
        adapterNewAds = new AdapterNewAds(getActivity(),arrayList);
        recycler.setAdapter(adapterNewAds);

    }


}