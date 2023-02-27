package com.zhangchuang.demo.ui.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.zhangchuang.demo.MainActivity;
import com.zhangchuang.demo.R;
import com.zhangchuang.demo.entity.User;
import com.zhangchuang.demo.network.RetrofitHelper;
import com.zhangchuang.demo.network.api.UserService;
import com.zhangchuang.demo.service.impl.ApplicationServiceImpl;
import com.zhangchuang.demo.utils.NetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;

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
                    Toast.makeText(getApplicationContext(), "请输入账号!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pwsText.length() == 0) {
                    Toast.makeText(getApplicationContext(), "请输入密码!", Toast.LENGTH_SHORT).show();
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
     *
     * @param context 上下文信息
     * @param json    需要传递的JSON信息
     */
    public boolean saveToken(Context context, String json) {
        JSONObject jsonObject = null;
        int code = 0;
        String meg = null;
        try {
            jsonObject = new JSONObject(json);
            code = jsonObject.getInt("code");
            meg = jsonObject.getString("msg");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        if (code != 200) {
            Toast.makeText(getApplicationContext(), meg, Toast.LENGTH_SHORT).show();
            Log.e(MEDIA_PROJECTION_SERVICE, "JSON数据解析失败！");
            return false;
        }
        String token = jsonObject.optString("token");
        SharedPreferences application = context.getSharedPreferences("Application", MODE_PRIVATE);
        SharedPreferences.Editor edit = application.edit();
        edit.putString("token", token);
        return true;
    }


    /**
     * 读取网络配置信息并初始化
     */
    public void OnRetrofit() {
        ApplicationServiceImpl applicationService1 = new ApplicationServiceImpl(getApplicationContext());
        String networkInfo = applicationService1.readNetworkInfo();
        Log.v(MEDIA_PROJECTION_SERVICE, "网络配置信息--->" + networkInfo);
        mRetrofit = new Retrofit.Builder()
                .baseUrl(networkInfo)
                .build();
    }

    /**
     * 执行网络登录
     *
     * @param userInfo 用户JSON
     */
    public void networkLogin(String userInfo) {
        OnRetrofit();
        UserService userService = mRetrofit.create(UserService.class);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), userInfo);
        Call<ResponseBody> login = userService.login(requestBody);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String json = response.body().string();
                    Log.v(MEDIA_PROJECTION_SERVICE, "POST回调成功!" + json);
                    boolean isFlag = saveToken(getApplicationContext(), json);
                    Intent intent = new Intent();
                    if (isFlag) {
                        intent.setClass(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e(MEDIA_PROJECTION_SERVICE, "POST回调失败！\n错误信息--->" + throwable);
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }
}