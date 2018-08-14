package com.tim.pollution;

import android.Manifest;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.baidu.mobstat.StatService;
import com.hjm.bottomtabbar.BottomTabBar;
import com.tim.pollution.activity.AboutUsActivity;
import com.tim.pollution.fragment.AboutFragment;
import com.tim.pollution.fragment.CityContrastFragment;
import com.tim.pollution.fragment.FirstPageFragment;
import com.tim.pollution.fragment.MainFragment;
import com.tim.pollution.fragment.MapFragment;
import com.tim.pollution.fragment.NewMapFragment;
import com.tim.pollution.fragment.PKFragment;
import com.tim.pollution.fragment.SellersFragment;
import com.tim.pollution.general.BaseActivity;
import com.tim.pollution.view.CustomViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.fragment_container)
    CustomViewPager viewPager;

    @BindView(R.id.home_rg)
    RadioGroup radioGroup;
    @BindView(R.id.home_main)
    RadioButton rbMain;
    @BindView(R.id.home_map)
    RadioButton rbMap;
    @BindView(R.id.home_sellers)
    RadioButton rbSellers;
    @BindView(R.id.home_pk)
    RadioButton rbPk;
    @BindView(R.id.home_about)
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
        Log.d("test","00002");
        // 设置appKey
        StatService.setAppKey("a362fc3133");
        StatService.start(this);

        fragments = new ArrayList<>();
        fragments.add(new FirstPageFragment());
        fragments.add(new NewMapFragment());
        fragments.add(new SellersFragment());
        fragments.add(new CityContrastFragment());
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
