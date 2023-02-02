package com.zhangchuang.demo.ui.notifications;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.zhangchuang.demo.R;

public class UserinfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        getSupportActionBar().hide();
    }
}