package com.example.copycare;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class BannerAdapter extends FragmentStateAdapter {
    public int mCount;
    public BannerAdapter(FragmentManager fm, Lifecycle lifecycle,int count) {
        super(fm,lifecycle);
        mCount = count;
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        int index = getRealPosition(position);
        if(index==0) return new FragBanner1();
        else if(index==1) return new FragBanner2();
        else return new FragBanner3();
    }
    @Override
    public int getItemCount() {
        return 2000;
    }
    public int getRealPosition(int position) { return position % mCount; }

//    @Override
//    public BannerAdapter(FragmentManager fm){
//        super(fm)
//    }

}

