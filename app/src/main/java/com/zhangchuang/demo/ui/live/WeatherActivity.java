package com.zhangchuang.demo.ui.live;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zhangchuang.demo.R;
import com.zhangchuang.demo.network.api.LiveService;
import com.zhangchuang.demo.service.impl.ApplicationServiceImpl;

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

public class WeatherActivity extends AppCompatActivity {

    private ApplicationServiceImpl applicationService;

    private Retrofit mRetrofit;

    private ListView weatherListView;
    //天气数据信息
    private List maxTemperature;
    private List minTemperature;
    private List day;
    private List weather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        initView();
        getWeatherInfoByNetwork();
    }


    /**
     * 初始化View
     */
    public void initView() {
        //隐藏标题栏
        getSupportActionBar().hide();
        maxTemperature = new ArrayList();
        minTemperature = new ArrayList();
        day = new ArrayList();
        weather = new ArrayList();
        weather = new ArrayList();
        applicationService = new ApplicationServiceImpl(getApplicationContext());
        weatherListView = findViewById(R.id.weather_list);
    }


    /**
     * 初始化Retrofit框架
     */
    public void initRetrofit() {
        String networkInfo = applicationService.readNetworkInfo();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(networkInfo)
                .build();
    }

    /**
     * 通过网络获取天气信息
     */
    public void getWeatherInfoByNetwork() {
        initRetrofit();
        LiveService liveService = mRetrofit.create(LiveService.class);
        Call<ResponseBody> weatherInfo = liveService.getWeatherInfo();
        weatherInfo.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String string = response.body().string();
                    JSONObject jsonObject = new JSONObject(string);
                    if (jsonObject.getInt("code") == 200) {
                        String data = jsonObject.getString("data");
                        JSONObject jsonObject1 = new JSONObject(data);
                        String weatherList = jsonObject1.getString("weatherList");
                        finishData(weatherList);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.w("GET回调失败", throwable.toString());
            }
        });
    }

    /**
     * 整理JSON数据
     *
     * @param json
     */
    public void finishData(String json) {
        //将所需的数据转换成存放在集合里面
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

        //将集合的数据取出需要使用的
        for (int i = 0; i < list.size(); i++) {
            JSONObject jsonObject = (JSONObject) list.get(i);
            try {
                day.add(jsonObject.getString("day"));
                maxTemperature.add(jsonObject.getString("minTemperature"));
                minTemperature.add(jsonObject.getString("temperature"));
                weather.add(jsonObject.getString("weather"));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            list.forEach(System.out::println);
        }

        //遍历数据
        for (int i = 0; i < day.size(); i++) {
            System.out.println(day.get(i));
            System.out.println(maxTemperature.get(i));
            System.out.println(minTemperature.get(i));
            System.out.println(weather.get(i));
        }

        //展示数据
        weatherListView.setAdapter(new WeatherAdapter());
    }

    /**
     * 自定义Adapter
     */
    private class WeatherAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return day.size();
        }

        @Override
        public Object getItem(int position) {
            return day.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(getApplicationContext(), R.layout.weather_layout, null);
            TextView dayText = view.findViewById(R.id.weather_day);
            dayText.setText(day.get(position).toString());
            TextView max = view.findViewById(R.id.weather_max);
            max.setText(maxTemperature.get(position).toString());
            TextView min = view.findViewById(R.id.weather_min);
            min.setText(minTemperature.get(position).toString());

            ImageView images = view.findViewById(R.id.weather_images);
            switch (weather.get(position).toString()) {
                case "晴":
                    images.setImageResource(R.drawable.weather_sunny);
                    break;
                case "多云":
                    images.setImageResource(R.drawable.weather_cloudy);
                    break;
            }

            return view;
        }
    }

}