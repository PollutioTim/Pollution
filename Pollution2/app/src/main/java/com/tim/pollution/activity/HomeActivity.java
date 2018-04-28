package com.tim.pollution.activity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.tim.pollution.R;
import com.tim.pollution.adapter.HomeFragmentAdapter;
import com.tim.pollution.bean.changetrend.RegionNetBean;
import com.tim.pollution.callback.ICallBack;
import com.tim.pollution.fragment.WeatherFragment;
import com.tim.pollution.general.Constants;
import com.tim.pollution.general.MData;
import com.tim.pollution.general.MDataType;
import com.tim.pollution.net.WeatherDal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import android.widget.LinearLayout.LayoutParams;
public class HomeActivity extends AppCompatActivity implements ICallBack{


    @BindView(R.id.weather_title_location)
    TextView weatherTitleLocation;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.weather_title_location_select)
    TextView weatherTitleLocationSelect;
    @BindView(R.id.home_vp)
    ViewPager homeVp;
    @BindView(R.id.home_top)
    LinearLayout homeTop;
    @BindView(R.id.ll_dots)
    LinearLayout llDots;
    private ArrayList<ImageView> dotsList;

    private List<Fragment> fragments=new ArrayList<>();
    private RegionNetBean regionNetBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        loadLocation();
//        loadData();

    }

    private void loadLocation() {
        if(true){
            Map<String ,String>params=new HashMap<>();
            params.put("key", Constants.key);
            params.put("regiontype","allregion");
            WeatherDal.getInstance().getRegion(params,this);
        }
    }

    private void loadData() {
        //todo 写死
        List<String> regionIds=new ArrayList<>();
//        regionIds.add("140101");
//        regionIds.add("14121");
        regionIds.add("140123");
        regionIds.add("140201");
        if(regionNetBean==null||regionNetBean.getMessage()==null){
            return;
        }
        regionIds.add(regionNetBean.getMessage().get(0).getRegionId());
        for (int i=0;i<regionIds.size();i++){
            WeatherFragment fragment=new WeatherFragment();
            Bundle bundle = new Bundle();
            bundle.putString("regionId",regionIds.get(i));//这里的values就是我们要传的值
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }
        initDots();
        homeVp.setAdapter(new HomeFragmentAdapter(getSupportFragmentManager(),fragments));
        homeVp.setCurrentItem(0);
        // 设置ViewPager的监听
        homeVp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                //遍历存放图片的数组
                for (int i = 0; i < fragments.size(); i++) {
                    //判断小点点与当前的图片是否对应，对应设置为亮色 ，否则设置为暗色
                    if (i == position %fragments.size()) {
                        dotsList.get(i).setImageDrawable(
                                getResources().getDrawable(
                                        R.drawable.dot_white));
                    } else {
                        dotsList.get(i).setImageDrawable(
                                getResources().getDrawable(
                                        R.drawable.dot_gray));
                    }

                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }


    /**
     * 初始化小点
     */
    private void initDots() {
        //创建存放小点点的集合
        dotsList = new ArrayList<ImageView>();
        //每次初始化之前清空集合
        dotsList.clear();
        // 每次初始化之前  移除  布局中的所有小点
        llDots.removeAllViews();
        for (int i = 0; i < fragments.size(); i++) {
            //创建小点点图片
            ImageView imageView = new ImageView(this);
            Drawable drawable = null;
            if (i == 0) {
                // 亮色图片
                drawable = getResources().getDrawable(R.drawable.dot_white);

            } else {
                drawable = getResources().getDrawable(R.drawable.dot_gray);
            }
            imageView.setImageDrawable(drawable);
            // 考虑屏幕适配
            LayoutParams params = new LayoutParams(dip2px(this, 10), dip2px(this, 10));
            //设置小点点之间的间距    
            params.setMargins(dip2px(this, 5), 0, dip2px(this, 5), 0);
            //将小点点添加大线性布局中
            llDots.addView(imageView, params);
            // 将小点的控件添加到集合中
            dotsList.add(imageView);
        }
    }
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public void onProgress(Object data) {
        MData mData = (MData) data;
        if (MDataType.REGIONNET_BEAN.equals(mData.getType())) {
            regionNetBean = (RegionNetBean) mData.getData();
//            regionNetBean = getTest();
            if (regionNetBean != null) {
                loadData();
            }
        }

    }

    @Override
    public void onError(String msg, String eCode) {

    }

}
