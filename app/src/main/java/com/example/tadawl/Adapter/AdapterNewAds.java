package com.example.tadawl.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tadawl.Activity.PostDetails;
import com.example.tadawl.Model.ModelAds;
import com.example.tadawl.Model.ModelNewAds;
import com.example.tadawl.R;
import com.example.tadawl.Utils.Api;

import java.util.ArrayList;


public class AdapterNewAds extends RecyclerView.Adapter<AdapterNewAds.ViewHolder> {

//    Typeface tf;
    int current_page, last_page;
    private ArrayList<ModelNewAds> arrayList;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Activity activity;
//    RelativeLayout container;
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
        try {
            Glide.with(activity).load(Api.ROOT_URL+item.getImage())
                    .into(holder.imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.startActivity(new Intent(activity, PostDetails.class));
            }
        });
        holder.textViewTitle.setText(item.getTitle());
        holder.textViewPrice.setText(item.getPrice()+" "+"جنيه سوداني");




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
        LinearLayout container;
        TextView textViewTitle, textViewPrice;

        ViewHolder(View itemView) {
            super(itemView);
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





}
