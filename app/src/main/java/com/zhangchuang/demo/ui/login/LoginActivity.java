package com.zhangchuang.demo.ui.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.zhangchuang.demo.R;
import com.zhangchuang.demo.entity.User;
import com.zhangchuang.demo.network.RetrofitHelper;
import com.zhangchuang.demo.network.api.UserService;
import com.zhangchuang.demo.service.impl.ApplicationServiceImpl;
import com.zhangchuang.demo.utils.NetworkUtils;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {


    private RetrofitHelper retrofitHelper;
    private Retrofit mRetrofit;
    private ApplicationServiceImpl applicationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    public void init() {
        NetworkUtils networkUtils = new NetworkUtils();
        findViewById(R.id.button7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TELEPHONY_SERVICE, "显示的信息");
                //获取用户账户信息
                EditText username = findViewById(R.id.editTextTextPersonName);
                EditText password = findViewById(R.id.editTextTextPassword);
                String userText = username.getText().toString();
                String pwsText = password.getText().toString();
                if (username.length() == 0) {
                    Toast.makeText(getApplicationContext(),"请输入账号!",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pwsText.length() == 0) {
                    Toast.makeText(getApplicationContext(),"请输入密码!",Toast.LENGTH_SHORT).show();
                    return;
                }
                Gson gson = new Gson();
                User user = new User(userText, pwsText);
                String userInfoJson = gson.toJson(user);
                System.out.println("输出的信息--->" + userInfoJson);
                networkLogin(userInfoJson);
            }
        });
    }


    /**
     * 保存Token信息
     * @param context 上下文信息
     * @param token 需要保存的Token信息
     */
    public void saveToken(Context context,String token){
        SharedPreferences application = context.getSharedPreferences("Application", MODE_PRIVATE);
        SharedPreferences.Editor edit = application.edit();
        edit.putString("token",token);
    }


    /**
     * 读取网络配置信息并初始化
     */
    public void OnRetrofit(){
        ApplicationServiceImpl applicationService1 = new ApplicationServiceImpl(getApplicationContext());
        String networkInfo = applicationService1.readNetworkInfo();
        Log.v(MEDIA_PROJECTION_SERVICE,"网络配置信息--->" + networkInfo);
        mRetrofit = new Retrofit.Builder()
                .baseUrl(networkInfo)
                .build();
    }

    /**
     * 执行网络登录
     * @param userInfo 用户JSON
     */
    public void networkLogin(String userInfo){
        OnRetrofit();
        UserService userService = mRetrofit.create(UserService.class);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),userInfo);
        Call<ResponseBody> login = userService.login(requestBody);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.v(MEDIA_PROJECTION_SERVICE,"POST回调成功!" + response.body().string());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e(MEDIA_PROJECTION_SERVICE,"POST回调失败！\n错误信息--->" + throwable);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }
}