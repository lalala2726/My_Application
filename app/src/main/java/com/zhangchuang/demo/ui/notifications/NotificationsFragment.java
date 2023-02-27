package com.zhangchuang.demo.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.zhangchuang.demo.R;
import com.zhangchuang.demo.databinding.FragmentNotificationsBinding;

import org.w3c.dom.Text;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        View userInfo = view.findViewById(R.id.rela_2);
        userInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), UserinfoActivity.class);
                startActivity(intent);
            }
        });
        initUserInfo();
        return view;
    }

    private void initUserInfo() {
        System.out.println("初始化用户信息~~~");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}