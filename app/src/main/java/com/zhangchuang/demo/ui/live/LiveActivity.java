package com.zhangchuang.demo.ui.live;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.zhangchuang.demo.R;
import com.zhangchuang.demo.network.api.LiveService;
import com.zhangchuang.demo.service.impl.ApplicationServiceImpl;
import com.zhangchuang.demo.utils.MyLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LiveActivity extends Fragment {

    private Banner mBanner;

    private List images;

    private Retrofit mRetrofit;

    private ApplicationServiceImpl applicationService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_live, container, false);

        initView(view);
        getAdsList();
        return view;
    }


    /**
     * 初始化视图
     */
    public void initView(View v) {
        mBanner = v.findViewById(R.id.view_banner);
        images = new ArrayList();
        applicationService = new ApplicationServiceImpl(getContext());
    }

    public void setBanner() {
        mBanner.setImages(images);
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        mBanner.setImageLoader(new MyLoader());
        mBanner.start();
    }


    /**
     * 初始化网络信息和Retrofit框架
     */
    private void initRetrofit() {
        String networkInfo = applicationService.readNetworkInfo();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(networkInfo)
                .build();
    }

    /**
     * 通过网络获取广告轮播图
     */
    public void getAdsList() {
        initRetrofit();
        LiveService liveService = mRetrofit.create(LiveService.class);
        Call<ResponseBody> ads = liveService.getAds();
        ads.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String string = response.body().string();
                    JSONObject jsonObject = new JSONObject(string);
                    if (jsonObject.getInt("code") == 200) {
                        String rows = jsonObject.getString("rows");
                        JsonToArrayList(rows);
                    } else {
                        Log.e("ERROR", "请求失败！查看--->" + string);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {

            }
        });
    }

    /**
     * 整理JSON数据将所需要的信息进行整理
     *
     * @param json
     */
    public void JsonToArrayList(String json) {
        String networkInfo = applicationService.readNetworkInfo();
        //将JSON数据转Array集合
        ArrayList list = new ArrayList();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                list.add(jsonObject);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        //整理数据
        for (int i = 0; i < list.size(); i++) {
            JSONObject jsonObject = (JSONObject) list.get(i);
            try {
                images.add(networkInfo + jsonObject.getString("advImg"));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            images.forEach(System.out::println);
        }
        //设置轮播图
       setBanner();
    }
}