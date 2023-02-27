package com.zhangchuang.demo.network;

import static android.content.Context.TELEPHONY_SERVICE;

import android.content.Context;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.util.Log;

import com.zhangchuang.demo.network.api.UserInfoService;
import com.zhangchuang.demo.network.api.UserService;
import com.zhangchuang.demo.service.impl.ApplicationServiceImpl;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 执行类
 *
 * @Author:Zhangchuang
 * @Date: 2023/2/21 14:24
 */
public class RetrofitHelper {


}
