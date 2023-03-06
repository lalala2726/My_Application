package com.zhangchuang.demo.ui.notifications;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zhangchuang.demo.R;
import com.zhangchuang.demo.entity.User;
import com.zhangchuang.demo.network.api.UserService;
import com.zhangchuang.demo.service.impl.ApplicationServiceImpl;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserinfoActivity extends AppCompatActivity {
    private ApplicationServiceImpl applicationService;

    private Retrofit mRetrofit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        getSupportActionBar().hide();
        initInfo();
        getUserInfoBuNetWork();
    }



    /**
     * 预加载信息
     */
    public void initInfo() {
        applicationService = new ApplicationServiceImpl(getApplicationContext());
    }

    /**
     * 预加载Retrofit
     */
    public void initRetrofit() {
        String readNetworkInfo = applicationService.readNetworkInfo();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(readNetworkInfo)
                .build();
    }

    public void getUserInfoBuNetWork() {
        initRetrofit();
        Log.e("Msg", "正在加载网络信息");
        String token = applicationService.readToken();
        UserService userService = mRetrofit.create(UserService.class);
        userService.getUserInfo(token);
        Call<ResponseBody> userInfo = userService.getUserInfo(token);
        userInfo.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String json = response.body().string();

                    Log.e("SUCCESS", "返回的原始信息:" + json);
                    JsonToUser(json);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {

            }
        });
    }

    /**
     * 将JSON数据转Java实体类
     *
     * @param json 需要传输的JSON数据
     */
    public void JsonToUser(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            int code = jsonObject.getInt("code");
            if (code == 200) {
                String user = jsonObject.getString("user");
                Gson gson = new Gson();
                User user1 = gson.fromJson(user, User.class);
                System.out.println("二次清洗信息:" + user1);
                //显示用户信息
                displayUserInfo(user1);
            } else {
                Log.e("ERROR", "用户信息获取失败！");
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 展示用户信息
     */
    public void displayUserInfo(User user){

        TextView userId = findViewById(R.id.info_userid);
        userId.setText(user.getUserId());
        TextView userName = findViewById(R.id.info_username);
        userName.setText(user.getUserName());
        TextView phone = findViewById(R.id.info_phone);
        phone.setText(user.getPhonenumber());
        TextView email = findViewById(R.id.info_email);
        email.setText(user.getEmail());
        TextView sex = findViewById(R.id.info_sex);
        if (0 == user.getSex()){
            sex.setText("男");
        }else {
            sex.setText("女");
        }


    }
}