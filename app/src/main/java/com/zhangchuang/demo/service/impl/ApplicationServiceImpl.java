package com.zhangchuang.demo.service.impl;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.zhangchuang.demo.service.ApplicationService;
import com.zhangchuang.demo.ui.TestActivity;

/**
 * author ZhangChuang
 * date 2023/1/30 23:09
 * description:
 */
public class ApplicationServiceImpl implements ApplicationService {

    private Context context = null;

    public ApplicationServiceImpl(Context context) {
        this.context = context;
    }


    /**
     * 判断用户是否配置过信息
     *
     * @return
     */
    @Override
    public boolean FirstTime() {
        SharedPreferences application = context.getSharedPreferences("Application", context.MODE_PRIVATE);
        return application.getBoolean("FirstTime", false);
    }


    /**
     * 保存配置信息
     *
     * @param ip
     * @param port
     * @return
     */
    @Override
    public boolean saveConfig(String ip, String port) {
        SharedPreferences application = context.getSharedPreferences("Application", context.MODE_PRIVATE);
        SharedPreferences.Editor edit = application.edit();
        edit.putString("IP", ip);
        edit.putString("Port", port);
        edit.commit();
        String ip1 = application.getString("IP", "127.0.0.1");
        String port1 = application.getString("Port", "8080");
        if (ip.equals(ip1) && port.equals(port1)) {
            edit.putBoolean("FirstTime", true);
            edit.commit();
            System.out.println("ip = " + ip + ", port = " + port);
            return true;
        }
        System.out.println("ip = " + ip + ", port = " + port);
        return false;
    }

    /**
     * 判断用户是否登录
     * @return
     */
    @Override
    public boolean FirstLogin() {
        SharedPreferences application = context.getSharedPreferences("Application", Context.MODE_PRIVATE);
        boolean aBoolean = application.getBoolean("Application", false);
        return aBoolean;
    }


}
