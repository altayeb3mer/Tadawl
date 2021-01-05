package com.example.tadawl.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import com.example.tadawl.Adapter.SlideShow_adapter;
import com.example.tadawl.Model.ModelSlideImg;
import com.example.tadawl.R;
import com.example.tadawl.Utils.Api;
import com.example.tadawl.Utils.SharedPrefManager;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import me.relex.circleindicator.CircleIndicator;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class PostDetails extends AppCompatActivity {

    ViewPager viewPager;
    SlideShow_adapter slideShowAdapter;
    ArrayList<ModelSlideImg> arrayListImage;
    CircleIndicator circleIndicator;
    Toolbar toolbar;

    LinearLayout layoutMenu, progressLay, callLay, layBack;
    PopupMenu popup;
    String id = "", type = "";
    ConstraintLayout container;
    TextView price, title, views, rating, description;
    String phone = "";

    TextView txtState,txtCity,txtType,txtCat,txtNoRoom,txtNeighborhood,txtArea,txtDate;
    TextView txtState2,txtCity2,txtType2,txtCompany,txtTransmission,txtIs_new,txtModel,txtYear,txtColor,txtDate2;
    TableLayout tableCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.hideOverflowMenu();
        init();
        initPopupMenu();

        Bundle args = getIntent().getExtras();
        if (args != null) {
            id = args.getString("id");
            type = args.getString("type");
            switch (type) {
                case "car": {
                    getCarsDetails();
                    break;
                }
                case "realestate": {
                    getEstateDetails();
                    break;
                }
            }
        }

    }

    private void init() {
        txtState = findViewById(R.id.txtState);
        txtCity = findViewById(R.id.txtCity);
        txtType = findViewById(R.id.txtType);
        txtCat = findViewById(R.id.txtCat);
        txtNoRoom = findViewById(R.id.txtNoRoom);
        txtNeighborhood = findViewById(R.id.txtNeighborhood);
        txtArea = findViewById(R.id.txtArea);
        txtDate = findViewById(R.id.txtDate);
        tableEstate = findViewById(R.id.tableEstate);

        txtState2 = findViewById(R.id.txtState2);
        txtCity2 = findViewById(R.id.txtCity2);
        txtType2 = findViewById(R.id.txtType2);
        txtCompany = findViewById(R.id.txtCompany);
        txtTransmission = findViewById(R.id.txtTransmission);
        txtIs_new = findViewById(R.id.txtIs_new);
        txtModel = findViewById(R.id.txtModel);
        txtYear = findViewById(R.id.txtYear);
        txtColor = findViewById(R.id.txtColor);
        txtDate2 = findViewById(R.id.txtDate2);
        tableCar = findViewById(R.id.tableCar);

        layBack = findViewById(R.id.layBack);
        layBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        price = findViewById(R.id.price);
        title = findViewById(R.id.title);
        rating = findViewById(R.id.rating);
        rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogRate();
            }
        });
        views = findViewById(R.id.views);
        description = findViewById(R.id.description);
        callLay = findViewById(R.id.callBtn);
        callLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CALL(phone);
            }
        });

        progressLay = findViewById(R.id.progressLay);
        container = findViewById(R.id.container);
    }

    private void initPopupMenu() {
        layoutMenu = findViewById(R.id.layMenu);
        popup = new PopupMenu(PostDetails.this, layoutMenu);
        popup.getMenuInflater()
                .inflate(R.menu.option_menu_post_details, popup.getMenu());

        layoutMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                PostDetails.this.openOptionsMenu();
                //popup menu


//                popup.getMenu().getItem(R.id.item1).setTitle("");
                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.item1: {
                                addToFavorite();
                                break;
                            }
                            case R.id.item2: {
                                openDialog();
//                                Toast.makeText(PostDetails.this, "item2", Toast.LENGTH_SHORT).show();
                                break;
                            }

                        }
                        return true;
                    }
                });
                popup.show();

            }
        });
    }

    private void initSlider(ArrayList<ModelSlideImg> arrayImages) {
        viewPager = findViewById(R.id.viewpager);
        slideShowAdapter = new SlideShow_adapter(getApplicationContext(), arrayImages);
        circleIndicator = findViewById(R.id.indicator);
        viewPager.setAdapter(slideShowAdapter);
        circleIndicator.setViewPager(viewPager);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.option_menu_post_details, menu);//Menu Resource, Menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1: {
                Toast.makeText(this, "item1", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.item2: {
                Toast.makeText(this, "item2", Toast.LENGTH_SHORT).show();
                break;
            }

        }
        return true;
    }

    private void openDialog() {
        final Dialog dialog = new Dialog(PostDetails.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog);
        final EditText editText = dialog.findViewById(R.id.reason);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        LinearLayout layBack = dialog.findViewById(R.id.layBack);
        layBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        AppCompatButton send = dialog.findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String reason = editText.getText().toString().trim();
                if (!reason.equals("")){
                    reportAds(reason);
                    dialog.dismiss();
                }else{
                    Toast.makeText(PostDetails.this, "الرجاء كتابة سبب الابلاغ", Toast.LENGTH_SHORT).show();
                }


            }
        });
        dialog.show();
    }

    private void dialogRate() {
        final Dialog dialog = new Dialog(PostDetails.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_rate);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        star1 = dialog.findViewById(R.id.star1);
        star2 = dialog.findViewById(R.id.star2);
        star3 = dialog.findViewById(R.id.star3);
        star4 = dialog.findViewById(R.id.star4);
        star5 = dialog.findViewById(R.id.star5);

        LinearLayout layBack = dialog.findViewById(R.id.layBack);
        layBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        star1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRateView(1);
            }
        });
        star2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRateView(2);
            }
        });
        star3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRateView(3);
            }
        });
        star4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRateView(4);
            }
        });
        star5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRateView(5);
            }
        });

        AppCompatButton send = dialog.findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!rate.equals("")){
                    rating(rate);
                    dialog.dismiss();
                }else{
                    Toast.makeText(PostDetails.this, "الرجاء اختيار التقييم من 1 الى 5 نجوم", Toast.LENGTH_SHORT).show();
                }


            }
        });
        dialog.show();
    }

    private void setRateView(int i) {
        rate = String.valueOf(i);
        switch (i){
            case 1:{
                star1.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_orange));
                star2.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_gray));
                star3.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_gray));
                star4.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_gray));
                star5.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_gray));
                break;
            }
            case 2:{
                star1.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_orange));
                star2.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_orange));
                star3.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_gray));
                star4.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_gray));
                star5.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_gray));
                break;
            }
            case 3:{
                star1.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_orange));
                star2.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_orange));
                star3.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_orange));
                star4.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_gray));
                star5.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_gray));
                break;
            }
            case 4:{
                star1.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_orange));
                star2.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_orange));
                star3.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_orange));
                star4.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_orange));
                star5.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_gray));
                break;
            }
            case 5:{
                star1.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_orange));
                star2.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_orange));
                star3.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_orange));
                star4.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_orange));
                star5.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_orange));
                break;
            }
        }
    }

    String rate = "";
    ImageView star1,star2,star3,star4,star5;

    private void dialogFavorite(String msg) {
        final Dialog dialog = new Dialog(PostDetails.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_add_favorite);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView textView = dialog.findViewById(R.id.msg);
        textView.setText(msg);

        AppCompatButton send = dialog.findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void getCarsDetails() {
        arrayListImage = new ArrayList<>();
        progressLay.setVisibility(View.VISIBLE);
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

        Api.RetrofitGetCarDetails service = retrofit.create(Api.RetrofitGetCarDetails.class);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", id);
        Call<String> call = service.putParam(hashMap);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                try {
                    JSONObject object = new JSONObject(response.body());
                    String statusCode = object.getString("status_code");
                    JSONObject data = object.getJSONObject("data");
                    switch (statusCode) {
                        case "200": {
                            String image1 = data.getString("image1");
                            if (!image1.equals("") && !image1.equals("null")) {
                                ModelSlideImg modelSlideImg = new ModelSlideImg();
                                modelSlideImg.setUrl(image1);
                                arrayListImage.add(modelSlideImg);
                            }
                            String image2 = data.getString("image2");
                            if (!image2.equals("") && !image2.equals("null")) {
                                ModelSlideImg modelSlideImg = new ModelSlideImg();
                                modelSlideImg.setUrl(image2);
                                arrayListImage.add(modelSlideImg);
                            }
                            String image3 = data.getString("image3");
                            if (!image3.equals("") && !image3.equals("null")) {
                                ModelSlideImg modelSlideImg = new ModelSlideImg();
                                modelSlideImg.setUrl(image3);
                                arrayListImage.add(modelSlideImg);
                            }
                            String image4 = data.getString("image4");
                            if (!image4.equals("") && !image4.equals("null")) {
                                ModelSlideImg modelSlideImg = new ModelSlideImg();
                                modelSlideImg.setUrl(image4);
                                arrayListImage.add(modelSlideImg);
                            }
                            String image5 = data.getString("image5");
                            if (!image5.equals("") && !image5.equals("null")) {
                                ModelSlideImg modelSlideImg = new ModelSlideImg();
                                modelSlideImg.setUrl(image5);
                                arrayListImage.add(modelSlideImg);
                            }
                            initSlider(arrayListImage);


                            price.setText(data.getString("price") + " " + "جنيه سوداني");
                            title.setText(data.getString("title"));
                            views.setText(data.getString("views"));
                            rating.setText(data.getString("rating"));
                            String desc = data.getString("description");
                            if (!desc.equals("")&&!desc.equals("null")){
                                description.setText(data.getString("description"));
                            }else {
                                description.setVisibility(View.GONE);
                            }

                            //tableEstate
                            tableCar.setVisibility(View.VISIBLE);
                            txtState2.setText(Html.fromHtml("<span style='color:#808080'>الولاية </span>"+data.getString("state")));
                            txtCity2.setText(Html.fromHtml("<span style='color:#808080'>المدينة </span>"+data.getString("city")));
                            txtType2.setText(Html.fromHtml("<span style='color:#808080'>النوع </span>"+data.getString("ad_type")));
                            txtCompany.setText(Html.fromHtml("<span style='color:#808080'>الشركة المصنعة </span>"+data.getString("company")));
                            txtTransmission.setText(Html.fromHtml("<span style='color:#808080'>نوع القيادة </span>"+data.getString("transmission")));
                            txtIs_new.setText(Html.fromHtml("<span style='color:#808080'>الحالة </span>"+data.getString("is_new")));
                            txtModel.setText(Html.fromHtml("<span style='color:#808080'>الموديل </span>"+data.getString("model")));
                            txtYear.setText(Html.fromHtml("<span style='color:#808080'>سنة الصنع </span>"+data.getString("year")));
                            txtColor.setText(Html.fromHtml("<span style='color:#808080'>اللون </span>"+data.getString("color")));
                            txtDate2.setText(Html.fromHtml("<span style='color:#808080'>تاريخ الاعلان </span>"+data.getString("created_at")));

//                            description.append("\n \nالولاية" + " : " + data.getString("state") + "\n");
//                            description.append("المدينة" + " : " + data.getString("city") + "\n");
//                            description.append("النوع" + " : " + data.getString("ad_type") + "\n");
//                            description.append("الشركة المصنعة" + " : " + data.getString("company") + "\n");
//                            description.append("نوع القيادة" + " : " + data.getString("transmission") + "\n");
//                            description.append("الحالة" + " : " + data.getString("is_new") + "\n");
//                            description.append("الموديل" + " : " + data.getString("model") + "\n");
//                            description.append("سنة الصنع" + " : " + data.getString("year") + "\n");
//                            description.append("اللون" + " : " + data.getString("color") + "\n");
//                            description.append("تاريخ الاعلان" + " : " + data.getString("created_at") + "\n");

                            phone = data.getString("owner_phone");

                            is_favourite = data.getBoolean("is_favourite");
                            Menu menu = popup.getMenu();
                            if (is_favourite) {
                                menu.getItem(0).setTitle("ازالة من المفضلة");
                            } else {
                                menu.getItem(0).setTitle("وضع في المفضلة");
                            }

                            break;
                        }

                        default: {
                            Toast.makeText(PostDetails.this, "حدث خطأ الرجاء المحاولة مرة اخرى", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }

                    progressLay.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
//                    Toast.makeText(getActivity(), "خطأ في التحويل", Toast.LENGTH_SHORT).show();
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

    private void getEstateDetails() {
        arrayListImage = new ArrayList<>();
        progressLay.setVisibility(View.VISIBLE);
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

        Api.RetrofitGetEstateDetails service = retrofit.create(Api.RetrofitGetEstateDetails.class);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", id);
        Call<String> call = service.putParam(hashMap);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                try {
                    JSONObject object = new JSONObject(response.body());
                    String statusCode = object.getString("status_code");
                    JSONObject data = object.getJSONObject("data");
                    switch (statusCode) {
                        case "200": {
                            String image1 = data.getString("image1");
                            if (!image1.equals("") && !image1.equals("null")) {
                                ModelSlideImg modelSlideImg = new ModelSlideImg();
                                modelSlideImg.setUrl(image1);
                                arrayListImage.add(modelSlideImg);
                            }
                            String image2 = data.getString("image2");
                            if (!image2.equals("") && !image2.equals("null")) {
                                ModelSlideImg modelSlideImg = new ModelSlideImg();
                                modelSlideImg.setUrl(image2);
                                arrayListImage.add(modelSlideImg);
                            }
                            String image3 = data.getString("image3");
                            if (!image3.equals("") && !image3.equals("null")) {
                                ModelSlideImg modelSlideImg = new ModelSlideImg();
                                modelSlideImg.setUrl(image3);
                                arrayListImage.add(modelSlideImg);
                            }
                            String image4 = data.getString("image4");
                            if (!image4.equals("") && !image4.equals("null")) {
                                ModelSlideImg modelSlideImg = new ModelSlideImg();
                                modelSlideImg.setUrl(image4);
                                arrayListImage.add(modelSlideImg);
                            }
                            String image5 = data.getString("image5");
                            if (!image5.equals("") && !image5.equals("null")) {
                                ModelSlideImg modelSlideImg = new ModelSlideImg();
                                modelSlideImg.setUrl(image5);
                                arrayListImage.add(modelSlideImg);
                            }
                            initSlider(arrayListImage);


                            price.setText(data.getString("price") + " " + "جنيه سوداني");
                            title.setText(data.getString("title"));
                            views.setText(data.getString("views"));
                            rating.setText(data.getString("rating"));
                            String desc = data.getString("description");
                            if (!desc.equals("")&&!desc.equals("null")){
                                description.setText(data.getString("description"));
                            }else {
                                description.setVisibility(View.GONE);
                            }

                            //tableEstate
                            tableEstate.setVisibility(View.VISIBLE);
                            txtState.setText(Html.fromHtml("<span style='color:#808080'>الولاية </span>"+data.getString("state")));
                            txtCity.setText(Html.fromHtml("<span style='color:#808080'>المدينة </span>"+data.getString("city")));
                            txtType.setText(Html.fromHtml("<span style='color:#808080'>النوع </span>"+data.getString("ad_type")));
                            txtCat.setText(Html.fromHtml("<span style='color:#808080'>التصنيف </span>"+data.getString("category")));
                            txtNoRoom.setText(Html.fromHtml("<span style='color:#808080'>عدد الغرف </span>"+data.getString("number_of_rooms")));
                            txtNeighborhood.setText(Html.fromHtml("<span style='color:#808080'>اسم الحي </span>"+data.getString("neighborhood")));
                            txtArea.setText(Html.fromHtml("<span style='color:#808080'>المساحة </span>"+data.getString("area")));
                            txtDate.setText(Html.fromHtml("<span style='color:#808080'>تاريخ الاعلان </span>"+data.getString("created_at")));


//                            description.append("\n \nالولاية" + " : " + data.getString("state") + "\n");
//                            description.append("المدينة" + " : " + data.getString("city") + "\n");
//                            description.append("النوع" + " : " + data.getString("ad_type") + "\n");
//                            description.append("التصنيف" + " : " + data.getString("category") + "\n");
//                            description.append("عدد الغرف" + " : " + data.getString("number_of_rooms") + "\n");
//                            description.append("اسم الحي" + " : " + data.getString("neighborhood") + "\n");
//                            description.append("المساحة" + " : " + data.getString("area") + "\n");
////                            description.append("الموديل"+" : "+data.getString("model")+"\n");
////                            description.append("سنة الصنع"+" : "+data.getString("year")+"\n");
////                            description.append("اللون"+" : "+data.getString("color")+"\n");
//                            description.append("تاريخ الاعلان" + " : " + data.getString("created_at") + "\n");

                            phone = data.getString("owner_phone");

                            is_favourite = data.getBoolean("is_favourite");
                            Menu menu = popup.getMenu();
                            if (is_favourite) {
                                menu.getItem(0).setTitle("ازالة من المفضلة");
                            } else {
                                menu.getItem(0).setTitle("وضع في المفضلة");
                            }

                            break;
                        }

                        default: {
                            Toast.makeText(PostDetails.this, "حدث خطأ الرجاء المحاولة مرة اخرى", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }

                    progressLay.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
//                    Toast.makeText(getActivity(), "خطأ في التحويل", Toast.LENGTH_SHORT).show();
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
    TableLayout tableEstate;
    boolean is_favourite=false;
    private void addToFavorite() {
        progressLay.setVisibility(View.VISIBLE);
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

        Api.RetrofitAddFavorite service = retrofit.create(Api.RetrofitAddFavorite.class);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", id);
        hashMap.put("type", type);
        Call<String> call = service.putParam(hashMap);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                try {
                    JSONObject object = new JSONObject(response.body());
                    String statusCode = object.getString("status_code");
                    JSONObject data = object.getJSONObject("data");
                    switch (statusCode) {
                        case "200": {

                            if (is_favourite){
                                is_favourite=false;
                            }else{
                                is_favourite=true;
                            }

                            String message = data.getString("message");
                            dialogFavorite(message);
                            Menu menu = popup.getMenu();
                            if (is_favourite) {
                                menu.getItem(0).setTitle("ازالة من المفضلة");
                            } else {
                                menu.getItem(0).setTitle("وضع في المفضلة");
                            }
                            break;
                        }

                        default: {
                            Toast.makeText(PostDetails.this, "حدث خطأ الرجاء المحاولة مرة اخرى", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }

                    progressLay.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "خطأ في التحويل", Toast.LENGTH_SHORT).show();
                }
                progressLay.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "حدث خطأ الرجاء المحاولة مر اخرى", Toast.LENGTH_SHORT).show();
//                showSnackBarBtn("حدث خطأ الرجاء المحاولة مر اخرى");
                progressLay.setVisibility(View.GONE);
            }
        });
    }

    private void rating(String value) {
        progressLay.setVisibility(View.VISIBLE);
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

        Api.RetrofitRating service = retrofit.create(Api.RetrofitRating.class);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", id);
        hashMap.put("type", type);
        hashMap.put("value", value);
        Call<String> call = service.putParam(hashMap);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                try {
                    JSONObject object = new JSONObject(response.body());
                    String statusCode = object.getString("status_code");
                    JSONObject data = object.getJSONObject("data");
                    switch (statusCode) {
                        case "200": {
//
//                            if (is_favourite){
//                                is_favourite=false;
//                            }else{
//                                is_favourite=true;
//                            }

                            String message = data.getString("message");
                            dialogFavorite(message);
//                            Menu menu = popup.getMenu();
//                            if (is_favourite) {
//                                menu.getItem(0).setTitle("ازالة من المفضلة");
//                            } else {
//                                menu.getItem(0).setTitle("وضع في المفضلة");
//                            }
                            break;
                        }

                        default: {
                            Toast.makeText(PostDetails.this, "حدث خطأ الرجاء المحاولة مرة اخرى", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }

                    progressLay.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "خطأ في التحويل", Toast.LENGTH_SHORT).show();
                }
                progressLay.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "حدث خطأ الرجاء المحاولة مر اخرى", Toast.LENGTH_SHORT).show();
//                showSnackBarBtn("حدث خطأ الرجاء المحاولة مر اخرى");
                progressLay.setVisibility(View.GONE);
            }
        });
    }

    private void reportAds(String reason) {
        progressLay.setVisibility(View.VISIBLE);
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

        Api.RetrofitReportAds service = retrofit.create(Api.RetrofitReportAds.class);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", id);
        hashMap.put("type", type);
        hashMap.put("reason", reason);
        Call<String> call = service.putParam(hashMap);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                try {
                    JSONObject object = new JSONObject(response.body());
                    String statusCode = object.getString("status_code");
                    JSONObject data = object.getJSONObject("data");
                    switch (statusCode) {
                        case "200": {
                            String message = data.getString("message");
                            dialogFavorite(message);
                            break;
                        }

                        default: {
                            Toast.makeText(PostDetails.this, "حدث خطأ الرجاء المحاولة مرة اخرى", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }

                    progressLay.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "خطأ في التحويل", Toast.LENGTH_SHORT).show();
                }
                progressLay.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "حدث خطأ الرجاء المحاولة مر اخرى", Toast.LENGTH_SHORT).show();
//                showSnackBarBtn("حدث خطأ الرجاء المحاولة مر اخرى");
                progressLay.setVisibility(View.GONE);
            }
        });
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
                switch (type) {
                    case "cars": {
                        getCarsDetails();
                        break;
                    }
                    case "realestate": {
                        getEstateDetails();
                        break;
                    }
                }
                snackbar.dismiss();
            }
        }).setActionTextColor(getResources().getColor(R.color.colorPrimary));
        snackbar.show();
    }

    private void CALL(String phone) {
        String uri = "tel:" + phone;
        Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(uri));
        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(callIntent);
    }

}