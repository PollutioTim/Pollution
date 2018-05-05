package com.tim.pollution.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by tcy on 2018/4/27.
 */

public class HomeFragmentAdapter extends FragmentStatePagerAdapter {
    private FragmentManager mfragmentManager;
    private List<Fragment> mlist;

    public HomeFragmentAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.mlist = list;
    }

    @Override
    public Fragment getItem(int arg0) {
        return mlist.get(arg0);//显示第几个页面
    }


    @Override
    public int getCount() {
        return mlist.size();//有几个页面
    }

    private int mChildCount = 0;


    @Override

    public void notifyDataSetChanged() {

        mChildCount = getCount();

        super.notifyDataSetChanged();

    }


    @Override

    public int getItemPosition(Object object) {

        return POSITION_NONE;

    }
}
