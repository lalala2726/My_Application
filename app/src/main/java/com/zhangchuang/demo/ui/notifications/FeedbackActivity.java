package com.zhangchuang.demo.ui.notifications;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zhangchuang.demo.R;
import com.zhangchuang.demo.entity.Feedback;
import com.zhangchuang.demo.network.api.SystemService;
import com.zhangchuang.demo.service.impl.ApplicationServiceImpl;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FeedbackActivity extends AppCompatActivity {

    private EditText editText;
    private EditText title_view;
    private Retrofit mRetrofit;

    private ApplicationServiceImpl applicationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        init();
        findViewById(R.id.confirm_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editText.getText().toString();
                String title = title_view.getText().toString();
                if (title.length() == 0){
                    Toast.makeText(getApplicationContext(), "请填写您的主题", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (text.length() == 0) {
                    Toast.makeText(getApplicationContext(), "请填写您的意见！", Toast.LENGTH_SHORT).show();
                    return;
                }
                Feedback feedback = new Feedback();
                feedback.setTitle(title);
                feedback.setContent(text);
                Gson gson = new Gson();
                String json = gson.toJson(Feedback.class);
                feedbackByNetwork(json);
            }
        });
    }

    /**
     * 初始化控件
     */
    public void init() {
        setTitle("意见反馈");
        editText = findViewById(R.id.text_view);
        title_view = findViewById(R.id.title_view);
        applicationService = new ApplicationServiceImpl(getApplicationContext());
    }


    /**
     * 预加载Retrofit
     */
    public void initRetrofit() {
        String networkInfo = applicationService.readNetworkInfo();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(networkInfo)
                .build();
    }

    /**
     * 通过网络反馈信息
     */
    public void feedbackByNetwork(String json) {
        initRetrofit();
        SystemService systemService = mRetrofit.create(SystemService.class);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);
        Call<ResponseBody> feedback = systemService.feedback(null, requestBody);
        feedback.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.e("SUCCESS", "POST回调成功！返回参数-->" + response.body().string());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e("ERROR", "POST回调失败！错误信息--->" + throwable);
            }
        });
    }
}