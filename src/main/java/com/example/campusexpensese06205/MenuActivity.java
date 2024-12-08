package com.example.campusexpensese06205;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.example.campusexpensese06205.adapter.ViewPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    NavigationView navigationView;
    BottomNavigationView bottomNavigationView;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ViewPager2 viewPager2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_main);
        navigationView = findViewById(R.id.nav_view);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        viewPager2 = findViewById(R.id.viewPager);
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,
                drawerLayout, toolbar,
                R.string.open_nav,
                R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        Menu menu = navigationView.getMenu();
        MenuItem logout = menu.findItem(R.id.nav_logout);
        setupviewPager2();
        clickItemTabMenu();

        // viet su kien logout
        logout.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                Intent intentLogout = new Intent(MenuActivity.this, LoginActivity.class);
                startActivity(intentLogout);
                finish();
                return false;
            }
        });

    }
    private void clickItemTabMenu(){
        bottomNavigationView.setOnItemSelectedListener(item ->{
            if(item.getItemId()==R.id.home_menu){
                viewPager2.setCurrentItem(0);
            } else if (item.getItemId()==R.id.expenses_menu) {
                viewPager2.setCurrentItem(1);
            } else if (item.getItemId()==R.id.budget_menu) {
                viewPager2.setCurrentItem(2);
            } else if (item.getItemId()==R.id.setting_menu) {
                viewPager2.setCurrentItem(3);
            }else{
                viewPager2.setCurrentItem(0);
            }
           return true;
        });
    }
    private void setupviewPager2(){
        ViewPagerAdapter viewPager = new ViewPagerAdapter(getSupportFragmentManager(),getLifecycle());
       viewPager2.setAdapter(viewPager);
       viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
           @Override
           public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
               super.onPageScrolled(position, positionOffset, positionOffsetPixels);
           }

           @Override
           public void onPageSelected(int position) {
               super.onPageSelected(position);
               if(position ==0 ){
                   bottomNavigationView.getMenu().findItem(R.id.home_menu).setChecked(true);
               } else if (position ==1 ) {
                   bottomNavigationView.getMenu().findItem(R.id.expenses_menu).setChecked(true);
               } else if (position ==2 ) {
                   bottomNavigationView.getMenu().findItem(R.id.budget_menu).setChecked(true);

               } else if (position ==3) {
                   bottomNavigationView.getMenu().findItem(R.id.setting_menu).setChecked(true);
               }else{
                   bottomNavigationView.getMenu().findItem(R.id.home_menu).setChecked(true);
               }
           }

           @Override
           public void onPageScrollStateChanged(int state) {
               super.onPageScrollStateChanged(state);
           }
       });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.home_menu){
            viewPager2.setCurrentItem(0);
        } else if (item.getItemId()==R.id.expenses_menu) {
            viewPager2.setCurrentItem(1);
        } else if (item.getItemId()==R.id.budget_menu) {
            viewPager2.setCurrentItem(2);
        } else if (item.getItemId()==R.id.setting_menu) {
            viewPager2.setCurrentItem(3);
        }
        drawerLayout.closeDrawer((GravityCompat.START));
        return true;
    }
}
