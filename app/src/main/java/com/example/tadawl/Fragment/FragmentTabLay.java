package com.example.tadawl.Fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tadawl.Adapter.AdapterTabAds;
import com.example.tadawl.Model.ModelAds;
import com.example.tadawl.Model.ModelState;
import com.example.tadawl.R;
import com.example.tadawl.Utils.Api;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

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
    LinearLayout progressLay;
    RelativeLayout container2;
    LinearLayoutManager linearLayoutManager;

    Context context;
    @Override
    public void onAttach(@NonNull Context context) {
        this.context = context;
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tab_lay, container, false);
        init();
        return view;
    }

    private void init() {
        container2 = view.findViewById(R.id.container);
        progressLay = view.findViewById(R.id.progressLay);
        switch (fragId){
            case "2":{
                getPopular();
                break;
            }
            case "3":{
                getAll();
                break;
            }
        }
    }
    private void getPopular() {
        arrayList = new ArrayList<>();
        progressLay.setVisibility(View.VISIBLE);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        okhttp3.Request.Builder ongoing = chain.request().newBuilder();
                        ongoing.addHeader("Content-Type", "application/json;");
                        ongoing.addHeader("Content-Type", "application/x-www-form-urlencoded");

                        return chain.proceed(ongoing.build());
                    }
                })
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.ROOT_URL)
                .client(httpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api.RetrofitGetPopular service = retrofit.create(Api.RetrofitGetPopular.class);
        HashMap<String, String> hashMap=new HashMap<>();
        hashMap.put("type","popular");
        Call<String> call = service.putParam(hashMap);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                try {
                    JSONObject object = new JSONObject(response.body());
                    String statusCode = object.getString("status_code");
                    JSONArray arrayData = object.getJSONArray("data");
//                    String data = object.getString("data");
//                    JSONArray arrayData=new JSONArray(data);
                    switch (statusCode) {
                        case "200": {

                            for (int i = 0; i < arrayData.length(); i++) {
                                JSONObject object1 = arrayData.getJSONObject(i);

                                ModelAds modelAds = new ModelAds();
                                modelAds.setId(object1.getString("id"));
                                modelAds.setTitle(object1.getString("title"));
                                modelAds.setDescription(object1.getString("description"));
                                modelAds.setPrice(object1.getString("price"));
                                modelAds.setViews(object1.getString("views"));
                                modelAds.setRating(object1.getString("rating"));
                                modelAds.setImage(object1.getString("image"));
                                modelAds.setType(object1.getString("type"));

                                arrayList.add(modelAds);
                            }
                            if (arrayList.size() > 0) {
                                initAdapter(arrayList);
                            } else {
                                Toast.makeText(context, "الرجاء المحاوله لاحقا", Toast.LENGTH_SHORT).show();
                            }

                            break;
                        }

                        default: {
                            Toast.makeText(context, "حدث خطأ الرجاء المحاولة مرة اخرى", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "حدث خطأ الرجاء المحاولة مر اخرى", Toast.LENGTH_SHORT).show();
//                    showSnackBar("حدث خطأ الرجاء المحاولة مر اخرى");
                }
                progressLay.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                Toast.makeText(context, "حدث خطأ الرجاء المحاولة مر اخرى", Toast.LENGTH_SHORT).show();
//                showSnackBar("حدث خطأ الرجاء المحاولة مر اخرى");
                progressLay.setVisibility(View.GONE);
            }
        });
    }
    private void getAll() {
        arrayList = new ArrayList<>();
        progressLay.setVisibility(View.VISIBLE);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        okhttp3.Request.Builder ongoing = chain.request().newBuilder();
                        ongoing.addHeader("Content-Type", "application/json;");
                        ongoing.addHeader("Content-Type", "application/x-www-form-urlencoded");

                        return chain.proceed(ongoing.build());
                    }
                })
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.ROOT_URL)
                .client(httpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api.RetrofitGetAll service = retrofit.create(Api.RetrofitGetAll.class);
        Call<String> call = service.putParam();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                try {
                    JSONObject object = new JSONObject(response.body());
                    String statusCode = object.getString("status_code");
                    JSONArray arrayData = object.getJSONArray("data");
//                    String data = object.getString("data");
//                    JSONArray arrayData=new JSONArray(data);
                    switch (statusCode) {
                        case "200": {

                            for (int i = 0; i < arrayData.length(); i++) {
                                JSONObject object1 = arrayData.getJSONObject(i);

                                ModelAds modelAds = new ModelAds();
                                modelAds.setId(object1.getString("id"));
                                modelAds.setTitle(object1.getString("title"));
                                modelAds.setDescription(object1.getString("description"));
                                modelAds.setPrice(object1.getString("price"));
                                modelAds.setViews(object1.getString("views"));
                                modelAds.setRating(object1.getString("rating"));
                                modelAds.setImage(object1.getString("image"));
                                modelAds.setType(object1.getString("type"));

                                arrayList.add(modelAds);
                            }
                            if (arrayList.size() > 0) {
                                initAdapter(arrayList);
                            } else {
                                Toast.makeText(context, "الرجاء المحاوله لاحقا", Toast.LENGTH_SHORT).show();
                            }

                            break;
                        }

                        default: {
                            Toast.makeText(context, "حدث خطأ الرجاء المحاولة مرة اخرى", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "حدث خطأ الرجاء المحاولة مر اخرى", Toast.LENGTH_SHORT).show();
//                    showSnackBar("حدث خطأ الرجاء المحاولة مر اخرى");
                }
                progressLay.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                Toast.makeText(context, "حدث خطأ الرجاء المحاولة مر اخرى", Toast.LENGTH_SHORT).show();
//                showSnackBar("حدث خطأ الرجاء المحاولة مر اخرى");
                progressLay.setVisibility(View.GONE);
            }
        });
    }

    //snackBar msg
    private void showSnackBar(String msg) {
        Snackbar snackbar = Snackbar.make(container2, msg, Snackbar.LENGTH_LONG);
        View snackview = snackbar.getView();
        snackview.setBackgroundColor(Color.RED);
        TextView masseage;
        masseage = snackview.findViewById(R.id.snackbar_text);
        masseage.setTextSize(16);
        masseage.setTextColor(Color.WHITE);
        snackbar.show();
    }

    public void showSnackBarBtn(String msg) {
        final Snackbar snackbar = Snackbar.make(container2, msg, Snackbar.LENGTH_INDEFINITE);
        View snackview = snackbar.getView();
        snackview.setBackgroundColor(Color.RED);
        TextView masseage;
        masseage = snackview.findViewById(R.id.snackbar_text);
        masseage.setTextSize(16);
        masseage.setTextColor(Color.WHITE);
        snackbar.setAction("اعادة المحاولة", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call your action method here
                init();
                snackbar.dismiss();
            }
        }).setActionTextColor(getResources().getColor(R.color.colorPrimary));
        snackbar.show();
    }
    private void initAdapter(ArrayList<ModelAds> arrayList){
        recyclerView = view.findViewById(R.id.recycler);
//        recyclerView.setNestedScrollingEnabled(false);
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true);
        recyclerView.setLayoutManager(linearLayoutManager);
//        for (int i = 0; i < 10; i++) {
//            ModelAds modelAds= new ModelAds();
//            modelAds.setId(i+"");
//            arrayList.add(modelAds);
//        }
        adapterTabAds = new AdapterTabAds(getActivity(),arrayList);
        recyclerView.setAdapter(adapterTabAds);
//        recyclerView.smoothScrollToPosition(arrayList.size());
    }
}