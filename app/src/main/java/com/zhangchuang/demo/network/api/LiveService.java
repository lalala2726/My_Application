package com.zhangchuang.demo.network.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * 生活缴费相关接口
 *
 * @Author:Zhangchuang
 * @Date: 2023/3/13 15:49
 */
public interface LiveService {


    /**
     * 获取广告轮播图
     * @return
     */
    @GET("/prod-api/api/living/rotation/list")
    Call<ResponseBody> getAds();


}
