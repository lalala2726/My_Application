package com.zhangchuang.demo.utils;


import android.os.Message;

import androidx.annotation.NonNull;


import java.io.IOException;
import java.util.Map;
import java.util.logging.Handler;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetworkUtils {


    /**
     * 异步GET请求
     * @param url
     * @param json
     * @return
     */
    public String asyncGet(String url, String... json) {
        System.out.println("GET请求执行一次！");
        OkHttpClient okHttpClient = new OkHttpClient();
        MediaType parse = MediaType.parse("application/json");
        RequestBody requestBody = RequestBody.create(parse, json[0]);
        final Request request = new Request.Builder()
                .url(url)
                .method("GET", requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                System.out.println("GET请求执行失败！");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                System.out.println("GET请求执行成功！");
            }
        });
        return null;
    }


    /**
     * 异步POST请求
     * @param url
     * @param json
     * @return
     */
    public String asyncPOST(String url, String json) {
        Message message = new Message();
        System.out.println("POST请求执行一次");
        new Thread(() -> {
            OkHttpClient client = new OkHttpClient();
            MediaType parse = MediaType.parse("application/json");
            RequestBody requestBody = RequestBody.create(parse, json);
            Request builder = new Request.Builder()
                    .url(url)
                    .method("POST", requestBody)
                    .build();
            client.newCall(builder).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    System.out.println("POST请求执行失败！");
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    System.out.println("POST请求执行成功！");
                    message.obj = response.body().string();
                }
            });
        }).start();
        return null;
    }


}
