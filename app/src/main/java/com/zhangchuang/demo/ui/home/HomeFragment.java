package com.zhangchuang.demo.ui.home;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.zhangchuang.demo.R;
import com.zhangchuang.demo.network.api.SystemService;
import com.zhangchuang.demo.service.impl.ApplicationServiceImpl;
import com.zhangchuang.demo.ui.function.NewsInfoActivity;
import com.zhangchuang.demo.ui.function.StopCarActivity;
import com.zhangchuang.demo.ui.live.LiveActivity;
import com.zhangchuang.demo.utils.MyLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
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

    //首页轮播图资源
    private List images;

    //新闻相关的资源
    private List newsTitles;
    private List newsImages;
    private List newsPublishDate;

    private List newsContent;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        banner = view.findViewById(R.id.vv_banner);
        initView(view);
        getAdImageByNetwork();
        newsInfo();
        return view;
    }

    /**
     * 初始化
     *
     * @param v
     */
    public void initView(View v) {
        //跳转到停车界面
        v.findViewById(R.id.parking).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), StopCarActivity.class);
                startActivity(intent);
            }
        });
        v.findViewById(R.id.button_live).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到LiveActivity
            }
        });
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
     * 首页轮播图相关
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


    /**
     * 新闻相关模块
     */
    public void newsInfo() {
        getNewsInfoByNetwork();
        newsPublishDate = new ArrayList();
        newsImages = new ArrayList();
        newsTitles = new ArrayList();
        newsContent = new ArrayList();

    }

    /**
     * 新闻列表
     */
    public void initListView() {
        ListView listView = null;
        listView = getView().findViewById(R.id.news_listview);
        NewsAdapter newsAdapter = new NewsAdapter();
        listView.setAdapter(newsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), NewsInfoActivity.class);
                intent.putExtra("newsContent", newsContent.get((int) position).toString());
                intent.putExtra("newsTitle", newsTitles.get((int) position).toString());
                startActivity(intent);
            }
        });
    }

    /**
     * 通过网络获取新闻信息
     */
    public void getNewsInfoByNetwork() {
        initRetrofit();
        SystemService systemService = mRetrofit.create(SystemService.class);
        Call<ResponseBody> newsInfo = systemService.getNewsInfo();
        newsInfo.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String string = response.body().string();
                    JSONObject jsonObject = new JSONObject(string);
                    int code = jsonObject.getInt("code");
                    if (code == 200) {
                        String rows = jsonObject.getString("rows");
                        newsParseJsonToArrayList(rows);
                    }
                } catch (IOException e) {
                    Log.e("Exception", "IOException--->" + e);
                } catch (JSONException e) {
                    Log.e("Exception", "JSONException--->" + e);
                } catch (NullPointerException e) {
                    Log.e("NULL", "对象为空！-->" + e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e("ERROR", "获取失败！错误信息-->" + throwable);
            }
        });
    }


    /**
     * 将JSON数据转化成ArrayList集合
     *
     * @param json
     */
    public void newsParseJsonToArrayList(String json) {
        String networkInfo = applicationService.readNetworkInfo();
        //将JSON数据转ArrayList集合
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

        //数据清理
        for (int i = 0; i < list.size(); i++) {
            JSONObject jsonObject = (JSONObject) list.get(i);
            try {
                newsTitles.add(jsonObject.getString("title"));
                newsImages.add(networkInfo + jsonObject.getString("cover"));
                newsPublishDate.add(jsonObject.getString("publishDate"));
                newsContent.add(jsonObject.getString("content"));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            list.forEach(System.out::println);
        }

        //加载列表
        try {
            initListView();
        } catch (NullPointerException e) {
            Log.e(getContext().toString(), "空对象!");
        }

    }

    public class NewsAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return newsTitles.size();
        }

        @Override
        public Object getItem(int position) {
            return newsTitles.get(position);
        }

        @Override
        public long getItemId(int position) {
            Log.e("SUCCESS", "获取到的ID-->" + position);
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(getContext(), R.layout.news, null);

            TextView title = view.findViewById(R.id.news_title);
            title.setText(newsTitles.get(position).toString());

            TextView info = view.findViewById(R.id.news_info);
            info.setText(newsPublishDate.get(position).toString());

            ImageView imageView = view.findViewById(R.id.news_images);
            //设置图片
            Glide.with(getContext()).load(newsImages.get(position).toString()).into(imageView);
            return view;
        }
    }


}