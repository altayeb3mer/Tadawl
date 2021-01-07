package com.example.tadawl.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tadawl.Activity.PostDetails;
import com.example.tadawl.Model.ModelNewAds;
import com.example.tadawl.R;
import com.example.tadawl.Utils.Api;
import com.example.tadawl.Utils.SharedPrefManager;

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


public class AdapterNewAdsDel extends RecyclerView.Adapter<AdapterNewAdsDel.ViewHolder> {

//    Typeface tf;
    int current_page, last_page;
    private ArrayList<ModelNewAds> arrayList;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Activity activity;
//    RelativeLayout container;
    LinearLayout progressLay;
    public AdapterNewAdsDel(Activity activity, ArrayList<ModelNewAds> r,LinearLayout progressLay) {
        this.mInflater = LayoutInflater.from(activity);
        this.arrayList = r;
        this.activity = activity;
        this.progressLay = progressLay;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.new_adds_rec_item_del, parent, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final ModelNewAds item = arrayList.get(position);
        try {
            Glide.with(activity).load(Api.ROOT_URL+item.getImage())
                    .into(holder.imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, PostDetails.class);
                intent.putExtra("id",item.getId());
                intent.putExtra("type",item.getType());
                activity.startActivity(intent);
            }
        });
        holder.textViewTitle.setText(item.getTitle());
        holder.textViewPrice.setText(item.getPrice()+" "+"جنيه سوداني");



        holder.layDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAds(item.getType(),item.getId(),position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }


    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        LinearLayout container,layDel;
        TextView textViewTitle, textViewPrice;

        ViewHolder(View itemView) {
            super(itemView);
            layDel = itemView.findViewById(R.id.layDel);
            imageView = itemView.findViewById(R.id.img);
            container = itemView.findViewById(R.id.container);
            textViewTitle = itemView.findViewById(R.id.title);
            textViewPrice = itemView.findViewById(R.id.price);

        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }



    private void deleteAds(String type, String id, final int position) {
        progressLay.setVisibility(View.VISIBLE);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        okhttp3.Request.Builder ongoing = chain.request().newBuilder();
                        ongoing.addHeader("Content-Type", "application/json;");
                        ongoing.addHeader("Content-Type", "application/x-www-form-urlencoded");
                        String token = SharedPrefManager.getInstance(activity).GetToken();
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

        Api.RetrofitDeletePost service = retrofit.create(Api.RetrofitDeletePost.class);
        HashMap<String, String> hashMap=new HashMap<>();
        hashMap.put("type",type);
        hashMap.put("id",id);
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
                            arrayList.remove(position);
                            notifyDataSetChanged();
                            break;
                        }

                        default: {
                            Toast.makeText(activity, "حدث خطأ الرجاء المحاولة مرة اخرى", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }

                    progressLay.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                    progressLay.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                Toast.makeText(activity, "حدث خطأ الرجاء المحاولة مر اخرى", Toast.LENGTH_SHORT).show();

                progressLay.setVisibility(View.GONE);
            }
        });
    }


}
