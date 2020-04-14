package com.example.a86130.zhangmiaoyin.adapter;

import com.example.a86130.zhangmiaoyin.fragment.HomeFragmet;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


public class HomeVpAdapter extends FragmentPagerAdapter {
    private List<HomeFragmet> mFragments;

    public HomeVpAdapter(@NonNull FragmentManager fm, List<HomeFragmet> fragments) {
        super(fm);
        this.mFragments = fragments;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
}
