package com.example.tadawl.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tadawl.Activity.AllAds;
import com.example.tadawl.Adapter.AdapterNewAds;
import com.example.tadawl.Model.ModelNewAds;
import com.example.tadawl.Utils.Api;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import com.example.tadawl.R;
import com.example.tadawl.Utils.CustomViewPager;
import com.example.tadawl.Adapter.ViewPagerAdapter;

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

    RecyclerView recycler,recycler2;
    AdapterNewAds adapterNewAds,adapterNewAds2;
    ArrayList <ModelNewAds> arrayList,arrayList2;
    TextView txtShowMore,txtShowMore2;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_main2, container, false);
        init();


        return view;
    }

    private void init() {
        container = view.findViewById(R.id.container);
        progressLayDark = view.findViewById(R.id.progressLayDark);
        progressLay = view.findViewById(R.id.progressLay);
        progressLay2 = view.findViewById(R.id.progressLay2);
        txtShowMore = view.findViewById(R.id.txtShowMore);
        txtShowMore2 = view.findViewById(R.id.txtShowMore2);
        recycler = view.findViewById(R.id.recycler);
        recycler2 = view.findViewById(R.id.recycler2);
        tableLayout = view.findViewById(R.id.tabLayout);
        customViewPager = view.findViewById(R.id.viewpager);

        txtShowMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AllAds.class);
                intent.putExtra("type","cars");
                startActivity(intent);
            }
        });
        txtShowMore2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AllAds.class);
                intent.putExtra("type","realestates");
                startActivity(intent);
            }
        });


        initTabLayout();
        getCars();
        getEstate();
