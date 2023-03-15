package com.zhangchuang.demo.ui.live;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zhangchuang.demo.R;
import com.zhangchuang.demo.network.api.LiveService;
import com.zhangchuang.demo.service.impl.ApplicationServiceImpl;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PayActivity extends AppCompatActivity {
    private String url;
    private String json;
    private String title;
    private String amount;
    private String payName;

    private ApplicationServiceImpl applicationService;
    private Retrofit mRetrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        acceptParam();
        initView();

        //确定支付
        findViewById(R.id.pay_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payByNetwork(url, json);
            }
        });
        //取消支付
        findViewById(R.id.pay_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //摧毁当前Activity
                finish();
            }
        });
    }

    /**
     * 接受Intent参数
     */
    public void acceptParam() {
        //接受Intent参数
        url = getIntent().getStringExtra("url");
        json = getIntent().getStringExtra("json");
        title = getIntent().getStringExtra("title");
        amount = getIntent().getStringExtra("amount");
        payName = getIntent().getStringExtra("payName");

        //打印传递的信息
        Log.d("传递的信息", "url:" + url + " json:" + json + " title:" + title + " amount:" + amount + " payName:" + payName);
    }

    public void initView() {
        getSupportActionBar().hide();
        applicationService = new ApplicationServiceImpl(getApplicationContext());
        TextView payTitle = findViewById(R.id.pay_title);
        payTitle.setText(title);
        TextView payAmount = findViewById(R.id.pay_amount);
        payAmount.setText(amount);
        TextView payNameView = findViewById(R.id.pay_name);
        payNameView.setText(payName);
    }

    public void initRetrofit() {
        String networkInfo = applicationService.readNetworkInfo();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(networkInfo)
                .build();
    }

    public void payByNetwork(String url, String json) {
        initRetrofit();
        String token = applicationService.readToken();
        LiveService liveService = mRetrofit.create(LiveService.class);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
        Call<ResponseBody> responseBodyCall = liveService.pay(url, token, body);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("GET回调成功", response.toString());
                try {
                    String string = response.body().string();
                    JSONObject jsonObject = new JSONObject(string);
                    if (jsonObject.getInt("code") == 200){
                        Toast.makeText(PayActivity.this, "支付成功！", Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        String msg = jsonObject.getString("msg");
                        Toast.makeText(PayActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                    Log.d("响应体", string);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.w("GET回调失败", throwable);
            }
        });
    }

    /**
     * 支付成功方法
     */
    public void paySuccess() {
    }
}