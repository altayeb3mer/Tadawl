package com.example.tadawl.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
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

public class SearchActivity extends AppCompatActivity {
    AdapterNewAds adapterNewAds;
    ArrayList<ModelNewAds> arrayList;
    GridLayoutManager gridLayoutManager;

    EditText editTextSearch;
    RecyclerView recycler;
    LinearLayout progressLay,noItemLay;
    ImageView imageView_back_icon,ic_clear;
    String currentPage = "", lastPage = "", perPage = "";
    boolean isLoading = false;
    NestedScrollView nestedScroll;
    ConstraintLayout container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        arrayList = new ArrayList<>();
        init();
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
                    getSearch(Integer.parseInt(currentPage) + 1 + "");
                }

            }
        });
    }

    String query="";

    private void init() {
        container = findViewById(R.id.container);
        nestedScroll = findViewById(R.id.nestedScroll);
        imageView_back_icon = findViewById(R.id.back_icon);
        imageView_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        noItemLay = findViewById(R.id.noItemLay);
        editTextSearch = findViewById(R.id.edtSearch);
        editTextSearch.addTextChangedListener(editTextWatcher);
        gridLayoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(gridLayoutManager);
        progressLay = findViewById(R.id.progressLay);
        ic_clear = findViewById(R.id.ic_clear);
        ic_clear.setVisibility(View.INVISIBLE);

        ic_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextSearch.setText("");
                showKeyboard(SearchActivity.this);
                query = "";
            }
        });


        editTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    query = editTextSearch.getText().toString().trim();
                    if (!query.equals("")) {
                        hideKeyboard(SearchActivity.this);
                        currentPage = "";
                        if (arrayList.size()>0){
                            arrayList.clear();
                            adapterNewAds.notifyDataSetChanged();
                        }
                        getSearch(currentPage);
                    } else {
                        Toast.makeText(SearchActivity.this, "الرجاء كتابة شي للبحث", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
                return false;
            }
        });





    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void showKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.showSoftInput(editTextSearch, 0);
    }

    TextWatcher editTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //none
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //none
        }

        @Override
        public void afterTextChanged(Editable s) {

            String check = s.toString();

            if (check.length()>0){
                ic_clear.setVisibility(View.VISIBLE);
            }else{
                ic_clear.setVisibility(View.INVISIBLE);
            }

        }

    };

    private void initAdapter(ArrayList<ModelNewAds> array) {
        adapterNewAds = new AdapterNewAds(this, array);
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
//                getFavorite(currentPage);
                snackbar.dismiss();
            }
        }).setActionTextColor(getResources().getColor(R.color.colorPrimary));
        snackbar.show();
    }

    private void getSearch(String page) {
        isLoading = true;
            progressLay.setVisibility(View.VISIBLE);

            noItemLay.setVisibility(View.GONE);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        okhttp3.Request.Builder ongoing = chain.request().newBuilder();
                        ongoing.addHeader("Content-Type", "application/json;");
                        ongoing.addHeader("Content-Type", "application/x-www-form-urlencoded");
                        String token = SharedPrefManager.getInstance(getApplicationContext()).GetToken();
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

        Api.RetrofitSearch service = retrofit.create(Api.RetrofitSearch.class);
        HashMap<String, String> hashMap=new HashMap<>();
        hashMap.put("page",page);
        hashMap.put("q",query);
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
                                noItemLay.setVisibility(View.GONE);
                            }else {
                                noItemLay.setVisibility(View.VISIBLE);
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


}