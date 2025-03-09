package com.example.smartmeteradmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ViewPagerHomeAdapter extends FragmentPagerAdapter {
    public ViewPagerHomeAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if(position==0){
            return new AdminHomeFragment();
        }else if(position==1){
            return new PlanFragment();
        } else {
            return new AdminHistoryFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if(position==0){
            return "User List";
        }else if(position==1){
            return "Plan Details";
        }else {
            return "User Usage";
        }
    }
}
