package com.zhangchuang.demo.network.api;

import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;

/**
 * 生活缴费相关接口
 *
 * @Author:Zhangchuang
 * @Date: 2023/3/13 15:49
 */
public interface LiveService {


    /**
     * 获取广告轮播图
     *
     * @return
     */
    @GET("/prod-api/api/living/rotation/list")
    Call<ResponseBody> getAds();


    /**
     * 获取新闻详情列表
     *
     * @return
     */
    @GET("/prod-api/api/living/press/press/list")
    Call<ResponseBody> getNewsAndInfo();

    /**
     * 生活专区意见反馈
     *
     * @param token
     * @return
     */
    @POST("/prod-api/api/living/feedback")
    Call<ResponseBody> submitLiveFeedBack(@Header("Authorization") String token, @Body RequestBody body);

    /**
     * 获取天气信息
     *
     * @return
     */
    @GET("/prod-api/api/living/weather")
    Call<ResponseBody> getWeatherInfo();


    @POST
    Call<ResponseBody> pay(@Url String url,@Header("Authorization") String token, @Body RequestBody body);

}
