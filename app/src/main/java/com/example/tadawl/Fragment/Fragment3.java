package com.example.tadawl.Fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tadawl.Adapter.AdapterNewAds;
import com.example.tadawl.Model.ModelNewAds;
import com.example.tadawl.R;
import com.example.tadawl.Utils.Api;
import com.example.tadawl.Utils.SharedPrefManager;
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

import static android.content.ContentValues.TAG;


public class Fragment3 extends Fragment {

    Context context;
    @Override
    public void onAttach(@NonNull Context context) {
        this.context = context;
        super.onAttach(context);
    }


    public Fragment3() {
        // Required empty public constructor
    }

    View view;
    RecyclerView recycler;
    AdapterNewAds adapterNewAds;
    ArrayList<ModelNewAds> arrayList;
    GridLayoutManager gridLayoutManager;
    LinearLayout layBack, progressLay, progressLayPage,layNoData;
    String type = "";
    TextView title;
    String currentPage = "", lastPage = "", perPage = "";
    boolean isLoading = false;
    NestedScrollView nestedScroll;

    boolean paging = false;
    ConstraintLayout container;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_3, container, false);
        arrayList = new ArrayList<>();
        init();
        getMyAds("");
        //get last view on nestedScrollView
        nestedScroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (scrollY > oldScrollY) {
                    Log.i(TAG, "Scroll DOWN");
                }
                if (scrollY < oldScrollY) {
                    Log.i(TAG, "Scroll UP");
                }

                if (scrollY == 0) {
                    Log.i(TAG, "TOP SCROLL");
                }

                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    Log.i(TAG, "BOTTOM SCROLL");

                    if (Double.parseDouble(lastPage) > Double.parseDouble(currentPage) && !isLoading )
                        paging=true;
                    getMyAds(Integer.parseInt(currentPage) + 1 + "");
                }

            }
        });
        return view;
    }

    private void init() {
        layNoData = view.findViewById(R.id.layNoData);
        progressLayPage = view.findViewById(R.id.progressLayPage);
        title = view.findViewById(R.id.title);
        container = view.findViewById(R.id.container);
        nestedScroll = view.findViewById(R.id.nestedScroll);
        progressLay = view.findViewById(R.id.progressLay);
        gridLayoutManager = new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false);
        recycler = view.findViewById(R.id.recycler);
        recycler.setLayoutManager(gridLayoutManager);
    }

    private void getMyAds(String page) {
        isLoading = true;
        if (paging) {
            progressLayPage.setVisibility(View.VISIBLE);
        } else {
            progressLay.setVisibility(View.VISIBLE);
        }
        layNoData.setVisibility(View.GONE);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        okhttp3.Request.Builder ongoing = chain.request().newBuilder();
                        ongoing.addHeader("Content-Type", "application/json;");
                        ongoing.addHeader("Content-Type", "application/x-www-form-urlencoded");
                        String token = SharedPrefManager.getInstance(context).GetToken();
                        ongoing.addHeader("Authorization", token);
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

        Api.RetrofitMyAds service = retrofit.create(Api.RetrofitMyAds.class);
        HashMap<String, String> hashMap=new HashMap<>();
        hashMap.put("page",page);
        Call<String> call = service.putParam(hashMap);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                try {
                    JSONObject object = new JSONObject(response.body());
                    String statusCode = object.getString("status_code");
                    JSONObject jsonObject = object.getJSONObject("data");
                    //meta
//                    JSONObject metaObject = jsonObject.getJSONObject("meta");
                    currentPage = jsonObject.getString("current_page");
                    lastPage = jsonObject.getString("last_page");
                    perPage = jsonObject.getString("per_page");

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
                                modelNewAds.setType(item.getString("type"));

                                arrayList.add(modelNewAds);
                            }

                            if (arrayList.size() > 0) {
                                initAdapter(arrayList);
                                layNoData.setVisibility(View.GONE);
                            }else {
                                layNoData.setVisibility(View.VISIBLE);
                            }

                            break;
                        }

                        default: {
                            Toast.makeText(context, "حدث خطأ الرجاء المحاولة مرة اخرى", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }

                    progressLay.setVisibility(View.GONE);
                    progressLayPage.setVisibility(View.GONE);
                    isLoading = false;
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
//                    showSnackBarBtn("حدث خطأ الرجاء المحاولة مر اخرى");
                    isLoading = false;
                }
                progressLay.setVisibility(View.GONE);
                progressLayPage.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                Toast.makeText(context, "حدث خطأ الرجاء المحاولة مر اخرى", Toast.LENGTH_SHORT).show();
//                showSnackBarBtn("حدث خطأ الرجاء المحاولة مر اخرى");
                isLoading = false;
                progressLay.setVisibility(View.GONE);
                progressLayPage.setVisibility(View.GONE);
            }
        });
    }

    private void initAdapter(ArrayList<ModelNewAds> array) {
        adapterNewAds = new AdapterNewAds(getActivity(), array);
        recycler.setAdapter(adapterNewAds);
        recycler.smoothScrollToPosition(arrayList.size() - Integer.parseInt(perPage));

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
                getMyAds(currentPage);
                snackbar.dismiss();
            }
        }).setActionTextColor(getResources().getColor(R.color.colorPrimary));
        snackbar.show();
    }


}