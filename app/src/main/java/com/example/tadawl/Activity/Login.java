package com.example.tadawl.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tadawl.R;
import com.example.tadawl.Utils.Api;
import com.example.tadawl.Utils.SharedPrefManager;
import com.google.android.material.textfield.TextInputEditText;

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

public class Login extends AppCompatActivity  implements View.OnClickListener {
    TextInputEditText editTextPhone, editTextPass;
    TextView textViewRegister;
    AppCompatButton button;
    String phone="",password;
    LinearLayout progressLay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init() {
        editTextPass = findViewById(R.id.password);
        textViewRegister = findViewById(R.id.txtRegister);
        textViewRegister.setOnClickListener(this);
        progressLay = findViewById(R.id.progressLay);
        editTextPhone = findViewById(R.id.phone);
        editTextPhone.addTextChangedListener(phoneWatcher);




        button = findViewById(R.id.btn);
        button.setOnClickListener(this);
    }

    private boolean isValidMobile(String phone) {
        if (phone.length()==10){
            return android.util.Patterns.PHONE.matcher(phone).matches();
        }
        return false;
    }
    TextWatcher phoneWatcher = new TextWatcher() {
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

            if (!isValidMobile(check)){
                editTextPhone.setError("رقم هاتف غير مقبول (مسموح بـ 10 ارقام)");
                phone = "";
            }else{
                phone = check;
            }

        }

    };
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.txtRegister:{
                startActivity(new Intent(getApplicationContext(),Register.class));
                finish();
                break;
            }
            case R.id.btn:{
                preLogin();
                break;
            }

        }
    }

    private void preLogin() {
        password = editTextPass.getText().toString();
        if (!phone.equals("")&&!password.equals("")){
            LoginFun();
        }else{
            Toast.makeText(this, "الرجاء كتابة بيانات صحيحة", Toast.LENGTH_SHORT).show();
        }
    }

    private void LoginFun() {
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

        Api.RetrofitLogin service = retrofit.create(Api.RetrofitLogin.class);

        HashMap<String, String> hashBody = new HashMap<>();
        hashBody.put("phone",phone);
        hashBody.put("password",password);

        Call<String> call = service.putParam(hashBody);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                try {
                    JSONObject object = new JSONObject(response.body());
                    String statusCode = object.getString("status_code");
                    String data = object.getString("data");
                    JSONObject objectData=new JSONObject(data);
                    switch (statusCode){
                        case "200":{
                            String token = objectData.getString("token");

                            SharedPrefManager.getInstance(getApplicationContext()).storeToken(token);

                            startActivity(new Intent(Login.this,MainActivity.class));
                            String msg = objectData.getString("message");
                            Toast.makeText(Login.this, ""+msg, Toast.LENGTH_SHORT).show();

                            finish();
                            break;
                        }
//                        case "401":{
//                            ShowSnakBar("تم استخدام البريد الالكتروني او رقم الهاتف مسبقا");
//                            break;
//                        }
                        default:{
                            String msg = objectData.getString("message");
                            Toast.makeText(Login.this, ""+msg, Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(Login.this, "خطأ في التحويل", Toast.LENGTH_SHORT).show();
                }
                progressLay.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                Toast.makeText(Login.this, "خطأ في تسجيل الدخول، ربما البيانات غير صحيحة", Toast.LENGTH_SHORT).show();
                progressLay.setVisibility(View.GONE);
            }
        });
    }

}