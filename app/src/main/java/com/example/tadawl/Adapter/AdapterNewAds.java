package com.example.tadawl.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.example.tadawl.Model.ModelAds;
import com.example.tadawl.Model.ModelNewAds;
import com.example.tadawl.R;

import java.util.ArrayList;


public class AdapterNewAds extends RecyclerView.Adapter<AdapterNewAds.ViewHolder> {

//    Typeface tf;
    int current_page, last_page;
    private ArrayList<ModelNewAds> arrayList;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Activity activity;
    RelativeLayout container;
    public AdapterNewAds(Activity activity, ArrayList<ModelNewAds> r) {
        this.mInflater = LayoutInflater.from(activity);
        this.arrayList = r;
        this.activity = activity;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.new_adds_rec_item, parent, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ModelNewAds item = arrayList.get(position);
//        Glide.with(activity).load(URL.ROOT_IMG+item.getImgUrl())
//                .into(holder.imageView);
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


        ViewHolder(View itemView) {
            super(itemView);
//            imageView = itemView.findViewById(R.id.img);

        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }





}
