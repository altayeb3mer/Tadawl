package com.example.tadawl.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.tadawl.Model.ModelSlideImg;
import com.example.tadawl.R;
import com.example.tadawl.Utils.Api;

import java.util.ArrayList;

/**
 * Created by Altayeb on 10/25/2018.
 */
public class SlideShow_adapter extends PagerAdapter {
    public ArrayList<ModelSlideImg> arrayList;
    LayoutInflater inflater;
    int detail;
    private Context context;


    public SlideShow_adapter(Context context, ArrayList<ModelSlideImg> arrayList) {
        this.context = context;

        this.arrayList = arrayList;
        //this.detail=det;
    }


    @Override
    public int getCount() {

        return arrayList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == (RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ModelSlideImg item =  arrayList.get(position);

        View view = inflater.inflate(R.layout.slider_img, container, false);
        ImageView imageView = view.findViewById(R.id.img);

        Glide.with(context).load(Api.ROOT_URL+item.getUrl()).into(imageView);

        container.addView(view);


        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}
