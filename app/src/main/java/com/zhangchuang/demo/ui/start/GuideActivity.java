package com.zhangchuang.demo.ui.start;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;

import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.zhangchuang.demo.R;
import com.zhangchuang.demo.utils.MyLoader;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends AppCompatActivity {

    private Banner banner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initView();
    }

    private void initView() {
        banner = findViewById(R.id.v_banner);
        //资源文件
        List images = new ArrayList<Integer>();
        images.add(R.drawable.phone1);
        images.add(R.drawable.phone2);
        images.add(R.drawable.phone3);
        images.add(R.drawable.phone4);
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        banner.setImageLoader(new MyLoader());
        banner.setImages(images);
        banner.setBannerAnimation(Transformer.DepthPage);
        banner.start();
    }
}