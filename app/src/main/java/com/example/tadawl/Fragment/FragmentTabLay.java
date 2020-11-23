package com.example.tadawl.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tadawl.Adapter.AdapterTabAds;
import com.example.tadawl.Model.ModelAds;
import com.example.tadawl.R;

import java.util.ArrayList;

public class FragmentTabLay extends Fragment {

    RecyclerView recyclerView;
    ArrayList <ModelAds> arrayList;
    AdapterTabAds adapterTabAds;
    private  String fragId;
    public FragmentTabLay(String fragmentId) {
        this.fragId = fragmentId;
    }

    public FragmentTabLay() {
        // Required empty public constructor
    }



    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tab_lay, container, false);
        init();
        return view;
    }

    LinearLayoutManager linearLayoutManager;
    private void init() {
        arrayList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recycler);
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true);
        recyclerView.setLayoutManager(linearLayoutManager);
        for (int i = 0; i < 10; i++) {
            ModelAds modelAds= new ModelAds();
            modelAds.setId(i+"");
            arrayList.add(modelAds);
        }
        adapterTabAds = new AdapterTabAds(getActivity(),arrayList);
        recyclerView.setAdapter(adapterTabAds);
    }
}