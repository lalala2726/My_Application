package com.zhangchuang.demo.network.api;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * 用户相关接口
 *
 * @Author:Zhangchuang
 * @Date: 2023/2/27 8:52
 */
public interface UserService {

    @POST("/prod-api/api/login")
    retrofit2.Call<ResponseBody> login(@Body RequestBody body);


}
