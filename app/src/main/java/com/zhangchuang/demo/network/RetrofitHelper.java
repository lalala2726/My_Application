package com.zhangchuang.demo.network;

import com.zhangchuang.demo.network.api.UserInfoService;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 执行类
 *
 * @Author:Zhangchuang
 * @Date: 2023/2/21 14:24
 */
public class RetrofitHelper {

    private static OkHttpClient mokHttpClient;

    //本地环境测试
    private static final String BASE_URL = "http://localhost:8080/";

    /**
     * 测试网络请求
     */
    public void testGet(String baseUrl){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Call userInfo = retrofit.create(UserInfoService.class).getUserInfo();

    }
}
