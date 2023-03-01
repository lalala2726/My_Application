package com.zhangchuang.demo.ui.notifications;

import androidx.appcompat.app.AppCompatActivity;

import android.icu.text.CaseMap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

    private EditText oldPassword;
    private EditText newPassword;
    private EditText updatePassword;

    private UpdatePassword entityPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        init();
       findViewById(R.id.update_password).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String old = oldPassword.getText().toString();
               String newPws = newPassword.getText().toString();
               String Password = updatePassword.getText().toString();
               if (old.length() == 0){
                   Toast.makeText(getApplicationContext(),"请输入旧密码",Toast.LENGTH_SHORT);
                   return;
               }
               if (newPws.length() == 0){
                   Toast.makeText(getApplicationContext(),"请输入新密码",Toast.LENGTH_SHORT);
                   return;
               }
               if (Password.length() == 0){
                   Toast.makeText(getApplicationContext(),"请输入确定密码",Toast.LENGTH_SHORT);
                   return;
               }
               if (Password.equals(updatePassword.getText().toString())){
                   Log.e("Warning!","两次密码不一致！");
                   Toast.makeText(getApplicationContext(),"两次密码不一致！",Toast.LENGTH_SHORT);
                   return;
               }
               entityPassword.setNewPassword(newPws);
               entityPassword.setOldPassword(old);
               Gson gson = new Gson();

               String json = gson.toJson(UpdatePassword.class);
               updatePasswordByNetwork(json);
           }
       });
    }

    /**
     * 初始化
     */
    public void init() {
        entityPassword = new UpdatePassword();
        oldPassword = findViewById(R.id.old_password_view);
        newPassword =  findViewById(R.id.new_password_view);
        updatePassword = findViewById(R.id.determine_password);
        setTitle("修改你的密码");
        applicationService = new ApplicationServiceImpl(getApplicationContext());
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