package com.zhangchuang.demo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.zhangchuang.demo.MainActivity;
import com.zhangchuang.demo.R;
import com.zhangchuang.demo.network.UserNetwork;
import com.zhangchuang.demo.service.impl.ApplicationServiceImpl;
import com.zhangchuang.demo.ui.start.EditConfigActivity;
import com.zhangchuang.demo.utils.ToastUtil;

public class TestActivity extends AppCompatActivity {


    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        intent = new Intent();
        init();
    }

    private void init() {
        ToastUtil toastUtil = new ToastUtil(getApplicationContext());
        ApplicationServiceImpl applicationService = new ApplicationServiceImpl(getApplicationContext());
        Intent intent = new Intent();
        findViewById(R.id.first_time).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(),"显示的文本信息",Toast.LENGTH_LONG).show();
//                System.out.println("上下文信息--->" + getApplicationContext());
                if (applicationService.FirstTime()) {
                    System.out.println("配置过信息");
                    intent.setClass(getApplicationContext(), TestActivity.class);
                    startActivity(intent);
                }else {
                    System.out.println("未配置过信息");
                    intent.setClass(getApplicationContext(), MainActivity.class);
                }
            }
        });

        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserNetwork userNetwork = new UserNetwork();
                userNetwork.testNetwork();
            }
        });
    }


}