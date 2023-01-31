package com.zhangchuang.demo.service;

import android.content.Context;

/**
 * author ZhangChuang
 * date 2023/1/30 23:06
 * description:系统服务
 */
public interface ApplicationService {


    /**
     * 判断用户是否配置过信息
     *
     * @return
     */
    boolean FirstTime();

    /**
     * 保存配置信息
     *
     * @return
     */
    boolean saveConfig(String ip, String port);

}