//        initAdapterNewAds();

    }

    private void initAdapterCars(ArrayList<ModelNewAds> array) {
        adapterNewAds = new AdapterNewAds(getActivity(),array);
        recycler.setAdapter(adapterNewAds);

    }
    private void initAdapterEstate(ArrayList<ModelNewAds> array) {
        adapterNewAds2 = new AdapterNewAds(getActivity(),array);
        recycler2.setAdapter(adapterNewAds2);

    }

    private void initTabLayout() {
        FragmentManager fragmentManager=getChildFragmentManager();
        ViewPagerAdapter adapter = new ViewPagerAdapter(fragmentManager);

        FragmentTabLay fragmentTabLay1 = new FragmentTabLay("1");
        FragmentTabLay fragmentTabLay2 = new FragmentTabLay("2");
        FragmentTabLay fragmentTabLay3 = new FragmentTabLay("3");

//        adapter.addFragment(fragmentTabLay1,"لك");
        adapter.addFragment(fragmentTabLay2,"اعلانات متداولة");
        adapter.addFragment(fragmentTabLay3,"كل الاعلانات");
        customViewPager.setAdapter(adapter);
        tableLayout.setupWithViewPager(customViewPager);

    }

    LinearLayout progressLay,progressLay2,progressLayDark;
    private void getCars() {
        arrayList = new ArrayList<>();
        progressLay.setVisibility(View.VISIBLE);
        progressLayDark.setVisibility(View.VISIBLE);
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

        Api.RetrofitGetCarsOrEstate service = retrofit.create(Api.RetrofitGetCarsOrEstate.class);
        HashMap<String, String> hashMap=new HashMap<>();
        hashMap.put("type","cars");
        Call<String> call = service.putParam(hashMap);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                try {
                    JSONObject object = new JSONObject(response.body());
                    String statusCode = object.getString("status_code");
                    JSONObject jsonObject = object.getJSONObject("data");
                    switch (statusCode) {
                        case "200": {
                            JSONArray arrayData = jsonObject.getJSONArray("data");
                            for (int i = 0; i < arrayData.length(); i++) {
                                JSONObject item = arrayData.getJSONObject(i);
                                ModelNewAds modelNewAds = new ModelNewAds();
                                modelNewAds.setId(item.getString("id"));
                                modelNewAds.setTitle(item.getString("title"));
                                modelNewAds.setDescription(item.getString("description"));
                                modelNewAds.setPrice(item.getString("price"));
                                modelNewAds.setViews(item.getString("views"));
                                modelNewAds.setRating(item.getString("rating"));
                                modelNewAds.setImage(item.getString("image"));

                                arrayList.add(modelNewAds);
                            }

                            if (arrayList.size()>0){
                                initAdapterCars(arrayList);
                            }

                            break;
                        }

                        default: {
                            Toast.makeText(getActivity(), "حدث خطأ الرجاء المحاولة مرة اخرى", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }

                    progressLay.setVisibility(View.GONE);
                    progressLayDark.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
//                    Toast.makeText(getActivity(), "خطأ في التحويل", Toast.LENGTH_SHORT).show();
                    showSnackBarBtn("حدث خطأ الرجاء المحاولة مر اخرى");
                }
                progressLay.setVisibility(View.GONE);
                progressLayDark.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
//                Toast.makeText(getActivity(), "خطأ في تسجيل الدخول، ربما البيانات غير صحيحة", Toast.LENGTH_SHORT).show();
                showSnackBarBtn("حدث خطأ الرجاء المحاولة مر اخرى");
                progressLay.setVisibility(View.GONE);
                progressLayDark.setVisibility(View.GONE);
            }
        });
    }

    private void getEstate() {
        arrayList2 = new ArrayList<>();
        progressLay2.setVisibility(View.VISIBLE);
        progressLayDark.setVisibility(View.VISIBLE);
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

        Api.RetrofitGetCarsOrEstate service = retrofit.create(Api.RetrofitGetCarsOrEstate.class);
        HashMap<String, String> hashMap=new HashMap<>();
        hashMap.put("type","realestates");
        Call<String> call = service.putParam(hashMap);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                try {
                    JSONObject object = new JSONObject(response.body());
                    String statusCode = object.getString("status_code");
                    JSONObject jsonObject = object.getJSONObject("data");
                    switch (statusCode) {
                        case "200": {
                            JSONArray arrayData = jsonObject.getJSONArray("data");
                            for (int i = 0; i < arrayData.length(); i++) {
                                JSONObject item = arrayData.getJSONObject(i);
                                ModelNewAds modelNewAds = new ModelNewAds();
                                modelNewAds.setId(item.getString("id"));
                                modelNewAds.setTitle(item.getString("title"));
                                modelNewAds.setDescription(item.getString("description"));
                                modelNewAds.setPrice(item.getString("price"));
                                modelNewAds.setViews(item.getString("views"));
                                modelNewAds.setRating(item.getString("rating"));
                                modelNewAds.setImage(item.getString("image"));

                                arrayList2.add(modelNewAds);
                            }

                            if (arrayList2.size()>0){
                                initAdapterEstate(arrayList2);
                            }

                            break;
                        }

                        default: {
                            Toast.makeText(getActivity(), "حدث خطأ الرجاء المحاولة مرة اخرى", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }

                    progressLay2.setVisibility(View.GONE);
                    progressLayDark.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
//                    Toast.makeText(getActivity(), "خطأ في التحويل", Toast.LENGTH_SHORT).show();
                    showSnackBarBtn("حدث خطأ الرجاء المحاولة مر اخرى");
                }
                progressLay2.setVisibility(View.GONE);
                progressLayDark.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
//                Toast.makeText(getActivity(), "خطأ في تسجيل الدخول، ربما البيانات غير صحيحة", Toast.LENGTH_SHORT).show();
                showSnackBarBtn("حدث خطأ الرجاء المحاولة مر اخرى");
                progressLay2.setVisibility(View.GONE);
                progressLayDark.setVisibility(View.GONE);
            }
        });
    }
    CoordinatorLayout container;
    //snackBar msg
    private void showSnackBar(String msg) {
        Snackbar snackbar = Snackbar.make(container, msg, Snackbar.LENGTH_LONG);
        View snackview = snackbar.getView();
        snackview.setBackgroundColor(Color.RED);
        TextView masseage;
        masseage = snackview.findViewById(R.id.snackbar_text);
        masseage.setTextSize(16);
        masseage.setTextColor(Color.WHITE);
        snackbar.show();
    }

    public void showSnackBarBtn(String msg) {
        final Snackbar snackbar = Snackbar.make(container, msg, Snackbar.LENGTH_INDEFINITE);
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


}