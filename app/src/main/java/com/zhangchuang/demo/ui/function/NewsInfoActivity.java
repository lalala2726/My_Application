package com.zhangchuang.demo.ui.function;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import com.zhangchuang.demo.R;
import com.zhangchuang.demo.service.impl.ApplicationServiceImpl;

public class NewsInfoActivity extends AppCompatActivity {

    private ApplicationServiceImpl applicationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_info);
        applicationService = new ApplicationServiceImpl(getApplicationContext());
        String networkInfo = applicationService.readNetworkInfo();
        //接受传递一处传递的newsContent信息
        String content = getIntent().getStringExtra("newsContent");
        String title = getIntent().getStringExtra("newsTitle");
        //将Content里面的scr前面加上一个networkInfo
        content = content.replace("src=\"", "src=\"" + networkInfo);
        Log.e("SUCCESS", "传递的信息--->" + title);
        Log.e("SUCCESS", "传递的信息--->" + content);
        setTitle(title);
        //WebView加载本地富文本
        WebView viewById = findViewById(R.id.web_view);
        viewById.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);

    }
}