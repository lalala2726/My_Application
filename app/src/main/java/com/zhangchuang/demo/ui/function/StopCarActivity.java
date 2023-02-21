package com.zhangchuang.demo.ui.function;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.zhangchuang.demo.R;

import java.util.ArrayList;
import java.util.List;

public class StopCarActivity extends AppCompatActivity {


    //    private String[] titleData = {"商城停车场"};
    List titleData = new ArrayList<String>();
    List informationData = new ArrayList<String>();
    List distancData = new ArrayList<String>();
    List surplusData = new ArrayList<String>();
    List timeData = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stopcar);

        //隐藏标题栏
        getSupportActionBar().hide();
        initInfo();
        ListView carListView = findViewById(R.id.car_ListView);
        CarAdapter carAdapter = new CarAdapter();
        carListView.setAdapter(carAdapter);
    }


    /**
     * 初始化数据加载数据
     */
    private void initInfo() {

        titleData.add("西安停车场");
        titleData.add("兴庆南路停车场");
        titleData.add("延兴路停车场");
        titleData.add("祥宁路停车场");
        titleData.add("东莞路停车场");
        titleData.add("商城停车场");


        informationData.add("兴庆南路北");
        informationData.add("兴庆南路南");
        informationData.add("延兴路北");
        informationData.add("祥宁路北");
        informationData.add("东莞路北");
        informationData.add("商城地下车库");
        distancData.add("10M");
        distancData.add("120M");
        distancData.add("800M");
        distancData.add("1200M");
        distancData.add("200M");
        distancData.add("205M");

        surplusData.add("100");
        surplusData.add("88");
        surplusData.add("52");
        surplusData.add("69");
        surplusData.add("20");
        surplusData.add("85");

        timeData.add("2");
        timeData.add("5");
        timeData.add("3");
        timeData.add("3");
        timeData.add("3");
        timeData.add("10");

    }


    private class CarAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return titleData.size();
        }

        @Override
        public Object getItem(int position) {
            return titleData.get(position);
        }

        @Override
        public long getItemId(int position) {
            System.out.println("你点击--->" + position);
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(StopCarActivity.this, R.layout.car_layout, null);

            //使用数组测试
//            TextView title = view.findViewById(R.id.c_title);
//            title.setText(titleData[position]);

            //使用List测试
            TextView title = view.findViewById(R.id.c_title);
            title.setText(titleData.get(position).toString());

            TextView information = view.findViewById(R.id.information);
            information.setText(informationData.get(position).toString());

            TextView distance = view.findViewById(R.id.distanc);
            distance.setText(distancData.get(position).toString());

            TextView surplus = view.findViewById(R.id.surplus);
            surplus.setText(surplusData.get(position).toString());

            TextView time = view.findViewById(R.id.c_time);
            time.setText(timeData.get(position).toString());



            return view;
        }
    }
}