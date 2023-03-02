package com.zhangchuang.demo.network.api;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

/**
 * 用户相关接口
 *
 * @Author:Zhangchuang
 * @Date: 2023/2/27 8:52
 */
public interface UserService {


    /**
     * 用户登录
     *
     * @param body
     * @return
     */
    @POST("/prod-api/api/login")
    Call<ResponseBody> login(@Body RequestBody body);


    /**
     * 获取用户信息
     *
     * @param token
     * @return
     */
    @GET("/prod-api/api/common/user/getInfo")
    Call<ResponseBody> getUserInfo(@Header("Authorization") String token);
//    @GET("/getUserInfo")
//    Call<ResponseBody> getUserInfo(@Query("id") Integer id);


    /**
     * 修改密码
     *
     * @param token
     * @param body
     * @return
     */
    @PUT("/prod-api/api/common/user/resetPwd")
    Call<ResponseBody> updatePassword(@Header("Authorization") String token, @Body RequestBody body);
}
