package com.example.tadawl.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.tadawl.R;
import com.example.tadawl.Utils.Api;
import com.example.tadawl.Utils.SharedPrefManager;
import com.goodiebag.pinview.Pinview;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class PinCode extends AppCompatActivity {
    AppCompatButton button;
    Pinview pinview;
    String otp="",phone="";
    LinearLayout progressLay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_code);
        Bundle args = getIntent().getExtras();
        if (args!=null){
            phone = args.getString("phone");
        }
        init();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preCheck();
            }
        });
    }

    private void preCheck(){
        if (!otp.equals("")){
            checkOtp();
        }else{
            dialogMsg("مدخلات خاطئة","الرجاء كتابة رمز التحقق (4 ارقام)");
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    private void checkOtp() {
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

        Api.RetrofitCheckOtp service = retrofit.create(Api.RetrofitCheckOtp.class);

        HashMap<String, String> hashBody = new HashMap<>();
        hashBody.put("phone",phone);
        hashBody.put("otp",otp);

        Call<String> call = service.putParam(hashBody);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                try {
                    JSONObject object = new JSONObject(response.body());
                    String statusCode = object.getString("status_code");
                    JSONObject objectData=new JSONObject(object.getString("data"));
                    switch (statusCode){
                        case "200":{
                            boolean otpValue = objectData.getBoolean("is_otp_correct");
                            if (otpValue){
                                boolean registerBefore = objectData.getBoolean("is_phone_registered");
                                if (registerBefore){
                                    startActivity(new Intent(PinCode.this,MainActivity.class));
                                    String token = objectData.getString("token");
                                    SharedPrefManager.getInstance(getApplicationContext()).storeToken(token);
                                }else{
                                    Intent intent =new Intent(PinCode.this,Register.class);
                                    intent.putExtra("phone",phone);
                                    startActivity(intent);
                                }
                                finish();
                            }else{
                                dialogMsg("الرمز خطأ","رمز التحقق خاطئ ، الرجاء المحاولة مرة اخرى!");
                            }

//                            String msg = objectData.getString("message");
//                            Toast.makeText(PinCode.this, ""+msg, Toast.LENGTH_SHORT).show();


                            break;
                        }
//                        case "401":{
//                            ShowSnakBar("تم استخدام البريد الالكتروني او رقم الهاتف مسبقا");
//                            break;
//                        }
                        default:{
                            String msg = objectData.getString("message");
                            Toast.makeText(PinCode.this, ""+msg, Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(PinCode.this, "خطأ في التحويل", Toast.LENGTH_SHORT).show();
                }
                progressLay.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                Toast.makeText(PinCode.this, "خطأ في تسجيل الدخول، ربما البيانات غير صحيحة", Toast.LENGTH_SHORT).show();
                progressLay.setVisibility(View.GONE);
            }
        });
    }

    private void init() {
        progressLay = findViewById(R.id.progressLay);
        button = findViewById(R.id.btn);
        pinview = findViewById(R.id.pinview);
        pinview.setPinViewEventListener(new Pinview.PinViewEventListener() {
            @Override
            public void onDataEntered(Pinview pinview, boolean fromUser) {
                    otp = pinview.getValue().trim();

            }
        });
    }



    private void dialogMsg(String title,String msg){
        AlertDialog alertDialog = new AlertDialog.Builder(PinCode.this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(msg);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "موافق",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }


}