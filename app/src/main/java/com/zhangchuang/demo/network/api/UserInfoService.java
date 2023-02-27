package com.zhangchuang.demo.network.api;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * @Author:Zhangchuang
 * @Date: 2023/2/21 14:17
 */
public interface UserInfoService {

    /**
     * 获取用户信息
     * @return
     */
    @GET("/user/getUserInfo")
    Call getUserInfo();
}
