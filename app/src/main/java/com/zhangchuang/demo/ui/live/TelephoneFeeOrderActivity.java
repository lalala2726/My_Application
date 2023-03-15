package com.zhangchuang.demo.ui.live;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

public class TelephoneFeeOrderActivity extends AppCompatActivity {

    private ApplicationServiceImpl applicationService;

    private Retrofit mRetrofit;
    private ListView listView;
    //资源文件
    private List title;
    private List rechargeAmount;
    private List paymentAmount;
    private List paymentType;
    private List rechargeTime;
    private List userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.telephone_fee_order_main2);
        initView();
        getTelephoneFeeOrderByNetwork();
    }

    /**
     * 初始化
     */
    public void initView() {
        //将菜单栏的颜色设置为白色
        setTitle("话费账单");
        listView = findViewById(R.id.order_list_view);
        applicationService = new ApplicationServiceImpl(getApplicationContext());
        title = new ArrayList();
        rechargeAmount = new ArrayList();
        paymentAmount = new ArrayList();
        paymentType = new ArrayList();
        rechargeTime = new ArrayList();
        userId = new ArrayList();


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
     * 通过网络获取话费账单
     */
    public void getTelephoneFeeOrderByNetwork() {
        initRetrofit();
        String token = applicationService.readToken();
        LiveService liveService = mRetrofit.create(LiveService.class);
        Call<ResponseBody> telephoneFeeOrder = liveService.getTelephoneFeeOrder(token);
        telephoneFeeOrder.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String string = response.body().string();
                    JSONObject jsonObject = new JSONObject(string);
                    if (jsonObject.getInt("code") == 200) {
                        String rows = jsonObject.getString("rows");
                        finishData(rows);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e("GET回调失败", throwable.toString());
            }
        });
    }


    /**
     * 整理数据
     *
     * @param json 需要整理的JSON数据
     */
    public void finishData(String json) throws JSONException {
        //将JSON数组数据存入ArrayList集合
        List list = new ArrayList();
        JSONArray jsonArray = new JSONArray(json);
        for (int i = 0; i < jsonArray.length(); i++) {
            list.add(jsonArray.getJSONObject(i));
        }

        //从ArrayList中取出可用的数据
        for (int i = 0; i < list.size(); i++) {
            JSONObject jsonObject = (JSONObject) list.get(i);
            title.add(jsonObject.getString("title"));
            rechargeAmount.add("实际话费到账：" + jsonObject.getString("rechargeAmount"));
            paymentAmount.add("-￥" + jsonObject.getString("paymentAmount"));
            paymentType.add(jsonObject.getString("paymentType"));
            rechargeTime.add(jsonObject.getString("rechargeTime"));
            userId.add("支付用户ID：" + jsonObject.getString("userId"));
        }

        //遍历数据
        for (int i = 0; i < title.size(); i++) {
            Log.e("title", title.get(i).toString());
            Log.e("rechargeAmount", rechargeAmount.get(i).toString());
            Log.e("paymentAmount", paymentAmount.get(i).toString());
            Log.e("paymentType", paymentType.get(i).toString());
            Log.e("rechargeTime", rechargeTime.get(i).toString());
            Log.e("userId", userId.get(i).toString());
        }

        listView.setAdapter(new phoneOderAdapter());
    }

    private class phoneOderAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return title.size();
        }

        @Override
        public Object getItem(int position) {
            return title.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(getApplicationContext(), R.layout.telephone_fee_order_layout, null);
            TextView orderTitle = view.findViewById(R.id.order_title);
            TextView orderDate = view.findViewById(R.id.order_date);
            TextView orderChange = view.findViewById(R.id.order_change);
            TextView orderRechargeAmount = view.findViewById(R.id.rechargeAmount);
            TextView orderUserId = view.findViewById(R.id.user_id);
            TextView orderPaymentType = view.findViewById(R.id.paymentType);

            orderTitle.setText(title.get(position).toString());
            orderDate.setText(rechargeTime.get(position).toString());
            orderChange.setText(paymentAmount.get(position).toString());
            orderRechargeAmount.setText(rechargeAmount.get(position).toString());
            orderUserId.setText(userId.get(position).toString());
            orderPaymentType.setText(paymentType.get(position).toString());

            return view;
        }
    }
}