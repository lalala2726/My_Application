package com.zhangchuang.demo.ui.live;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zhangchuang.demo.R;
import com.zhangchuang.demo.entity.Recharge;

public class InputPaymentInfoActivity extends AppCompatActivity {

    private String categoryId;
    private static final String SERVER_URL = "/prod-api/api/living/recharge";

    private String paymentUnit;

    private String title;

    private String billNumber;

    private String paymentNumber;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_payment_info);
        initView();
        PayInfo();
    }

    /**
     * 初始化View
     */
    public void initView() {
        editText = findViewById(R.id.money_input);
        TextView vPatTitle = findViewById(R.id.payment_title);
        TextView vPaymentNumber = findViewById(R.id.payment_number);
        TextView vPaymentUnit = findViewById(R.id.payment_unit);
        TextView vBillNumber = findViewById(R.id.bill_number);
        TextView vAddress = findViewById(R.id.address);
        //接收意图回传的信息
        categoryId = getIntent().getStringExtra("paymentNo");
        title = getIntent().getStringExtra("title");

        paymentNumber = getIntent().getStringExtra("paymentNo");
        paymentUnit = getIntent().getStringExtra("chargeUnit");
        billNumber = getIntent().getStringExtra("billNo");
        String address = getIntent().getStringExtra("address");
        if ("null".equals(address)) {
            vAddress.setText("无法查询此信息");
        } else {
            vAddress.setText(address);
        }
        //设置信息
        vPatTitle.setText(title);
        vPaymentNumber.setText(paymentNumber);
        vPaymentUnit.setText(paymentUnit);
        vBillNumber.setText(billNumber);
        getSupportActionBar().hide();

    }

    /**
     * 付款信息
     */
    public void PayInfo() {
        findViewById(R.id.money_50).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("50");
            }
        });
        findViewById(R.id.money_100).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("100");
            }
        });
        findViewById(R.id.money_150).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("150");
            }
        });

        findViewById(R.id.pay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = editText.getText().toString();
                Recharge recharge = new Recharge(billNumber, paymentNumber, "电子支付");
                Gson gson = new Gson();
                String json = gson.toJson(recharge);
                Intent intent = new Intent(getApplicationContext(), PayActivity.class);
                intent.putExtra("amount", amount);
                intent.putExtra("paymentUnit", paymentUnit);
                intent.putExtra("title", title);
                intent.putExtra("json", json);
                intent.putExtra("url", SERVER_URL);
                Log.i("JSON信息", json);
                startActivity(intent);
            }
        });
    }
}