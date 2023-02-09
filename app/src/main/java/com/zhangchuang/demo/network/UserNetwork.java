package com.zhangchuang.demo.network;

import com.zhangchuang.demo.entity.Data;
import com.zhangchuang.demo.utils.NetworkUtils;

import java.net.URI;

public class UserNetwork {
    private final String URL = "http://124.93.196.45:10001/prod-api/api/metro/notice/1";
    private Data data;
    private NetworkUtils networkUtils;

    public void testNetwork(){
        NetworkUtils utils = new NetworkUtils();
        String json = "{\n" +
                "    \"username\": \"lalala\",\n" +
                "    \"password\": \"123456\"\n" +
                "}";
//        String data = networkUtils1.postOkHttp("http://124.93.196.45:10001/prod-api/api/login", json);
//        System.out.println(data);
    }
}
