package com.zhangchuang.demo.ui.notifications;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class WalletActivity extends AppCompatActivity {

    private TextView wallet;

    private ApplicationServiceImpl applicationService;

    private Retrofit mRetrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        initView();
        getBalanceByNetwork();
    }

    /**
     * 初始化View
     */
    public void initView() {
        getSupportActionBar().hide();
        applicationService = new ApplicationServiceImpl(getApplicationContext());
        wallet = findViewById(R.id.wallet_info);
        findViewById(R.id.wallet_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WalletOrderActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 初始化Retrofit配置
     */
    public void initRetrofit() {
        String networkInfo = applicationService.readNetworkInfo();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(networkInfo)
                .build();
    }

    public void getBalanceByNetwork() {
        initRetrofit();
        String token = applicationService.readToken();
        UserService userService = mRetrofit.create(UserService.class);
        Call<ResponseBody> userInfo = userService.getUserInfo(token);
        userInfo.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String string = response.body().string();
                    JSONObject jsonObject = new JSONObject(string);
                    if (jsonObject.getInt("code") == 200) {
                        String user = jsonObject.getString("user");
                        LoadInfo(user);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.w("GET回调失败", throwable.toString());
            }
        });
    }

    public void LoadInfo(String json) {
        Gson gson = new Gson();
        User user = gson.fromJson(json, User.class);
        Log.d("用户信息", user.toString());
        wallet.setText("￥" + user.getBalance());
    }
}