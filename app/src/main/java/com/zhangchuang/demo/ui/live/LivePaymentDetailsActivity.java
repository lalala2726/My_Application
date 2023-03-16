package com.zhangchuang.demo.ui.live;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zhangchuang.demo.R;
import com.zhangchuang.demo.network.api.LiveService;
import com.zhangchuang.demo.service.impl.ApplicationServiceImpl;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LivePaymentDetailsActivity extends AppCompatActivity {

    private String categoryId;
    private String title;
    private EditText ePaymentNumber;

    private Retrofit mRetrofit;

    private String paymentNumber;

    private ApplicationServiceImpl applicationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_payment_detaills);
        initView();
        //下一步
        findViewById(R.id.next_step).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentNumber = ePaymentNumber.getText().toString();
                if ("".equals(paymentNumber)) {
                    Toast.makeText(LivePaymentDetailsActivity.this, "请输入您的缴费编号", Toast.LENGTH_SHORT).show();
                    return;
                }
                /*Intent intent = new Intent(getApplicationContext(), InputPaymentInfoActivity.class);
                intent.putExtra("categoryId", categoryId);
                intent.putExtra("title", title);
                startActivity(intent);*/
                verificationInfoByNetwork();
            }
        });
        //获取缴费编号
        findViewById(R.id.get_payment_number).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GetPaymentNumberActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 初始化View
     */
    public void initView() {
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.white));
        categoryId = getIntent().getStringExtra("categoryId");
        title = getIntent().getStringExtra("title");
        ePaymentNumber = findViewById(R.id.payment_number);
        applicationService = new ApplicationServiceImpl(getApplicationContext());
        TextView paymentTitle = findViewById(R.id.payment_title);
        paymentTitle.setText(title);
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
     * 验证编号是否存在
     */
    public void verificationInfoByNetwork() {
        initRetrofit();
        String token = applicationService.readToken();
        LiveService liveService = mRetrofit.create(LiveService.class);
        Call<ResponseBody> responseBodyCall = liveService.verificationInfo(token, paymentNumber, categoryId);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("GET回调成功", response.toString());
                try {
                    String string = response.body().string();
                    JSONObject jsonObject = new JSONObject(string);
                    if (jsonObject.getInt("code") == 200) {
                        String msg = jsonObject.getString("msg");
                        Toast.makeText(LivePaymentDetailsActivity.this, msg, Toast.LENGTH_SHORT).show();

                        String data = jsonObject.getString("data");
                        QuerySuccessful(data);
                    } else {
                        String msg = jsonObject.getString("msg");
                        Toast.makeText(LivePaymentDetailsActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                    Log.i("响应参数", string);
                } catch (IOException e) {
                    Log.w("IOException", e.toString());
                } catch (JSONException e) {
                    Log.w("IOException", e.toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.w("GET回调失败", throwable.toString());
            }
        });
    }

    public void QuerySuccessful(String data) throws JSONException {
        JSONObject jsonObject = new JSONObject(data);
        String billNo = jsonObject.getString("billNo");
        String chargeUnit = jsonObject.getString("chargeUnit");
        String paymentNo = jsonObject.getString("paymentNo");
        String address = jsonObject.getString("address");
        Log.i("整理的信息", billNo + " " + chargeUnit + " " + paymentNo + " " + address);
        Intent intent = new Intent(getApplicationContext(), InputPaymentInfoActivity.class);
        intent.putExtra("categoryId", categoryId);
        intent.putExtra("title", title);
        intent.putExtra("billNo", billNo);
        intent.putExtra("chargeUnit", chargeUnit);
        intent.putExtra("paymentNo", paymentNo);
        intent.putExtra("address", address);
        startActivity(intent);
    }
}