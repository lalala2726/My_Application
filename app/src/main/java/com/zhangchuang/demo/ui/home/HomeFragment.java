package com.zhangchuang.demo.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.zhangchuang.demo.R;
import com.zhangchuang.demo.databinding.FragmentHomeBinding;
import com.zhangchuang.demo.ui.start.GuideActivity;
import com.zhangchuang.demo.utils.MyLoader;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private Banner banner;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Intent intent = new Intent();
        Button test = view.findViewById(R.id.button);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.setClass(getActivity(), GuideActivity.class);
                startActivity(intent);
            }
        });
        banner = view.findViewById(R.id.vv_banner);
        initView();
        return view;
    }

    private void initView() {
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