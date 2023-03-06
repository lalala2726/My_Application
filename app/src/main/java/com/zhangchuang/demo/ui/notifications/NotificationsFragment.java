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

import com.google.gson.Gson;
import com.zhangchuang.demo.R;
import com.zhangchuang.demo.databinding.FragmentNotificationsBinding;
import com.zhangchuang.demo.entity.UpdatePassword;
import com.zhangchuang.demo.entity.User;
import com.zhangchuang.demo.network.api.UserService;
import com.zhangchuang.demo.service.impl.ApplicationServiceImpl;
import com.zhangchuang.demo.ui.login.LoginActivity;

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
        view.findViewById(R.id.rela_7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
        initUserInfo(view);
        return view;
    }

    private void initUserInfo(View v) {
        userNameView = v.findViewById(R.id.textView16);
        System.out.println("初始化用户信息~~~");
        initInfo();
        getUserInfoBuNetWork();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    /**
     * 预加载信息
     */
    public void initInfo() {
        applicationService = new ApplicationServiceImpl(getContext());
    }

    /**
     * 预加载Retrofit
     */
    public void initRetrofit() {
        String readNetworkInfo = applicationService.readNetworkInfo();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(readNetworkInfo)
                .build();
    }

    public void getUserInfoBuNetWork() {
        initRetrofit();
        Log.e("Msg", "正在加载网络信息");
        String token = applicationService.readToken();
        UserService userService = mRetrofit.create(UserService.class);
        userService.getUserInfo(token);
        Call<ResponseBody> userInfo = userService.getUserInfo(token);
        userInfo.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String json = response.body().string();

                    Log.e("SUCCESS", "返回的原始信息:" + json);
                    JsonToUser(json);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {

            }
        });
    }

    /**
     * 将JSON数据转Java实体类
     *
     * @param json 需要传输的JSON数据
     */
    public void JsonToUser(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            int code = jsonObject.getInt("code");
            if (code == 200) {
                String user = jsonObject.getString("user");
                Gson gson = new Gson();
                User user1 = gson.fromJson(user, User.class);
                System.out.println("二次清洗信息:" + user1);
                //显示用户信息
                userNameView.setText(user1.getUserName());
            } else {
                Log.e("ERROR", "用户信息获取失败！");
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }


}