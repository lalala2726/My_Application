package com.zhangchuang.demo.entity;

/**
 * @Author:Zhangchuang
 * @Date: 2023/2/9 19:47
 */
public class Data<T> {

    private String msg;

    private Integer code;

    private T data;

    public Data(String msg, Integer code, T data) {
        this.msg = msg;
        this.code = code;
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
