package com.zhangchuang.demo.ui.function;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.zhangchuang.demo.R;

public class OrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        initView();
    }

    /**
     * 初始化视图
     */
    public void initView(){
        setTitle("个人订单");
    }
}