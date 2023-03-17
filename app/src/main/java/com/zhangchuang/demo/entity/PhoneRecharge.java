package com.zhangchuang.demo.entity;

/**
 * @Author:Zhangchuang
 * @Date: 2023/3/15 16:25
 */
public class PhoneRecharge {
    private String paymentType;
    private String phonenumber;
    private String rechargeAmount;
    private String ruleId;
    private String type;

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getRechargeAmount() {
        return rechargeAmount;
    }

    public void setRechargeAmount(String rechargeAmount) {
        this.rechargeAmount = rechargeAmount;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "PhoneRecharge{" +
                "paymentType='" + paymentType + '\'' +
                ", phonenumber='" + phonenumber + '\'' +
                ", rechargeAmount='" + rechargeAmount + '\'' +
                ", ruleId='" + ruleId + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    public PhoneRecharge(String paymentType, String phonenumber, String rechargeAmount, String ruleId, String type) {
        this.paymentType = paymentType;
        this.phonenumber = phonenumber;
        this.rechargeAmount = rechargeAmount;
        this.ruleId = ruleId;
        this.type = type;
    }
}
