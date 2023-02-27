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
     * 读取网络配置信息
     *
     * @return
     */
    public String readNetworkInfo() {
        SharedPreferences application = context.getSharedPreferences("Application", Context.MODE_PRIVATE);
        String ip = application.getString("IP", null);
        String port = application.getString("Port", null);
        return "http://" + ip + ":" + port;
    }

    /**
     * 判断用户是否登录,如果用登录过为true，没有登录过是false
     *
     * @return
     */
    @Override
    public boolean FirstLogin() {
        SharedPreferences application = context.getSharedPreferences("Application", Context.MODE_PRIVATE);
        boolean aBoolean = application.getBoolean("Application", false);
        return aBoolean;
    }


    /**
     * 读取token信息
     *
     * @return 返回Token信息
     */
    public String readToken() {
        SharedPreferences application = context.getSharedPreferences("application", Context.MODE_PRIVATE);
        return application.getString("Token", null);
    }

    /**
     * 保存用户登录信息
     */
    public void saveLogin(Boolean info) {
        SharedPreferences application = context.getSharedPreferences("Application", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = application.edit();
        SharedPreferences.Editor editor = edit.putBoolean("Application", info);
        edit.commit();
    }


}
