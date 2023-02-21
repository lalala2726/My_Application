package com.zhangchuang.demo.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.zhangchuang.demo.MainActivity;
import com.zhangchuang.demo.R;
import com.zhangchuang.demo.network.UserNetwork;
import com.zhangchuang.demo.ui.function.StopCarActivity;
import com.zhangchuang.demo.utils.NetworkUtils;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    public void init(){
        EditText user = findViewById(R.id.editTextTextPersonName);
        EditText pws = findViewById(R.id.editTextTextPassword);
        String username = user.getText().toString();
        String password = pws.getText().toString();
        NetworkUtils networkUtils = new NetworkUtils();
        findViewById(R.id.button7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}