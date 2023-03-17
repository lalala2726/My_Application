package com.zhangchuang.demo.ui.live;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

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

public class GetPaymentNumberActivity extends AppCompatActivity {


    private EditText edID;

    private ApplicationServiceImpl applicationService;
    private Retrofit mRetrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_payment_number);
        initView();
        findViewById(R.id.btn_get_payment_number).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("回调一次");
                String id = edID.getText().toString();
                String categoryId = getIntent().getStringExtra("categoryId");
                queryUserRechargeId(id, categoryId);

            }
        });
    }

    /**
     * 初始化
     */
    public void initView() {
        //隐藏标题栏
        getSupportActionBar().hide();
        edID = findViewById(R.id.et_id);
        applicationService = new ApplicationServiceImpl(getApplicationContext());
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
     * 查询用户充值ID
     */
    public void queryUserRechargeId(String id, String categoryId) {
        initRetrofit();
        String token = applicationService.readToken();
        LiveService liveService = mRetrofit.create(LiveService.class);
        Call<ResponseBody> responseBodyCall = liveService.queryUserRechargeId(token, categoryId, id);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("GET回调成功", response.toString());
                try {
                    String string = response.body().string();
                    JSONObject jsonObject = new JSONObject(string);
                    if (jsonObject.getInt("code") == 200) {
                        String data = jsonObject.getString("data");
                        finishData(data);
                    }
                    Log.i("响应信息", string);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.d("GET回调失败", throwable.toString());
            }
        });
    }

    /**
     * 整理数据
     */
    public void finishData(String data) throws JSONException {
        String string = null;
        JSONArray jsonArray = new JSONArray(data);
        for (int i = 0; i < jsonArray.length(); i++) {
            string = jsonArray.getString(i);
        }
        JSONObject jsonObject = new JSONObject(string);
        String paymentNo = jsonObject.getString("paymentNo");
        //返回上级目录
        Intent intent = new Intent(getApplicationContext(),LivePaymentDetailsActivity.class);
        intent.putExtra("paymentNo", paymentNo);
        startActivity(intent);
    }
}