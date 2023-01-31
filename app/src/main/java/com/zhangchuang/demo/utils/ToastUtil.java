package com.zhangchuang.demo.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * author ZhangChuang
 * date 2023/1/31 13:53
 * description:
 */
public class ToastUtil {
    private Context context = null;


    public ToastUtil(Context context) {
        this.context = context;
    }

    public void displayMas(String msg) {
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
    }
}
