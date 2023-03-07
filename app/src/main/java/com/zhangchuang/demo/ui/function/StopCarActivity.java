package com.zhangchuang.demo.ui.function;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

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


    private void finishingJson(String json) {
        try {
            JSONArray jsonArray = new JSONArray(json);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private class CarAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }
    }

}