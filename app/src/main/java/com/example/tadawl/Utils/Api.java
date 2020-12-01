package com.example.tadawl.Utils;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class Api {
    public static String ROOT_URL = "http://maqsood.com.sd/tadawol/public/";



    //registration
    public interface RetrofitRegister {
        @POST("api/register")
        Call<String> putParam(@Body HashMap<String, String> param);
    }
    //login
    public interface RetrofitLogin {
        @POST("api/login")
        Call<String> putParam(@Body HashMap<String, String> param);
    }





}
