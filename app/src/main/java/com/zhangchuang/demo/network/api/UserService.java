package com.zhangchuang.demo.network.api;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * 用户相关接口
 *
 * @Author:Zhangchuang
 * @Date: 2023/2/27 8:52
 */
public interface UserService {


    /**
     * 用户登录
     * @param body
     * @return
     */
    @POST("/prod-api/api/login")
    retrofit2.Call<ResponseBody> login(@Body RequestBody body);


    /**
     * 获取用户信息
     * @param token
     * @return
     */
    @GET("/prod-api/api/common/user/getInfo")
    Call getUserInfo(@Header("Authorization") String token);

}
