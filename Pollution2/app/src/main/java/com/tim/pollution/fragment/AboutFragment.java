package com.tim.pollution.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tim.pollution.R;
import com.tim.pollution.activity.AboutUsActivity;
import com.tim.pollution.activity.QualityReleaseActivity;
import com.tim.pollution.activity.RelesExplainActvity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lenovo on 2018/4/23.
 */

public class AboutFragment extends Fragment {
    @BindView(R.id.explain_quality_tv)
    TextView tvQuality;
    @BindView(R.id.explain_release_tv)
    TextView tvRelease;
    @BindView(R.id.about_us_tv)
    TextView tvAboutUs;

    public static AboutFragment newInstance(String param1) {
        AboutFragment fragment = new AboutFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_setting,null);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick({R.id.explain_quality_tv, R.id.explain_release_tv, R.id.about_us_tv})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.explain_quality_tv:
                startActivity(new Intent(getActivity(),QualityReleaseActivity.class));
                break;
            case R.id.explain_release_tv:
                startActivity(new Intent(getActivity(),RelesExplainActvity.class));
                break;
            case R.id.about_us_tv:
                startActivity(new Intent(getActivity(),AboutUsActivity.class));
                break;
        }
    }
}
