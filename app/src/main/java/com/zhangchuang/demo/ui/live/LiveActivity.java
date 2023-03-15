package com.zhangchuang.demo.ui.live;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.zhangchuang.demo.R;
import com.zhangchuang.demo.network.api.LiveService;
import com.zhangchuang.demo.service.impl.ApplicationServiceImpl;
import com.zhangchuang.demo.ui.function.NewsInfoActivity;
import com.zhangchuang.demo.utils.MyLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashSet;
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

    //新闻资讯相关接口

    private ListView listView;

    private List newsTitle;
    private List newsImages;
    private List newsPublishDate;

    private List newsContent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_live, container, false);

        initView(view);
        getAdsList();
        getNewsAndInfoList();
        return view;
    }


    /**
     * 初始化视图
     */
    public void initView(View v) {
        newsTitle = new ArrayList();
        newsImages = new ArrayList();
        newsPublishDate = new ArrayList();
        newsContent = new ArrayList();
        mBanner = v.findViewById(R.id.view_banner);
        images = new ArrayList();
        applicationService = new ApplicationServiceImpl(getContext());
        listView = v.findViewById(R.id.live_listview);
        //新闻资讯跳转至详情
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), NewsInfoActivity.class);
                intent.putExtra("newsContent", newsContent.get((int) position).toString());
                intent.putExtra("newsTitle", newsTitle.get((int) position).toString());
                startActivity(intent);
            }
        });
        v.findViewById(R.id.live_feedback_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FeedBackActivity.class);
                startActivity(intent);
            }
        });
        v.findViewById(R.id.live_weather).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), WeatherActivity.class);
                startActivity(intent);
            }
        });
        //跳转至话费充值
        v.findViewById(R.id.live_telephone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), TelephoneFeeActivity.class);
                startActivity(intent);
            }
        });
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
                Log.e("ERROR", "错误信息--->" + throwable);
            }
        });
    }

    /**
     * 湖泊去新闻资讯列表
     */
    public void getNewsAndInfoList() {
        initRetrofit();
        LiveService liveService = mRetrofit.create(LiveService.class);
        Call<ResponseBody> newsAndInfo = liveService.getNewsAndInfo();
        newsAndInfo.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String string = response.body().string();
                    Log.e("SUCCESS", "返回体--->" + string);
                    JSONObject jsonObject = new JSONObject(string);
                    if (jsonObject.getInt("code") == 200) {
                        String rows = jsonObject.getString("rows");
                        parseJsonToArrayList(rows);
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
                Log.e("ERROR", "错误信息--->" + throwable);
            }
        });
    }

    /**
     * 加载新闻列表
     */
    public void newsListView() {
        NewsAdapter newsAdapter = new NewsAdapter();
        listView.setAdapter(newsAdapter);
    }

    /**
     * 轮播图
     * <p>
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

    public void parseJsonToArrayList(String json) {
        String networkInfo = applicationService.readNetworkInfo();
        ArrayList list = new ArrayList();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                list.add(jsonArray.getJSONObject(i));
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        //提取数据
        for (int i = 0; i < list.size(); i++) {
            JSONObject jsonObject = (JSONObject) list.get(i);
            try {
                newsTitle.add(jsonObject.getString("title"));
                newsImages.add(networkInfo + jsonObject.getString("cover"));
                newsPublishDate.add(jsonObject.getString("publishDate"));
                newsContent.add(jsonObject.getString("content"));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        //打印数据
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            newsTitle.forEach(System.out::println);
            newsImages.forEach(System.out::println);
            newsPublishDate.forEach(System.out::println);
            newsContent.forEach(System.out::println);
        }
        //加载新闻列表
        newsListView();

    }


    private class NewsAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return newsTitle.size();
        }

        @Override
        public Object getItem(int position) {
            return newsTitle.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(getContext(), R.layout.news, null);

            TextView title = view.findViewById(R.id.news_title);
            title.setText(newsTitle.get(position).toString());

            TextView info = view.findViewById(R.id.news_info);
            info.setText(newsPublishDate.get(position).toString());

            ImageView imageView = view.findViewById(R.id.news_images);
            //设置图片
            Glide.with(getContext()).load(newsImages.get(position).toString()).into(imageView);
            return view;
        }
    }
}