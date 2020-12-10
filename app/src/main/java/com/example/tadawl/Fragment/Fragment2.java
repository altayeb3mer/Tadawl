package com.example.tadawl.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.tadawl.Activity.PostDetails;
import com.example.tadawl.R;
import com.example.tadawl.Utils.Api;
import com.example.tadawl.Utils.SharedPrefManager;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static android.app.Activity.RESULT_OK;


public class Fragment2 extends Fragment {

    View view;
    CircleImageView circleImageView;
    TextView textViewName, textViewPhone;
    LinearLayout progressLay;
    String s_name = "", s_phone = "", s_img = "";
    ImageView imgEditName,imgEditPhone;
    public static final int PICK_IMAGE = 1;

    public Fragment2() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_2, container, false);
        init();
        getMyProfile();
        return view;
    }

    @Override
    public void onResume() {
        setFields();
        super.onResume();
    }

    private void init() {
        imgEditName = view.findViewById(R.id.edit_name);
        imgEditPhone = view.findViewById(R.id.edit_phone);
        imgEditName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogEdit("name");
            }
        });
        imgEditPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogEdit("phone");
            }
        });

        circleImageView = view.findViewById(R.id.imgProfile);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();
            }
        });
        textViewName = view.findViewById(R.id.name);
        textViewPhone = view.findViewById(R.id.phone);
        progressLay = view.findViewById(R.id.progressLay);
    }

    private void setFields() {
        textViewName.setText(s_name);
        textViewPhone.setText(s_phone);
        if (!s_img.equals("")&&!s_img.equals("null")){
            Glide.with(getActivity()).load(Api.ROOT_URL+s_img).into(circleImageView);
        }
    }

    private void editProfile(final String type, final String value) {
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
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.ROOT_URL)
                .client(httpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api.RetrofitEditProfile service = retrofit.create(Api.RetrofitEditProfile.class);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(type, value);
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
                            switch (type){
                                case "image":{
                                    dialogDone("تم تغيير صورة البروفايل");
                                    break;
                                }
                                case "name":{
                                    textViewName.setText(value);
                                    dialogDone("تم تعديل الاسم");
                                    break;
                                }
                                case "phone":{
                                    textViewPhone.setText(value);
                                    dialogDone("تم تعديل رقم الهاتف");
                                    break;
                                }
                            }
                            break;
                        }

                        default: {
                            Toast.makeText(getActivity(), "حدث خطأ الرجاء المحاولة مرة اخرى", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                    progressLay.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                progressLay.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                progressLay.setVisibility(View.GONE);
            }
        });
    }

    private void getMyProfile() {
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
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.ROOT_URL)
                .client(httpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api.RetrofitGetProfile service = retrofit.create(Api.RetrofitGetProfile.class);

        Call<String> call = service.putParam();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                try {
                    JSONObject object = new JSONObject(response.body());
                    String statusCode = object.getString("status_code");
                    JSONObject data = object.getJSONObject("data");
                    switch (statusCode) {
                        case "200": {
                            s_name = data.getString("name");
                            s_phone = data.getString("phone");
                            s_img = data.getString("image");
                            setFields();
                            break;
                        }

                        default: {
                            Toast.makeText(getActivity(), "حدث خطأ الرجاء المحاولة مرة اخرى", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                    progressLay.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();

                }
                progressLay.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                progressLay.setVisibility(View.GONE);
            }
        });
    }

    private void dialogEdit(final String type) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog);
        final EditText editText = dialog.findViewById(R.id.reason);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final TextView textViewTitle = dialog.findViewById(R.id.txtTitle);
        switch (type){
            case "name":{
                editText.setText(textViewName.getText().toString().trim());
                textViewTitle.setText("تعديل الاسم");
                break;
            }
            case "phone":{
                editText.setText(textViewPhone.getText().toString().trim());
                textViewTitle.setText("تعديل رقم الهاتف");
                break;
            }
        }

        LinearLayout layBack = dialog.findViewById(R.id.layBack);
        layBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        AppCompatButton send = dialog.findViewById(R.id.send);
        send.setText("تعديل");
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String reason = editText.getText().toString().trim();
                if (!reason.equals("")){
                    switch (type){
                        case "name":{
                            editProfile("name",reason);
                            break;
                        }
                        case "phone":{
                            editProfile("phone",reason);
                            break;
                        }
                    }
                    dialog.dismiss();
                }else{
                    Toast.makeText(getActivity(), "الرجاء ملء حقل التعديل", Toast.LENGTH_SHORT).show();
                }


            }
        });
        dialog.show();
    }

    private void dialogDone(String msg) {
        final Dialog dialog = new Dialog(getActivity());
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
                final Uri imageUri = data.getData();
                final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                circleImageView.setImageBitmap(selectedImage);
                editProfile("image",getStringFromImg(selectedImage));

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




}