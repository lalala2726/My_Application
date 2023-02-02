package com.zhangchuang.demo.ui.start;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import androidx.appcompat.app.AppCompatActivity;

import com.zhangchuang.demo.MainActivity;
import com.zhangchuang.demo.R;
import com.zhangchuang.demo.service.impl.ApplicationServiceImpl;

public class StartActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        init();
        //隐藏标题栏
        getSupportActionBar().hide();
    }

    public void init() {
        Intent intent = new Intent();
        ApplicationServiceImpl applicationService = new ApplicationServiceImpl(getApplicationContext());
        new Thread(() -> {
            System.out.println("线程开始");
            try {
                Thread.sleep(10 * 100 * 3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (applicationService.FirstTime()){
                intent.setClass(getApplicationContext(), MainActivity.class);
            }else {
                intent.setClass(getApplicationContext(),GuideActivity.class);
            }
            startActivity(intent);
            System.out.println("线程结束!");

        }).start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }
}