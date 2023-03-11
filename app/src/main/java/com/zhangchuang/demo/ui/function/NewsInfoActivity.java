package com.zhangchuang.demo.ui.function;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import com.zhangchuang.demo.R;

public class NewsInfoActivity extends AppCompatActivity {

    private String info = "<p>\\t按照去年iPhone 12系列的发布时间节点来看，如今距离苹果新一代iPhone发布的时间还有很长一段时间，但这并未能够阻挡消费者对于iPhone 13系列的关注热情。每当网上一有关于新机的爆料信息，总能引起了不少网友的围观与讨论</p><p><br></p><p>\\t就在近日，国外一数码博主便通过一则视频，向广大网友展示疑似iPhone 13 Pro Max的机模。根据该视频显示，全新的iPhone 13 Pro Max在外观上并没有做出太大的改变，依旧保持了刘海屏设计，同时后置依旧采用了三摄模组。区别的是，相较于上一代iPhone 13 Pro Max似乎机身厚度变大了些许，且摄像头模组的凸起程度也更高，而且正面的刘海屏也能感觉到明显变窄了不少。</p><p><br></p><p><img src=http://124.93.196.45:10001/prod-api/profile/upload/image/2021/05/17/863b3306-9fad-4255-a065-0f59b57c7cc4.jpg\\\"></p><p>\\t</p><p>      至于配置方面，iPhone 13 Pro Max有望搭载LTPO显示屏，借助于这一屏幕材质的特殊性，能够有效降低屏幕的功耗，iPhone 13 Pro Max大概率将会支持120Hz屏幕刷新率，从而拉开苹果手机的高刷屏序幕。此外，存储空间也或将升级至1TB内存。如果真的如此的话，那么对于广大果粉朋友来说，绝对算得上是近几年苹果带给用户最大的福音。</p><p><img src=\\\"/prod-api/profile/upload/image/2021/05/17/ec953328-80e2-457a-a18b-d61009a8e498.jpg\\\"></p>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_info);
        //WebView加载本地富文本
        WebView viewById = findViewById(R.id.web_view);
        viewById.loadDataWithBaseURL(null, info, "text/html", "utf-8", null);

    }
}