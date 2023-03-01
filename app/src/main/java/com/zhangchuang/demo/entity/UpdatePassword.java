package com.zhangchuang.demo.entity;

/**
 * @Author:Zhangchuang
 * @Date: 2023/2/28 15:01
 */
public class UpdatePassword {
    private String oldPassword;
    private String newPassword;


    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
