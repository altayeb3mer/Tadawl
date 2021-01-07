package com.example.tadawl.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tadawl.Activity.AddSuccess;
import com.example.tadawl.Model.ModelCity;
import com.example.tadawl.Model.ModelEstateCat;
import com.example.tadawl.Model.ModelState;
import com.example.tadawl.R;
import com.example.tadawl.Utils.Api;
import com.example.tadawl.Utils.SharedPrefManager;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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

import static android.app.Activity.RESULT_OK;


public class FragmentAddCar extends Fragment {


    public FragmentAddCar() {
        // Required empty public constructor
    }

    public static final int PICK_IMAGE = 1;
    View view;
    AppCompatButton btn;
    LinearLayout progressLay;
    ArrayList<ModelEstateCat> catArrayList;
    CardView cardViewImg1, cardViewImg2, cardViewImg3, cardViewImg4, cardViewImg5;
    ImageView imageView1, imageView2, imageView3, imageView4, imageView5;
    EditText editTextTitle, editTextPrice, editTextNoRoom, editTextArea, editTextNeighborhood, editTextDescription;
    String s_title = "", s_price = "", s_color = "", s_model = "", s_car_year = "", s_description = "";
    ConstraintLayout container;
    Spinner spinnerType, spinnerCategory, spinnerState, spinnerCity,SpinnerIsNew, SpinnerTransmission;
    ArrayList<String> arrayCatName;
    String[] arrayType;
    String s_type = "", s_category = "";
    boolean hasImage = false;
    //state spinner
    ArrayList<ModelState> arrayListState;
    ArrayList<ModelCity> arrayListCity;
    String s_state_id = "", s_city_id = "";
    String imgNo = "";
    HashMap<String, String> hashMapParam;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_car, container, false);
        hashMapParam = new HashMap<>();
        init();
        return view;
    }


    private void init(){
        container = view.findViewById(R.id.container);

        editTextTitle = view.findViewById(R.id.title);
        editTextPrice = view.findViewById(R.id.price);
        editTextNoRoom = view.findViewById(R.id.noOfRoom);
        editTextArea = view.findViewById(R.id.area);
        editTextNeighborhood = view.findViewById(R.id.neighborhood);
        editTextDescription = view.findViewById(R.id.description);

        imageView1 = view.findViewById(R.id.img1);
        imageView2 = view.findViewById(R.id.img2);
        imageView3 = view.findViewById(R.id.img3);
        imageView4 = view.findViewById(R.id.img4);
        imageView5 = view.findViewById(R.id.img5);

        cardViewImg1 = view.findViewById(R.id.cardImg1);
        cardViewImg2 = view.findViewById(R.id.cardImg2);
        cardViewImg3 = view.findViewById(R.id.cardImg3);
        cardViewImg4 = view.findViewById(R.id.cardImg4);
        cardViewImg5 = view.findViewById(R.id.cardImg5);


        cardViewImg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgNo = "1";
                pickImage();
            }
        });
        cardViewImg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgNo = "2";
                pickImage();
            }
        });
        cardViewImg3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgNo = "3";
                pickImage();
            }
        });
        cardViewImg4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgNo = "4";
                pickImage();
            }
        });
        cardViewImg5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgNo = "5";
                pickImage();
            }
        });


        spinnerState = view.findViewById(R.id.spinnerState);
        spinnerCity = view.findViewById(R.id.spinnerCity);
        SpinnerIsNew = view.findViewById(R.id.spinnerIsNew);
        SpinnerTransmission = view.findViewById(R.id.spinnerTransmission);

        progressLay = view.findViewById(R.id.progressLay);
        btn = view.findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(getActivity(), AddSuccess.class));
                preAdd();
            }
        });
        initPage();

    }
    private void initPage() {
        initSpinnerType();
        getCompanies();
        initTransmission();
        initSpinnerIsNew();
        getState();

    }
    private void preAdd() {
        s_title = editTextTitle.getText().toString().trim();
        s_price = editTextPrice.getText().toString().trim();
        s_color = editTextNoRoom.getText().toString().trim();
        s_model = editTextArea.getText().toString().trim();
        s_car_year = editTextNeighborhood.getText().toString().trim();
        s_description = editTextDescription.getText().toString().trim();

        if (!s_title.equals("") && !s_type.equals("") && !s_category.equals("")
                && !s_price.equals("") && !s_color.equals("")
                && !is_new.equals("") && !s_transmission.equals("") && !s_car_year.equals("")
                && !s_model.equals("") && !s_city_id.equals("")
                && hasImage) {
            sendPost();
        } else {
            showSnakBar("الرجاء ملء الحقول المطلوبة(على الاقل صورة)");
        }

    }

    private void sendPost() {
        arrayListState = new ArrayList<>();
        progressLay.setVisibility(View.VISIBLE);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        okhttp3.Request.Builder ongoing = chain.request().newBuilder();
                        ongoing.addHeader("Content-Type", "application/json;");
                        ongoing.addHeader("Content-Type", "application/x-www-form-urlencoded");
                        String token = SharedPrefManager.getInstance(getActivity()).GetToken();
                        ongoing.addHeader("Authorization", token);
                        return chain.proceed(ongoing.build());
                    }
                })
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.ROOT_URL)
                .client(httpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api.RetrofitAddCar service = retrofit.create(Api.RetrofitAddCar.class);

        hashMapParam.put("transmission", s_transmission);
        hashMapParam.put("is_new", is_new);
        hashMapParam.put("title", s_title);
        hashMapParam.put("ad_type", s_type);
        hashMapParam.put("price", s_price);
        hashMapParam.put("description", s_description);
        hashMapParam.put("company_id", s_category);
        hashMapParam.put("color", s_color);
        hashMapParam.put("year", s_car_year);
        hashMapParam.put("model", s_model);
        hashMapParam.put("city_id", s_city_id);

        Call<String> call = service.putParam(hashMapParam);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                try {
                    JSONObject object = new JSONObject(response.body());
                    String statusCode = object.getString("status_code");
                    JSONObject arrayData = object.getJSONObject("data");
//                    String data = object.getString("data");
//                    JSONArray arrayData=new JSONArray(data);
                    switch (statusCode) {
                        case "200": {

                            startActivity(new Intent(getActivity(), AddSuccess.class));
                            getActivity().finish();
                            break;
                        }

                        default: {
                            Toast.makeText(getActivity(), "حدث خطأ الرجاء المحاولة مرة اخرى", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "خطأ في التحويل", Toast.LENGTH_SHORT).show();
                }
                progressLay.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                Toast.makeText(getActivity(), "خطأ في اضافة الاعلان حاول مجددا", Toast.LENGTH_SHORT).show();
                progressLay.setVisibility(View.GONE);
            }
        });
    }

    //snackBar msg
    private void showSnakBar(String msg) {
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
                initPage();
                snackbar.dismiss();
            }
        }).setActionTextColor(getResources().getColor(R.color.colorPrimary));
        snackbar.show();
    }

    private void getCompanies() {
        catArrayList = new ArrayList<>();
        arrayCatName = new ArrayList<>();
        arrayCatName.add("اختر الشركة المصنعة");
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

        Api.RetrofitCarCompanies service = retrofit.create(Api.RetrofitCarCompanies.class);


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

                                ModelEstateCat modelEstateCat = new ModelEstateCat();
                                modelEstateCat.setName(object1.getString("name"));
                                modelEstateCat.setId(object1.getString("id"));

                                arrayCatName.add(object1.getString("name"));
                                catArrayList.add(modelEstateCat);
                            }
                            if (arrayCatName.size() > 0) {
                                initSpinnerCategory();
                            } else {
                                Toast.makeText(getActivity(), "الرجاء المحاوله لاحقا", Toast.LENGTH_SHORT).show();
                            }

                            break;
                        }

                        default: {
                            Toast.makeText(getActivity(), "حدث خطأ الرجاء المحاولة مرة اخرى", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
//                    Toast.makeText(getActivity(), "خطأ في التحويل", Toast.LENGTH_SHORT).show();
                    showSnakBar("حدث خطأ الرجاء المحاولة مر اخرى");
                }
                progressLay.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
//                Toast.makeText(getActivity(), "حدث خطأ الرجاء المحاولة مر اخرى", Toast.LENGTH_SHORT).show();
                showSnakBar("حدث خطأ الرجاء المحاولة مر اخرى");
                progressLay.setVisibility(View.GONE);
            }
        });
    }

    private void initSpinnerCategory() {
        spinnerCategory = view.findViewById(R.id.spinnerCategory);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, arrayCatName) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = null;
                v = super.getDropDownView(position, null, parent);
                // If this is the selected item position

                return v;
            }
        };
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter1);
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    s_category = "";
                } else {
                    s_category = catArrayList.get(position - 1).getId();
                    Toast.makeText(getActivity(), s_category, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void initSpinnerType() {
        spinnerType = view.findViewById(R.id.spinnerType);
        arrayType = new String[]{"اختر النوع", "ايجار", "بيع"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, arrayType) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = null;
                v = super.getDropDownView(position, null, parent);
                // If this is the selected item position

                return v;
            }
        };
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter1);
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    s_type = "";
                } else {
                    if (position == 1) {
                        s_type = "rent";
                    } else {
                        s_type = "buy";
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }

    private void initTransmission() {
        ArrayList<String> stateName = new ArrayList<>();
        stateName.add("اختر اختر نوع القيادة");
        stateName.add("اوتوماتيك");
        stateName.add("مانويل");
//        for (int i = 0; i < arrayList.size(); i++) {
//            stateName.add(arrayList.get(i).getName());
//        }
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, stateName) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = null;
                v = super.getDropDownView(position, null, parent);
                // If this is the selected item position

                return v;
            }
        };
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerTransmission.setAdapter(adapter1);
        SpinnerTransmission.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    s_transmission = "";
                } else {
                    if (position==1){
                        s_transmission = "automatic";
                    }else{
                        s_transmission = "manual";
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    String s_transmission="",is_new="";
    private void initSpinnerIsNew() {
        ArrayList<String> cityName = new ArrayList<>();
        cityName.add("اضغط للاختيار..");
        cityName.add("نعم");
        cityName.add("لا");

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, cityName) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = null;
                v = super.getDropDownView(position, null, parent);
                // If this is the selected item position

                return v;
            }
        };
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerIsNew.setAdapter(adapter1);
        SpinnerIsNew.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    is_new = "";
                } else {
                    if (position==1){
                        is_new = "1";
                    }else {
                        is_new = "0";
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    private void initSpinnerState(final ArrayList<ModelState> arrayList) {
        ArrayList<String> stateName = new ArrayList<>();
        stateName.add("اختر الولاية");
        for (int i = 0; i < arrayList.size(); i++) {
            stateName.add(arrayList.get(i).getName());
        }
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, stateName) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = null;
                v = super.getDropDownView(position, null, parent);
                // If this is the selected item position

                return v;
            }
        };
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerState.setAdapter(adapter1);
        spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    s_state_id = "";
                } else {
                    s_state_id = arrayList.get(position - 1).getId();
                    getCity(s_state_id);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void initSpinnerCity(final ArrayList<ModelCity> arrayList) {
        ArrayList<String> cityName = new ArrayList<>();
        cityName.add("اختر المدينة");
        for (int i = 0; i < arrayList.size(); i++) {
            cityName.add(arrayList.get(i).getName());
        }
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, cityName) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = null;
                v = super.getDropDownView(position, null, parent);
                // If this is the selected item position

                return v;
            }
        };
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCity.setAdapter(adapter1);
        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    s_city_id = "";
                } else {
                    s_city_id = arrayList.get(position - 1).getId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void getState() {
        arrayListState = new ArrayList<>();
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

        Api.RetrofitState service = retrofit.create(Api.RetrofitState.class);


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

                                ModelState modelState = new ModelState();
                                modelState.setName(object1.getString("name"));
                                modelState.setId(object1.getString("id"));

                                arrayListState.add(modelState);
                            }
                            if (arrayListState.size() > 0) {
                                initSpinnerState(arrayListState);
                            } else {
                                Toast.makeText(getActivity(), "الرجاء المحاوله لاحقا", Toast.LENGTH_SHORT).show();
                            }

                            break;
                        }

                        default: {
                            Toast.makeText(getActivity(), "حدث خطأ الرجاء المحاولة مرة اخرى", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
//                    Toast.makeText(getActivity(), "خطأ في التحويل", Toast.LENGTH_SHORT).show();
                    showSnackBarBtn("حدث خطأ الرجاء المحاولة مر اخرى");
                }
                progressLay.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
//                Toast.makeText(getActivity(), "خطأ في تسجيل الدخول، ربما البيانات غير صحيحة", Toast.LENGTH_SHORT).show();
                showSnackBarBtn("حدث خطأ الرجاء المحاولة مر اخرى");
                progressLay.setVisibility(View.GONE);
            }
        });
    }

    private void getCity(String state_id) {
        arrayListCity = new ArrayList<>();
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

        Api.RetrofitCity service = retrofit.create(Api.RetrofitCity.class);

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("state_id", state_id);

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

                                ModelCity modelCity = new ModelCity();
                                modelCity.setName(object1.getString("name"));
                                modelCity.setId(object1.getString("id"));

                                arrayListCity.add(modelCity);
                            }
                            if (arrayListCity.size() > 0) {
                                initSpinnerCity(arrayListCity);
                            } else {
                                Toast.makeText(getActivity(), "الرجاء المحاوله لاحقا", Toast.LENGTH_SHORT).show();
                            }

                            break;
                        }

                        default: {
                            Toast.makeText(getActivity(), "حدث خطأ الرجاء المحاولة مرة اخرى", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
//                    Toast.makeText(getActivity(), "خطأ في التحويل", Toast.LENGTH_SHORT).show();
                    showSnackBarBtn("حدث خطأ الرجاء المحاولة مر اخرى");
                }
                progressLay.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
//                Toast.makeText(getActivity(), "خطأ في تسجيل الدخول، ربما البيانات غير صحيحة", Toast.LENGTH_SHORT).show();
                showSnackBarBtn("حدث خطأ الرجاء المحاولة مر اخرى");
                progressLay.setVisibility(View.GONE);
            }
        });
    }




    //image picker
    private void pickImage() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            try {
                hasImage = true;
                final Uri imageUri = data.getData();
                final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                selectedImage = getResizedBitmap(selectedImage,400);

                switch (imgNo) {
                    case "1": {
//                        Glide.with(getActivity()).load(selectedImage).into(imageView1);
                        imageView1.setImageBitmap(selectedImage);
                        imageView1.setVisibility(View.VISIBLE);
                        hashMapParam.put("image1", getStringFromImg(selectedImage));
                        break;
                    }
                    case "2": {
//                        Glide.with(getActivity()).load(selectedImage).into(imageView2);
                        imageView2.setImageBitmap(selectedImage);
                        imageView2.setVisibility(View.VISIBLE);
                        hashMapParam.put("image2", getStringFromImg(selectedImage));
                        break;
                    }
                    case "3": {
//                        Glide.with(getActivity()).load(selectedImage).into(imageView3);
                        imageView3.setImageBitmap(selectedImage);
                        imageView3.setVisibility(View.VISIBLE);
                        hashMapParam.put("image3", getStringFromImg(selectedImage));
                        break;
                    }
                    case "4": {
//                        Glide.with(getActivity()).load(selectedImage).into(imageView4);
                        imageView4.setImageBitmap(selectedImage);
                        imageView4.setVisibility(View.VISIBLE);
                        hashMapParam.put("image4", getStringFromImg(selectedImage));
                        break;
                    }
                    case "5": {
//                        Glide.with(getActivity()).load(selectedImage).into(imageView5);
                        imageView5.setImageBitmap(selectedImage);
                        imageView5.setVisibility(View.VISIBLE);
                        hashMapParam.put("image5", getStringFromImg(selectedImage));
                        break;
                    }

                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(getActivity(), "لم تقم باختيار صورة", Toast.LENGTH_LONG).show();
//            hasImage = false;
        }
    }

    private String getStringFromImg(Bitmap bitmap) {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteStream);
        byte[] byteArray = byteStream.toByteArray();
        String baseString = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return baseString;
    }



    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
}