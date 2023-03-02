package com.zhangchuang.demo.network.api;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * 系统相关接口
 *
 * @Author:Zhangchuang
 * @Date: 2023/3/1 15:37
 */
public interface SystemService {


    @POST("/prod-api/api/common/feedback")
    Call<ResponseBody> feedback(@Header("Authorization") String token, @Body RequestBody body);
}
