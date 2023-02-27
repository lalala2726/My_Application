package com.zhangchuang.demo.ui.start;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.zhangchuang.demo.MainActivity;
import com.zhangchuang.demo.R;
import com.zhangchuang.demo.service.impl.ApplicationServiceImpl;
import com.zhangchuang.demo.ui.home.HomeFragment;
import com.zhangchuang.demo.ui.login.LoginActivity;
import com.zhangchuang.demo.utils.ToastUtil;

public class EditConfigActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editconfig);
        //隐藏标题栏
        getSupportActionBar().hide();
        init();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    public void init() {
        findViewById(R.id.button5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplicationServiceImpl applicationService = new ApplicationServiceImpl(getApplicationContext());
                ToastUtil toastUtil = new ToastUtil(getApplicationContext());
                EditText viewById = findViewById(R.id.editTextNumber);
                EditText viewById1 = findViewById(R.id.editTextNumber2);
                String ip = viewById.getText().toString();
                String port = viewById1.getText().toString();
                System.out.println("ip = " + ip);
                if ("".equals(ip) || "".equals(port)) {
                    toastUtil.displayMes("请填写完整！");
                    return;
                }
                if (applicationService.saveConfig(ip, port)) {

                    toastUtil.displayMes("保存成功！");
                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    toastUtil.displayMes("保存失败！请重启设备或重新安装软件！");
                }
            }
        });
    }
}