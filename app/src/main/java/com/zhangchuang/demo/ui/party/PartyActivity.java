package com.zhangchuang.demo.ui.party;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.zhangchuang.demo.R;
import com.zhangchuang.demo.utils.MyLoader;

import java.util.ArrayList;
import java.util.List;

public class PartyActivity extends Fragment {

    private Banner mBanner;

    private List<Integer> image;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_party, container, false);

        initView(view);
        setBanner();
        return view;
    }


    /**
     * 初始化视图
     */
    public void initView(View v) {

        mBanner = v.findViewById(R.id.view_banner);
        image = new ArrayList();
        image.add(R.drawable.p1);
        image.add(R.drawable.p2);
        image.add(R.drawable.p3);
    }

    public void setBanner() {
        mBanner.setImages(image);
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        mBanner.setImageLoader(new MyLoader());
        mBanner.start();
    }
}