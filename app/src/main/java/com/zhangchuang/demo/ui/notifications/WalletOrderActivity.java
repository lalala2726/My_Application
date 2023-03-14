package com.zhangchuang.demo.ui.notifications;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.zhangchuang.demo.R;
import com.zhangchuang.demo.network.api.UserService;
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

public class WalletOrderActivity extends AppCompatActivity {

    private ApplicationServiceImpl applicationService;

    private Retrofit mRetrofit;

    //订单数据
    private List changeTime;
    private List changeType;
    private List changeAmount;
    private List event;

    private ListView orderListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_order);
        initView();
        getWalletOrderInfoByNetwork();
    }


    /**
     * 初始化View
     */
    public void initView() {
        applicationService = new ApplicationServiceImpl(getApplicationContext());
        changeTime = new ArrayList();
        changeType = new ArrayList();
        changeAmount = new ArrayList();
        event = new ArrayList();
        orderListView = findViewById(R.id.order_list_view);
        setTitle("");

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
     * 获取钱包订单信息
     */
    public void getWalletOrderInfoByNetwork() {
        initRetrofit();
        String token = applicationService.readToken();
        UserService userService = mRetrofit.create(UserService.class);
        Call<ResponseBody> walletOrderInfo = userService.getWalletOrderInfo(token);
        walletOrderInfo.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String string = response.body().string();
                    JSONObject jsonObject = new JSONObject(string);
                    Log.i("响应信息", string);
                    if (jsonObject.getInt("code") == 200) {
                        parseJSONToArrayList(jsonObject.getString("rows"));
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
     * 解析并整理JSON数据
     *
     * @param json
     */
    public void parseJSONToArrayList(String json) throws JSONException {
        //将JSON数据存放在ArrayList集合中
        List list = new ArrayList();
        JSONArray jsonArray = new JSONArray(json);
        for (int i = 0; i < jsonArray.length(); i++) {
            list.add(jsonArray.getJSONObject(i));
        }

        //将List集合中的数据整理拿出需要的数据

        for (int i = 0; i < list.size(); i++) {
            JSONObject jsonObject = (JSONObject) list.get(i);
            changeTime.add(jsonObject.getString("changeTime"));
            changeType.add(jsonObject.getString("changeType"));
            event.add(jsonObject.getString("event"));
            changeAmount.add(jsonObject.getString("changeAmount"));
        }

        //遍历数据
        for (int i = 0; i < changeTime.size(); i++) {
            Log.d("changeTime", changeTime.get(i).toString());
            Log.d("changeType", changeType.get(i).toString());
            Log.d("event", event.get(i).toString());
            Log.d("changeAmount", changeAmount.get(i).toString());
        }
        //展示数据
        orderListView.setAdapter(new orderAdapter());
    }


    /**
     * 试图适配器
     */
    private class orderAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return changeAmount.size();
        }

        @Override
        public Object getItem(int position) {
            return changeAmount.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(getApplicationContext(), R.layout.wallet_order_layout, null);
            TextView title = view.findViewById(R.id.order_title);
            TextView time = view.findViewById(R.id.order_change);
            TextView amount = view.findViewById(R.id.order_date);
            amount.setText(changeTime.get(position).toString());
            title.setText(event.get(position).toString());
            time.setText(changeType.get(position).toString());
            time.setText(changeType.get(position).toString() + changeAmount.get(position).toString());
            return view;
        }
    }
}