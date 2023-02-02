package com.zhangchuang.demo.ui.start;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.zhangchuang.demo.R;

import java.util.ArrayList;

public class GuideActivity extends AppCompatActivity {

    private ArrayList<View> views = new ArrayList<>();

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        //隐藏标题栏
        getSupportActionBar().hide();
        //初始化View
        initView();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }


    public void initView() {
        viewPager = findViewById(R.id.viewPager);
        LayoutInflater from = getLayoutInflater().from(GuideActivity.this);
        View inflate1 = from.inflate(R.layout.activity_one, null);
        View inflate2 = from.inflate(R.layout.activity_two, null);
        View inflate3 = from.inflate(R.layout.activity_three, null);
        View inflate4 = from.inflate(R.layout.activity_four, null);
        views.add(inflate1);
        views.add(inflate2);
        views.add(inflate3);
        views.add(inflate4);
        inflate4.findViewById(R.id.button6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), EditConfigActivity.class);
                startActivity(intent);
            }
        });
        viewPager.setAdapter(new MyViewPager(views));
    }


}