package com.example.tadawl.Utils;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

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

    //category
    public interface RetrofitCategory {
        @GET("api/categories")
        Call<String> putParam();
    }

    //state
    public interface RetrofitState {
        @GET("api/states")
        Call<String> putParam();
    }

    //city
    public interface RetrofitCity {
        @GET("api/cities")
        Call<String> putParam(@QueryMap HashMap<String, String> params);
    }
    //add estate
    public interface RetrofitAddEstate {
        @POST("api/add-realestate")
        Call<String> putParam(@Body HashMap<String, String> params);
    }


}
