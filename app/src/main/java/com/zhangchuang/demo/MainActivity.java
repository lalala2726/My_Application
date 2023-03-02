package com.zhangchuang.demo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.zhangchuang.demo.databinding.ActivityMainBinding;
import com.zhangchuang.demo.entity.User;
import com.zhangchuang.demo.network.api.UserService;
import com.zhangchuang.demo.service.impl.ApplicationServiceImpl;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Intent intent;
    private Retrofit mRetrofit;
    private ApplicationServiceImpl applicationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
        //隐藏标题栏
        getSupportActionBar().hide();
        initInfo();
        //不影响主线程加载信息
        new Thread(() -> {
            getUserInfoBuNetWork();
        }).start();
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

                    Log.e("SUCCESS", "返回的信息" + json);

                    Gson gson = new Gson();
                    gson.toJson(json);
                    User user = new User();
                    System.out.println("获取的信息-->" + user.getEmail());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {

            }
        });
       /* Call<Data<User>> userInfo = userService.getUserInfo(token);
        userInfo.enqueue(new Callback<Data<User>>() {
            @Override
            public void onResponse(Call<Data<User>> call, Response<Data<User>> response) {

                Data<User> body = response.body();
                if (body == null) return;
                User user = body.getData();
                if (user == null) return;
                Log.e("SUCCESS", "返回的信息-->" + "code:" + body.getCode() + "msg" + body.getMsg() + "Data" + user);
            }

            @Override
            public void onFailure(Call<Data<User>> call, Throwable throwable) {

            }
        });*/
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return false;
    }


}