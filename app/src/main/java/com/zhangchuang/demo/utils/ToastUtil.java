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


    /**
     * 上下文信息
     * @param context
     */
    public ToastUtil(Context context) {
        this.context = context;
    }

    /**
     * 显示文本信息
     * @param Mes
     */
    public void displayMes(String Mes) {
        Toast.makeText(context,Mes,Toast.LENGTH_LONG).show();
    }
}
