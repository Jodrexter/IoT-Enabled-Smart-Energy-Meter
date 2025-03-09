package com.example.smartmeteradmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

public class AdminHome extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager pager;
    ViewPagerHomeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        tabLayout=(TabLayout) findViewById(R.id.tabLayout);
        pager=(ViewPager) findViewById(R.id.viewPager);

        adapter=new ViewPagerHomeAdapter(getSupportFragmentManager());

        pager.setAdapter(adapter);

        tabLayout.setupWithViewPager(pager);
    }
}