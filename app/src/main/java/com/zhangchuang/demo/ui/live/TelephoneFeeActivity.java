package com.zhangchuang.demo.ui.live;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zhangchuang.demo.R;
import com.zhangchuang.demo.databinding.TelephoneFeeOrderMain2Binding;
import com.zhangchuang.demo.entity.PhoneRecharge;

public class TelephoneFeeActivity extends AppCompatActivity {

    private EditText telephone;
    private TextView telephoneInfo;
    private String amount = "30";

    private static final String SERVER_ADDRESS = "/prod-api/api/living/phone/recharge";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tele_phone_fee);
        initView();

        //主逻辑
        findViewById(R.id.charge_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String info = telephone.getText().toString();
                if ("".equals(info)) {
                    Toast.makeText(TelephoneFeeActivity.this, "请先填写电话号码", Toast.LENGTH_SHORT).show();
                    telephoneInfo.setText("请先填写电话号码");
                    return;
                }
                getTelephoneInfo();
            }
        });

        //跳转到订单订单列表
        findViewById(R.id.query_bill).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TelephoneFeeOrderActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 初始化
     */
    public void initView() {
        setTitle("话费充值");
        TelephoneAmount();
        telephone = findViewById(R.id.telephone_edit);
        telephoneInfo = findViewById(R.id.telephone_info);
    }


    public void getTelephoneInfo() {
        String telephoneNumber = telephone.getText().toString();
        if (telephoneNumber.length() == 11) {
            telephoneInfo.setText("充值金额：" + amount + "元");
            PhoneRecharge phoneRecharge = new PhoneRecharge("电子支付", telephoneNumber, amount, "1", "2");
            Gson gson = new Gson();
            String json = gson.toJson(phoneRecharge);
            Intent intent = new Intent(getApplicationContext(), PayActivity.class);
            //意图传递
            String payName = "为" + telephoneNumber + "充值话费";
            intent.putExtra("json", json);
            intent.putExtra("url", SERVER_ADDRESS);
            intent.putExtra("title", "手机充值服务");
            intent.putExtra("amount", "￥" + amount);
            intent.putExtra("payName", payName);
            startActivity(intent);

            Log.d("JSON信息", json);
        } else {
            Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
            telephoneInfo.setText("请输入正确的手机号");
        }
    }

    /**
     * 话费充值金额
     */
    public void TelephoneAmount() {
        findViewById(R.id.amount_30).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                telephoneInfo.setText("你将充值30元");
                amount = "30";
            }
        });
        findViewById(R.id.amount_50).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                telephoneInfo.setText("你将充值50元");
                amount = "50";
            }
        });
        findViewById(R.id.amount_100).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                telephoneInfo.setText("你将充值100元");
                amount = "100";
            }
        });
        findViewById(R.id.amount_200).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                telephoneInfo.setText("你将充值200元");
                amount = "200";
            }
        });
        findViewById(R.id.amount_300).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                telephoneInfo.setText("你将充值300元");
                amount = "300";
            }
        });
        findViewById(R.id.amount_500).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                telephoneInfo.setText("你为充值500元");
                amount = "500";
            }
        });
    }
}