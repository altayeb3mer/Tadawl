package com.example.tadawl.Activity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.tadawl.Adapter.SlideShow_adapter;
import com.example.tadawl.Model.ModelSlideImg;
import com.example.tadawl.R;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;

public class PostDetails extends AppCompatActivity {

    ViewPager viewPager;
    SlideShow_adapter slideShowAdapter;
    ArrayList<ModelSlideImg> arrayList;
    CircleIndicator circleIndicator;
    Toolbar toolbar;

    LinearLayout layoutMenu;
    PopupMenu popup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.hideOverflowMenu();


        init();
    }

    private void init() {
        layoutMenu = findViewById(R.id.layMenu);
        layoutMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                PostDetails.this.openOptionsMenu();
                //popup menu
                popup = new PopupMenu(PostDetails.this, layoutMenu);
                popup.getMenuInflater()
                        .inflate(R.menu.option_menu_post_details, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.item1:{
                                Toast.makeText(PostDetails.this, "item1", Toast.LENGTH_SHORT).show();
                                break;
                            }
                            case R.id.item2:{
                                openDialog();
//                                Toast.makeText(PostDetails.this, "item2", Toast.LENGTH_SHORT).show();
                                break;
                            }

                        }
                        return true;
                    }
                });
                popup.show();

            }
        });
        arrayList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ModelSlideImg modelSlideImg = new ModelSlideImg();
            modelSlideImg.setId(i + "");
            arrayList.add(modelSlideImg);
        }
        viewPager = findViewById(R.id.viewpager);
        slideShowAdapter = new SlideShow_adapter(getApplicationContext(), arrayList);
        circleIndicator = findViewById(R.id.indicator);
        viewPager.setAdapter(slideShowAdapter);
        circleIndicator.setViewPager(viewPager);

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return false;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.option_menu_post_details, menu);//Menu Resource, Menu
        return true;
    }
    @Override    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:{
                Toast.makeText(this, "item1", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.item2:{
                Toast.makeText(this, "item2", Toast.LENGTH_SHORT).show();
                break;
            }

        }
        return true;
    }



    private void openDialog() {
        final Dialog dialog = new Dialog(PostDetails.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        AppCompatButton send = dialog.findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }



}