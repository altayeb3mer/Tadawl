package com.example.tadawl.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.solver.widgets.Chain;

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
import java.util.regex.Pattern;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Register extends AppCompatActivity implements View.OnClickListener {

    TextInputEditText editTextPhone, editTextName, editTextPass, editTextConfirmPass;
    TextView textViewLogin;
    AppCompatButton button;
    String phone="", name="", password="", c_password="";
    LinearLayout progressLay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();

    }

    private void init() {
        progressLay = findViewById(R.id.progressLay);
        editTextPhone = findViewById(R.id.phone);
        editTextPhone.addTextChangedListener(phoneWatcher);
        editTextName = findViewById(R.id.name);
        editTextName.addTextChangedListener(usernameWatcher);
        editTextPass = findViewById(R.id.password);
        editTextPass.addTextChangedListener(passWatcher);
        editTextConfirmPass = findViewById(R.id.c_password);
        editTextConfirmPass.addTextChangedListener(confirmPassWatcher);

        textViewLogin = findViewById(R.id.txtLogin);
        textViewLogin.setOnClickListener(this);
        button = findViewById(R.id.btn);
        button.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.txtLogin:{
                startActivity(new Intent(getApplicationContext(),Login.class));
                finish();
                break;
            }
            case R.id.btn:{
                preRegister();
                break;
            }



        }
    }

    private void preRegister() {
        if (!phone.equals("")&&!name.equals("")&&!password.equals("")&&!c_password.equals("")){
            Registration();
        }else{
            Toast.makeText(this, "الرجاء كتابة بيانات صحيحة", Toast.LENGTH_SHORT).show();
        }
    }


    private void Registration() {
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

            Api.RetrofitRegister service = retrofit.create(Api.RetrofitRegister.class);

            HashMap<String, String> hashBody = new HashMap<>();
            hashBody.put("name",name);
            hashBody.put("phone",phone);
            hashBody.put("password",password);
            hashBody.put("c_password",c_password);

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

                                startActivity(new Intent(Register.this,MainActivity.class));
                                String msg = objectData.getString("message");
                                Toast.makeText(Register.this, ""+msg, Toast.LENGTH_SHORT).show();

                                finish();
                                break;
                            }
//                        case "401":{
//                            ShowSnakBar("تم استخدام البريد الالكتروني او رقم الهاتف مسبقا");
//                            break;
//                        }
                            default:{
                                String msg = objectData.getString("message");
                                Toast.makeText(Register.this, ""+msg, Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(Register.this, "خطأ في التحويل", Toast.LENGTH_SHORT).show();
                    }
                    progressLay.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<String> call, Throwable throwable) {
                    Toast.makeText(Register.this, "خطأ في تسجيل الدخول، ربما البيانات غير صحيحة", Toast.LENGTH_SHORT).show();
                    progressLay.setVisibility(View.GONE);
                }
            });
        }



    private boolean isValidMobile(String phone) {
        if (phone.length()==10){
            return android.util.Patterns.PHONE.matcher(phone).matches();
        }
        return false;
    }

    private boolean isValidUser(String name) {
        if (name.length()>6&&name.length()<=20){
            Pattern pattern = Pattern.compile("^[a-zA-Z\\s]*$");
            return pattern.matcher(name).matches();
//            return android.util.Patterns.PHONE.matcher(name).matches();
        }
        return false;
    }


    TextWatcher usernameWatcher = new TextWatcher() {
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

            if (check.length() < 6|| check.length() > 20||isValidUser(check)) {
                editTextName.setError("مسموح من 6 الى 20 حرف");
                name = "";
            }else{
                name = check;
            }
        }

    };
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
    TextWatcher passWatcher = new TextWatcher() {
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

            if (check.length() < 8 || check.length() > 20) {
                editTextPass.setError("مسموح من 8 الى 20 خانة");
                password = "";
            }else {
                password = check;
            }
        }

    };
    TextWatcher confirmPassWatcher = new TextWatcher() {
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
            if (!check.equals(editTextPass.getText().toString())) {
                editTextConfirmPass.setError("كلمة السر غير متطابقة");
                c_password = "";
            }else {
                c_password = check;
            }
        }

    };





}