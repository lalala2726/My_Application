package com.zhangchuang.demo.ui.function;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.zhangchuang.demo.R;
import com.zhangchuang.demo.network.api.SystemService;
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

public class StopCarActivity extends AppCompatActivity {


    private Retrofit mRetrofit;

    private ApplicationServiceImpl applicationService;

    //原始JSON数据
    private List<JSONObject> list;

    //整理后的JSON数据
    //停车场标题
    private List<String> parkName;
    //详情
    private List<String> address;
    //剩余车位
    private List<String> vacancy;
    //距离
    private List<String> distance;
    //价格
    private List<String> rates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stopcar);

        initView();
        new Thread(() -> {
            getInfoByNetwork();
        }).start();
    }

    /**
     * 预加载View等信息
     */
    public void initView() {
        //隐藏标题栏
        getSupportActionBar().hide();
        applicationService = new ApplicationServiceImpl(getApplicationContext());
    }


    /**
     * 预加载网络框架的基本信息
     */
    private void initInfoByNetwork() {
        //读取网络配置的基本信息
        String networkInfo = applicationService.readNetworkInfo();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(networkInfo)
                .build();
    }

    /**
     * 通过网络获取基本的信息
     */
    public void getInfoByNetwork() {
        //预先加载网络框架
        initInfoByNetwork();
        //读取Token信息
        String token = applicationService.readToken();

        SystemService systemService = mRetrofit.create(SystemService.class);
        Call<ResponseBody> parkingList = systemService.getParkingList(token);
        parkingList.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String string = response.body().string();
                    Log.e("SUCCESS", "获取的参数--->" + string);
                    JSONObject jsonObject = new JSONObject(string);
                    int code = jsonObject.getInt("code");
                    String jsonList = jsonObject.getString("rows");
                    if (code == 200) {
                        finishingJson(jsonList);
                        return;
                    }
                    Log.e("ERROR", "JSON数据异常!解析失败！");
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
     * 将JSON集合转List集合
     *
     * @param json JSON集合
     */
    private void finishingJson(String json) {
        try {
            JSONArray jsonArray = new JSONArray(json);
            System.out.println("JSON转换的数据-->" + jsonArray);
            //将JSON集合转换为List集合
            list = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                list.add(jsonObject);
            }
            System.out.println("整理后的数据-->" + list);
            sortingData();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 整理数据
     */
    public void sortingData() {
        //将List集合里面的数据整理成List集合
        parkName = new ArrayList<>();
        address = new ArrayList<>();
        vacancy = new ArrayList<>();
        distance = new ArrayList<>();
        rates = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            JSONObject jsonObject = list.get(i);
            try {
                parkName.add(jsonObject.getString("parkName"));
                address.add(jsonObject.getString("address"));
                vacancy.add(jsonObject.getString("vacancy"));
                distance.add(jsonObject.getString("distance"));
                rates.add(jsonObject.getString("rates"));
            }catch (JSONException e){
                throw new RuntimeException(e);
            }

            //遍历集合数据
            for (int j = 0; j < parkName.size(); j++) {
                System.out.println("停车场名称-->" + parkName.get(j));
                System.out.println("详细信息-->" + address.get(j));
                System.out.println("剩余车位-->" + vacancy.get(j));
                System.out.println("距离-->" + distance.get(j));
                System.out.println("价格-->" + rates.get(j));
            }
        }

        //将数据显示在ListView上
        ListView listView = findViewById(R.id.car_ListView);
        CarAdapter carAdapter = new CarAdapter();
        listView.setAdapter(carAdapter);

    }


    private class CarAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return parkName.size();
        }

        @Override
        public Object getItem(int position) {
            return parkName.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //将List集合的数据显示在TextView上
            View view = View.inflate(getApplicationContext(),R.layout.car_layout,null);

            //停车场标题
            TextView title = view.findViewById(R.id.text_title);
            title.setText(parkName.get(position));
            //停车场详情
            TextView details = view.findViewById(R.id.text_details);
            details.setText(address.get(position));
            //停车场价格
            TextView amount = view.findViewById(R.id.text_amount);
            amount.setText(rates.get(position));
            //停车场剩余位置
            TextView textRemaining = view.findViewById(R.id.text_remaining);
            textRemaining.setText(vacancy.get(position));
            //停车场距离
            TextView textDistance = view.findViewById(R.id.text_distance);
            textDistance.setText(distance.get(position));

            return view;
        }
    }


}