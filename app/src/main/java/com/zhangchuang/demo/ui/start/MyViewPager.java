package com.zhangchuang.demo.ui.start;

import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

/**
 * author ZhangChuang
 * date 2023/1/30 10:27
 * description:
 */
public class MyViewPager extends PagerAdapter {
    private List<View> mViewList ;

    public MyViewPager(List<View> mViewList ) {
        this.mViewList = mViewList;
    }

    @Override
    public int getCount() {

        return mViewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mViewList.get(position));
        return mViewList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViewList.get(position));
    }
}