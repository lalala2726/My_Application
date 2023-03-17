package com.zhangchuang.demo.ui.live;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zhangchuang.demo.R;
import com.zhangchuang.demo.entity.Feedback;
import com.zhangchuang.demo.network.api.LiveService;
import com.zhangchuang.demo.service.impl.ApplicationServiceImpl;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FeedBackActivity extends AppCompatActivity {

    private ApplicationServiceImpl applicationService;

    private Retrofit mRetrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeb_back);
        //隐藏标题栏
        getSupportActionBar().hide();
        initView();
    }

    /**
     * 初始化View
     */
    public void initView() {
        applicationService = new ApplicationServiceImpl(getApplicationContext());
        findViewById(R.id.live_feedback_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView content = findViewById(R.id.live_feedback_content);
                if (content.getText().toString() == "") {
                    Toast.makeText(FeedBackActivity.this, "请填写反馈内容", Toast.LENGTH_SHORT).show();
                    return;
                }
                Feedback feedback = new Feedback(content.getText().toString(), "生活缴费-意见反馈");
                Gson gson = new Gson();
                String toJson = gson.toJson(feedback);
                Log.d("JSON信息", toJson);
                submitFeedBackInfoByNetwork(toJson);
            }
        });
    }

    /**
     * 初始化Retrofit信息
     */
    public void initRetrofit() {
        String networkInfo = applicationService.readNetworkInfo();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(networkInfo)
                .build();
    }

    /**
     * 通过网络提交反馈信息
     */
    public void submitFeedBackInfoByNetwork(String json) {
        initRetrofit();
        String token = applicationService.readToken();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);
        LiveService liveService = mRetrofit.create(LiveService.class);
        Call<ResponseBody> responseBodyCall = liveService.submitLiveFeedBack(token, requestBody);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String string = response.body().string();
                    Log.d("POST回调成功", string);
                    JSONObject jsonObject = new JSONObject(string);
                    if (jsonObject.getInt("code") == 200) {
                        Toast.makeText(FeedBackActivity.this, "反馈成功!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(FeedBackActivity.this, "反馈失败！", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    Log.w("IOException", e.toString());
                } catch (JSONException e) {
                    Log.w("JSONException", e.toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e("POST回调失败", throwable.toString());
            }
        });

    }
}