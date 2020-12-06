package com.example.tadawl.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tadawl.Adapter.AdapterNewAds;
import com.example.tadawl.Model.ModelNewAds;
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

import static android.content.ContentValues.TAG;

public class AllAds extends AppCompatActivity {

    Spinner spinnerCategory;
    String[] array;
    RecyclerView recycler;
    AdapterNewAds adapterNewAds;
    ArrayList<ModelNewAds> arrayList;
    GridLayoutManager gridLayoutManager;
    LinearLayout layBack,progressLay;
    String type = "";
    TextView title;
    String currentPage="",lastPage="",perPage="";
    boolean isLoading = false;
    NestedScrollView nestedScroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ads);
        arrayList = new ArrayList<>();
        Bundle args = getIntent().getExtras();
        if (args!=null){
            type = args.getString("type");
        }


        init();
        initHeader();
        initSpinner();
//        getCarsOrEstate(currentPage);


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
                        getCarsOrEstate(Integer.parseInt(currentPage) + 1 + "");
                }

            }
        });
    }

    private void initHeader() {
        switch (type){
            case "cars":{
                title.setText("سيارات");
                break;
            }
            case "realestates":{
                title.setText("عقارات");
                break;
            }
            default:{
                title.setText("كل الاعلانات");
            }
        }
    }

    private void init() {
        title = findViewById(R.id.title);
        container = findViewById(R.id.container);
        nestedScroll = findViewById(R.id.nestedScroll);
        progressLay = findViewById(R.id.progressLay);
        layBack = findViewById(R.id.laySearch);
        layBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        gridLayoutManager = new GridLayoutManager(this,3,GridLayoutManager.VERTICAL,false);
        recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(gridLayoutManager);
    }

    private void initAdapter(ArrayList<ModelNewAds> array) {
        adapterNewAds = new AdapterNewAds(this,array);
        recycler.setAdapter(adapterNewAds);
        recycler.smoothScrollToPosition(arrayList.size()-Integer.parseInt(perPage));

    }

    private void getCarsOrEstate(String page) {
        isLoading = true;
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

        Api.RetrofitGetCarsOrEstate service = retrofit.create(Api.RetrofitGetCarsOrEstate.class);
        HashMap<String, String> hashMap=new HashMap<>();
        hashMap.put("type",type);
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

                                arrayList.add(modelNewAds);
                            }

                            if (arrayList.size()>0){
                               initAdapter(arrayList);
                            }

                            break;
                        }

                        default: {
                            Toast.makeText(getApplicationContext(), "حدث خطأ الرجاء المحاولة مرة اخرى", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }

                    progressLay.setVisibility(View.GONE);
                    isLoading = false;
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
//                    showSnackBarBtn("حدث خطأ الرجاء المحاولة مر اخرى");
                    isLoading = false;
                }
                progressLay.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
//                Toast.makeText(getActivity(), "خطأ في تسجيل الدخول، ربما البيانات غير صحيحة", Toast.LENGTH_SHORT).show();
                showSnackBarBtn("حدث خطأ الرجاء المحاولة مر اخرى");
                isLoading = false;
                progressLay.setVisibility(View.GONE);
            }
        });
    }

    private void initSpinner(){

        //month array
        spinnerCategory = findViewById(R.id.spinner);
        array = new String[]{"عرض الكل","سيارات", "عقارات"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, R.layout.spinner_item_white, array) {
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
                    type = "all";
                } else if (position==1){
                    type = "cars";
                }else {
                    type = "realestates";
                }
                initHeader();
                arrayList = new ArrayList();
                getCarsOrEstate("");

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    ConstraintLayout container;

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
                getCarsOrEstate(currentPage);
                snackbar.dismiss();
            }
        }).setActionTextColor(getResources().getColor(R.color.colorPrimary));
        snackbar.show();
    }
}