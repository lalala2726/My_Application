package com.zhangchuang.demo.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.zhangchuang.demo.MainActivity;
import com.zhangchuang.demo.R;
import com.zhangchuang.demo.entity.User;
import com.zhangchuang.demo.network.RetrofitHelper;
import com.zhangchuang.demo.network.UserNetwork;
import com.zhangchuang.demo.ui.function.StopCarActivity;
import com.zhangchuang.demo.utils.NetworkUtils;

public class LoginActivity extends AppCompatActivity {


    private RetrofitHelper retrofitHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    public void init(){
        NetworkUtils networkUtils = new NetworkUtils();
        findViewById(R.id.button7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TELEPHONY_SERVICE,"显示的信息");
                //获取用户账户信息
                EditText username = findViewById(R.id.editTextTextPersonName);
                EditText password = findViewById(R.id.editTextTextPersonName);
                String userText = username.getText().toString();
                String pwsText = password.getText().toString();
                Gson gson = new Gson();
                User user = new User(userText,pwsText);
                String userInfoJson = gson.toJson(user);
                System.out.println("输出的信息--->" + userInfoJson);
            }
        });
    }
}