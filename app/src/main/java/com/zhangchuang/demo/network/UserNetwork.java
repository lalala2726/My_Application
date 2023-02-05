package com.zhangchuang.demo.network;

import com.zhangchuang.demo.utils.NetworkUtils;

import java.net.URI;

public class UserNetwork {
    private final String URL = "http://124.93.196.45:10001/prod-api/api/metro/notice/1";

    private NetworkUtils networkUtils;

    public void testNetwork(){
        String okhttp = networkUtils.getOkhttp(URL);
        System.out.println(okhttp);
    }
}
