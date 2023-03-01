package com.zhangchuang.demo.ui.notifications;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zhangchuang.demo.R;
import com.zhangchuang.demo.entity.UpdatePassword;
import com.zhangchuang.demo.network.api.UserService;
import com.zhangchuang.demo.service.impl.ApplicationServiceImpl;

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

public class UpdatePasswordActivity extends AppCompatActivity {

    private Retrofit mRetrofit;

    private ApplicationServiceImpl applicationService;

    private static final String LOCAL_SERVER_ADDRESS = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        init();
    }

    /**
     * 初始化
     */
    public void init() {
        applicationService = new ApplicationServiceImpl(getApplicationContext());
        Gson gson = new Gson();
        String jsonInfo = gson.toJson(UpdatePassword.class);
        updatePasswordByNetwork(jsonInfo);
    }

    /**
     * 初始化Retrofit信息
     */
    public void initRetrofit() {
        //读取网络配置信息
        String networkInfo = applicationService.readNetworkInfo();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(networkInfo)
                .build();
    }


    /**
     * 通过网络修改密码
     */
    public void updatePasswordByNetwork(String json) {
        initRetrofit();
        String token = applicationService.readToken();
        UserService userService = mRetrofit.create(UserService.class);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);
        Call<ResponseBody> responseBodyCall = userService.updatePassword(token, requestBody);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String string = response.body().string();
                    Log.i("SUCCESS", "GET回调成功！返回参数--->" + string);
                    JsonInfo(string);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e("ERROR", "POST回调失败！错误信息-->" + throwable);
            }
        });

    }


    public void JsonInfo(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            int code = jsonObject.getInt("code");
            if (code == 200) {
                String msg = jsonObject.getString("msg");
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
                return;
            }
            if (code != 200) {
                String msg = jsonObject.getString("msg");
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
                return;
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }

}