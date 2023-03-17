package com.zhangchuang.demo.ui.live;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.zhangchuang.demo.R;
import com.zhangchuang.demo.service.impl.ApplicationServiceImpl;

import retrofit2.Retrofit;

public class LivePaymentActivity extends AppCompatActivity {

    private Retrofit mRetrofit;

    private ApplicationServiceImpl applicationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_payment);
        initView();
    }

    /**
     * 初始化View
     */
    public void initView() {
        //设置标题栏的颜色为白色
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.white));
        //电费缴纳
        findViewById(R.id.button_electricity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LivePaymentDetailsActivity.class);
                intent.putExtra("categoryId", "3");
                intent.putExtra("title", "电费");
                startActivity(intent);
            }
        });
        //水费缴纳
        findViewById(R.id.button_fuel_gas_charge).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LivePaymentDetailsActivity.class);
                intent.putExtra("categoryId", "2");
                intent.putExtra("title", "水费");
                startActivity(intent);
            }
        });
        //燃气费缴纳
        findViewById(R.id.button_water).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LivePaymentDetailsActivity.class);
                intent.putExtra("categoryId", "4");
                intent.putExtra("title", "燃气费");
                startActivity(intent);
            }
        });
    }

    /**
     * 初始化Retrofit框架
     */
    public void initRetrofit() {

    }
}