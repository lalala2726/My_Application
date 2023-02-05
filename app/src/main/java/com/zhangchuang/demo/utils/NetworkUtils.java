package com.zhangchuang.demo.utils;


import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class NetworkUtils {


    private String data = null;

    public String getOkhttp(String url) {
        new Thread(() -> {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            try {
                client.newCall(request).execute();
                data = request.body().toString();
            } catch (IOException e) {
                System.out.println("访问网络失败:--->" + e);
            }

        }).start();
        return data;
    }
}
