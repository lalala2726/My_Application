package com.zhangchuang.demo.entity;

/**
 * @Author:Zhangchuang
 * @Date: 2023/3/16 16:43
 */
public class Recharge {
    private String billNo;
    private String paymentNo;
    private String paymentType;

    public Recharge(String billNo, String paymentNo, String paymentType) {
        this.billNo = billNo;
        this.paymentNo = paymentNo;
        this.paymentType = paymentType;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getPaymentNo() {
        return paymentNo;
    }

    public void setPaymentNo(String paymentNo) {
        this.paymentNo = paymentNo;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }
}
