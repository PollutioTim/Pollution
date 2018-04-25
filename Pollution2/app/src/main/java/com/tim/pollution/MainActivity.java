package com.tim.pollution;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.hjm.bottomtabbar.BottomTabBar;
import com.tim.pollution.activity.AboutUsActivity;
import com.tim.pollution.fragment.AboutFragment;
import com.tim.pollution.fragment.MainFragment;
import com.tim.pollution.fragment.MapFragment;
import com.tim.pollution.fragment.PKFragment;
import com.tim.pollution.fragment.SellersFragment;
import com.tim.pollution.view.CustomViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.fragment_container)
    CustomViewPager viewPager;

    @Bind(R.id.home_rg)
    RadioGroup radioGroup;
    @Bind(R.id.home_main)
    RadioButton rbMain;
    @Bind(R.id.home_map)
    RadioButton rbMap;
    @Bind(R.id.home_sellers)
    RadioButton rbSellers;
    @Bind(R.id.home_pk)
    RadioButton rbPk;
    @Bind(R.id.home_about)
    RadioButton rbAbout;

    private MapFragment mapFragment;
    private AboutFragment aboutFragment;
    private MainFragment mainFragment;
    private SellersFragment sellersFragment;
    private PKFragment pkFragment;
    private List<Fragment> fragments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        fragments = new ArrayList<>();
        fragments.add(new MainFragment());
        fragments.add(new MapFragment());
        fragments.add(new SellersFragment());
        fragments.add(new PKFragment());
        fragments.add(new AboutFragment());
        viewPager.setSlide(false);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }
        });

        //初始
        viewPager.setCurrentItem(0);

        radioGroup.setOnCheckedChangeListener(onCheckedChangeListener);
    }

    private RadioGroup.OnCheckedChangeListener onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            switch (i){
                case R.id.home_main:
                    viewPager.setCurrentItem(0,false);
                    break;
                case R.id.home_map:
                    viewPager.setCurrentItem(1,false);
                    break;
                case R.id.home_sellers:
                    viewPager.setCurrentItem(2,false);
                    break;
                case R.id.home_pk:
                    viewPager.setCurrentItem(3,false);
                    break;
                case R.id.home_about:
                    viewPager.setCurrentItem(4,false);
                    break;
            }
        }
    };


}
