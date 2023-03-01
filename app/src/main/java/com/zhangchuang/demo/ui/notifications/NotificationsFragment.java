package com.zhangchuang.demo.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.zhangchuang.demo.entity.UpdatePassword;
import com.zhangchuang.demo.network.api.UserService;
import com.zhangchuang.demo.service.impl.ApplicationServiceImpl;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;

    private Retrofit mRetrofit;

    private TextView userNameView;
    private ApplicationServiceImpl applicationService;

    private static final String LOCAL_SERVER_ADDRESS = "http://192.168.43.139:8080";


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

        //跳转至修改密码
        view.findViewById(R.id.rela_34).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), UpdatePasswordActivity.class);
                startActivity(intent);
            }
        });

        //跳转至意见反馈
        view.findViewById(R.id.rela_6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FeedbackActivity.class);
                startActivity(intent);
            }
        });
        initUserInfo();
        init(view);
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

    /**
     * 系统初始化
     */
    public void init(View v) {
        applicationService = new ApplicationServiceImpl(getContext());
        userNameView = v.findViewById(R.id.textView16);
        getUserInfoByNetwork();
    }

    /**
     * 初始化Retrofit
     */
    public void initRetrofit() {
        //读取配置文件信息
        String networkInfo = applicationService.readNetworkInfo();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(LOCAL_SERVER_ADDRESS)
                .build();
    }


    /**
     * 通过网络获取用户信息
     */
    public void getUserInfoByNetwork() {
        initRetrofit();
        //读取token信息
        String token = applicationService.readToken();
        UserService userService = mRetrofit.create(UserService.class);
        Call<ResponseBody> userInfo = userService.getUserInfo(10);
        userInfo.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String json = response.body().string();
                    Log.i("SUCCESS", "执行成功!返回信息-->" + json);
                    setUserInfo(json);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e("网络信息", "网络执行失败!\n错误信息--->" + throwable);
            }
        });
    }


    public void setUserInfo(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            String username = jsonObject.getString("username");
            userNameView.setText(username);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}