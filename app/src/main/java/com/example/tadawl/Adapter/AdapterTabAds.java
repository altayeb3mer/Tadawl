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

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.tadawl.Activity.PostDetails;
import com.example.tadawl.Model.ModelAds;
import com.example.tadawl.R;
import com.example.tadawl.Utils.Api;


import java.util.ArrayList;


public class AdapterTabAds extends RecyclerView.Adapter<AdapterTabAds.ViewHolder> {

//    Typeface tf;
    int current_page, last_page;
    private ArrayList<ModelAds> arrayList;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Activity activity;
    RelativeLayout contentainer;
    public AdapterTabAds(Activity activity, ArrayList<ModelAds> r) {
        this.mInflater = LayoutInflater.from(activity);
        this.arrayList = r;
        this.activity = activity;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.tab_rec_item, parent, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ModelAds item = arrayList.get(position);
        Glide.with(activity).load(Api.ROOT_URL+item.getImage())
                .into(holder.imageView);
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.startActivity(new Intent(activity, PostDetails.class));
            }
        });
        holder.textViewTitle.setText(item.getTitle());
        holder.textViewDescription.setText(item.getDescription());
        holder.textViewPrice.setText(item.getPrice()+" "+"جنيه سوداني");
        holder.textViewViews.setText(item.getViews());
        holder.textViewRating.setText(item.getRating());


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

        CardView container;
        TextView textViewTitle, textViewDescription,textViewPrice,
                textViewViews,textViewRating;

        ViewHolder(View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container);

            imageView = itemView.findViewById(R.id.img);
            textViewTitle = itemView.findViewById(R.id.title);
            textViewDescription = itemView.findViewById(R.id.description);
            textViewPrice = itemView.findViewById(R.id.price);
            textViewViews = itemView.findViewById(R.id.views);
            textViewRating = itemView.findViewById(R.id.rating);


        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }





}
