package com.zhangchuang.demo.ui.home;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.zhangchuang.demo.R;
import com.zhangchuang.demo.network.api.SystemService;
import com.zhangchuang.demo.service.impl.ApplicationServiceImpl;
import com.zhangchuang.demo.ui.TestActivity;
import com.zhangchuang.demo.ui.function.StopCarActivity;
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

public class HomeFragment extends Fragment {

    private Banner banner;

    private Retrofit mRetrofit;

    private ApplicationServiceImpl applicationService;

    private List images;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        banner = view.findViewById(R.id.vv_banner);
        initView(view);
        getAdImageByNetwork();

        //跳转至停车场页面
        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), StopCarActivity.class);
                startActivity(intent);
            }
        });

        //跳转至功能测试
        view.findViewById(R.id.button12).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), TestActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    /**
     * 初始化
     *
     * @param v
     */
    public void initView(View v) {
        applicationService = new ApplicationServiceImpl(getContext());
        images = new ArrayList();
    }


    private void initBanner() {
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        banner.setImageLoader(new MyLoader());
        banner.setImages(images);
        banner.setBannerAnimation(Transformer.DepthPage);
        banner.start();
    }

    /**
     * 初始化Retrofit等信息
     */
    public void initRetrofit() {
        String networkInfo = applicationService.readNetworkInfo();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(networkInfo)
                .build();
    }


    /**
     * 通过网络获取广告图片信息
     */
    public void getAdImageByNetwork() {
        initRetrofit();
        SystemService systemService = mRetrofit.create(SystemService.class);
        Call<ResponseBody> adList = systemService.getADList();
        adList.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String string = response.body().string();
                    JSONObject jsonObject = new JSONObject(string);
                    int code = jsonObject.getInt("code");
                    if (code == 200) {
                        String rows = jsonObject.getString("rows");
                        parseJsonToArrayList(rows);
                    } else {
                        Toast.makeText(getContext(), "远程获取失败！", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    Log.e("Exception", "IO异常-->" + e);
                } catch (JSONException e) {
                    Log.e("Exception", "JSON异常-->" + e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e("ERROR", "错误信息--->" + throwable);
            }
        });
    }

    /**
     * 将JSON数据转arrayList
     */
    public void parseJsonToArrayList(String json) {

        String networkInfo = applicationService.readNetworkInfo();
        //将JSON数据转List集合
        List list = new ArrayList();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                list.add(jsonObject);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        //将里面JSON特定的字段提取出来
        for (int i = 0; i < list.size(); i++) {
            JSONObject jsonObject = (JSONObject) list.get(i);
            try {
                images.add(networkInfo + jsonObject.getString("advImg"));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                images.forEach(System.out::println);
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            list.forEach(System.out::println);
        }
        initBanner();
    }


}