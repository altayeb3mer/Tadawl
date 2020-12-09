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
    //add car
    public interface RetrofitAddCar {
        @POST("api/add-car")
        Call<String> putParam(@Body HashMap<String, String> params);
    }
    //car companies
    public interface RetrofitCarCompanies {
        @GET("api/companies")
        Call<String> putParam();
    }
    //get popular
    public interface RetrofitGetPopular {
        @GET("api/ads")
        Call<String> putParam(@QueryMap HashMap<String, String> params);
    }
    //get all
    public interface RetrofitGetAll{
        @GET("api/ads")
        Call<String> putParam();
    }
    //get cars or estate
    public interface RetrofitGetCarsOrEstate {
        @GET("api/ads")
        Call<String> putParam(@QueryMap HashMap<String, String> params);
    }
    //get cars details
    public interface RetrofitGetCarDetails {
        @GET("api/car")
        Call<String> putParam(@QueryMap HashMap<String, String> params);
    }
    //get estate details
    public interface RetrofitGetEstateDetails {
        @GET("api/realestate")
        Call<String> putParam(@QueryMap HashMap<String, String> params);
    }
    //get favorite
    public interface RetrofitGetFavorite {
        @GET("api/favourite-ads")
        Call<String> putParam(@QueryMap HashMap<String, String> params);
    }
    //add to favorite
    public interface RetrofitAddFavorite {
        @POST("api/toggle-favourite")
        Call<String> putParam(@Body HashMap<String, String> params);
    }
    //report
    public interface RetrofitReportAds {
        @POST("api/report")
        Call<String> putParam(@Body HashMap<String, String> params);
    }
    //rate
    public interface RetrofitRating {
        @POST("api/rate")
        Call<String> putParam(@Body HashMap<String, String> params);
    }


}
