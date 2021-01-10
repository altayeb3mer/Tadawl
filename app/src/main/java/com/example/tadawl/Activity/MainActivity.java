package com.example.tadawl.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.example.tadawl.Fragment.Fragment2;
import com.example.tadawl.Fragment.Fragment3;
import com.example.tadawl.Fragment.Fragment4;
import com.example.tadawl.Fragment.FragmentMain;
import com.example.tadawl.R;
import com.example.tadawl.Utils.CustomViewPager;
import com.example.tadawl.Adapter.ViewPagerAdapter;
import com.example.tadawl.Utils.SharedPrefManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements  BottomNavigationView.OnNavigationItemSelectedListener,NavigationView.OnNavigationItemSelectedListener{

    BottomNavigationView bottomNavigationView;
    private CustomViewPager viewPager;
    DrawerLayout drawerLayout;
    public static NavigationView navigationView;
    LinearLayout nav_drawer_lay,laySearch;
    ImageView addPost;
    MenuItem menuItemLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_drawer);
//        String token = SharedPrefManager.getInstance(this).GetToken();
//        Toast.makeText(this, token, Toast.LENGTH_SHORT).show();
        init();

    }


    RelativeLayout navBtnLay;
    @Override
    protected void onResume() {
        super.onResume();
        logeddInOrNot();
    }

    private void logeddInOrNot() {
        String token = new SharedPrefManager(this).GetToken();
        if (!token.equals("")){
            hideLoginItem();
            navBtnLay.setVisibility(View.VISIBLE);
        }else {
            showLoginItem();
            navBtnLay.setVisibility(View.GONE);
        }
    }

    private void init() {
        laySearch =  findViewById(R.id.laySearch);
        navBtnLay =  findViewById(R.id.r1);
        laySearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),SearchActivity.class));
            }
        });
        addPost =  findViewById(R.id.addPost);
        nav_drawer_lay =  findViewById(R.id.nav_drawer_lay);
        viewPager =  findViewById(R.id.viewpager);
        bottomNavigationView = findViewById(R.id.btn_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        drawerLayout = findViewById(R.id.n_drawer);
        navigationView = findViewById(R.id.n_view);
        navigationView.setItemIconTintList(null);

        nav_drawer_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        navigationView.setNavigationItemSelectedListener(this);

        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,AddNewPost.class));
            }
        });
        setupViewPager(viewPager);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.btn_nav1:{
                switchToFragment(1);
                break;
            }
            case R.id.btn_nav2:{
                switchToFragment(2);
                break;
            }
            case R.id.btn_nav3:{
                switchToFragment(3);
                break;
            }
            case R.id.btn_nav4:{
                switchToFragment(4);
                break;
            }
            //nav menu
            case R.id.nav_menu_sc1:{
//                startActivity(new Intent(getApplicationContext(),Login.class));
                Toast.makeText(this, "لاحقا", Toast.LENGTH_SHORT).show();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            }
            case R.id.nav_menu_sc2:{
//                startActivity(new Intent(getApplicationContext(),Register.class));
                Toast.makeText(this, "لاحـقا", Toast.LENGTH_SHORT).show();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            }
            case R.id.nav_menu_favorite:{
                startActivity(new Intent(getApplicationContext(),FavoriteAds.class));
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            }
            case R.id.nav_menu_login:{
                startActivity(new Intent(getApplicationContext(),Login.class));
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            }

        }
        return true;
    }

    public void switchToFragment(int f_no) {
//        FragmentManager manager = getSupportFragmentManager();
        switch (f_no) {
            case 1: {
                viewPager.setCurrentItem(0);
                SetNavigationItemSelected(R.id.btn_nav1);
                break;
            }
            case 2: {
                viewPager.setCurrentItem(1);
                SetNavigationItemSelected(R.id.btn_nav2);
                break;
            }
            case 3: {
                viewPager.setCurrentItem(2);
                SetNavigationItemSelected(R.id.btn_nav3);
                break;
            }
            case 4: {
                viewPager.setCurrentItem(3);
                SetNavigationItemSelected(R.id.btn_nav4);
                break;
            }

        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        FragmentMain  fragmentMain= new FragmentMain();
        Fragment2 fragment2 = new Fragment2();
        Fragment3 fragment3 = new Fragment3();
        Fragment4 fragment4 = new Fragment4();

        adapter.addFragment(fragmentMain,"الرئيسية");
        adapter.addFragment(fragment2,"البروفايل");
        adapter.addFragment(fragment3,"منشورات");
        adapter.addFragment(fragment4,"الضبط");
        viewPager.setAdapter(adapter);
    }

    private void SetNavigationItemSelected(int id){
        bottomNavigationView.getMenu().findItem(id).setChecked(true);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (viewPager.getCurrentItem()==0){
                super.onBackPressed();
            }else{
                switchToFragment(1);
//                viewPager.setCurrentItem(0);
            }
        }
    }

    private void hideLoginItem(){
        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.nav_menu_login).setVisible(false);
    }
    private void showLoginItem(){
        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.nav_menu_login).setVisible(true);
    }


}