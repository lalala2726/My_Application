package com.zhangchuang.demo.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.zhangchuang.demo.R;
import com.zhangchuang.demo.databinding.FragmentHomeBinding;
import com.zhangchuang.demo.service.ApplicationService;
import com.zhangchuang.demo.ui.TestActivity;
import com.zhangchuang.demo.ui.start.EditConfigActivity;
import com.zhangchuang.demo.ui.start.GuideActivity;
import com.zhangchuang.demo.utils.MyLoader;

import java.awt.font.TextAttribute;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    //定义图片资源
    int[] icons ={R.drawable.phone1,R.drawable.phone2,R.drawable.phone3,R.drawable.phone4,R.drawable.phone1,
            R.drawable.phone1,
            R.drawable.phone1,
            R.drawable.phone1,
            R.drawable.phone1,
            R.drawable.phone1,
            R.drawable.phone1,
            R.drawable.phone1,
            R.drawable.phone1,
            R.drawable.phone1,
            R.drawable.phone1,};
    String[] titles = {"这是需要演示的文本信息,文本信息文本信息文本信息文本信息文本信息",
            "这是需要演示的文本信息,文本信息文本信息文本信息文本信息文本信息",
            "这是需要演示的文本信息,文本信息文本信息文本信息文本信息文本信息",
            "这是需要演示的文本信息,文本信息文本信息文本信息文本信息文本信息",
            "这是需要演示的文本信息,文本信息文本信息文本信息文本信息文本信息",
            "这是需要演示的文本信息,文本信息文本信息文本信息文本信息文本信息",
            "这是需要演示的文本信息,文本信息文本信息文本信息文本信息文本信息",
            "这是需要演示的文本信息,文本信息文本信息文本信息文本信息文本信息",
            "这是需要演示的文本信息,文本信息文本信息文本信息文本信息文本信息",
            "这是需要演示的文本信息,文本信息文本信息文本信息文本信息文本信息",
            "这是需要演示的文本信息,文本信息文本信息文本信息文本信息文本信息",
            "这是需要演示的文本信息,文本信息文本信息文本信息文本信息文本信息",
            "这是需要演示的文本信息,文本信息文本信息文本信息文本信息文本信息",
            "这是需要演示的文本信息,文本信息文本信息文本信息文本信息文本信息",
            "这是需要演示的文本信息,文本信息文本信息文本信息文本信息文本信息",};

    private Banner banner;
    private ListView listView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        banner = view.findViewById(R.id.vv_banner);
        initView();
        listView = view.findViewById(R.id.new_list);
        NewsAdapter newsAdapter = new NewsAdapter();
        listView.setAdapter(newsAdapter);
        return view;
    }


    private class NewsAdapter extends BaseAdapter{
        //获取item的数量
        @Override
        public int getCount() {
            return icons.length;
        }

        //记录浏览的列表
        @Override
        public Object getItem(int position) {
            return titles[icons.length];
        }

        //获取选中的序号
        @Override
        public long getItemId(int position) {
            return 0;
        }

        //用于加载ListView需要加载的内容
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(getActivity(),R.layout.news,null);
            TextView title = view.findViewById(R.id.textView14);
            ImageView viewById = view.findViewById(R.id.imageView3);
            title.setText(titles[position]);
            viewById.setImageResource(icons[position]);
            return view;
        }
    }

    private void initView() {
        //资源文件
        List images = new ArrayList<Integer>();
        images.add(R.drawable.phone1);
        images.add(R.drawable.phone2);
        images.add(R.drawable.phone3);
        images.add(R.drawable.phone4);
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        banner.setImageLoader(new MyLoader());
        banner.setImages(images);
        banner.setBannerAnimation(Transformer.DepthPage);
        banner.start();
    }

}