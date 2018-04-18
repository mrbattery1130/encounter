package com.mrbattery.encounter;

import android.app.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mrbattery.encounter.constant.Constant;
import com.mrbattery.encounter.entity.UserToken;
import com.mrbattery.encounter.util.HttpUtil;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.ContentValues.TAG;


public class LoginActivity extends Activity {

    @BindView(R.id.tv_user_id)
    public AutoCompleteTextView tvUserID;

    @BindView(R.id.tv_password)
    public EditText tvPassword;

    @BindView(R.id.sign_in_button)
    public Button signInBtn;

    @BindView(R.id.login_progress)
    public ProgressBar loginProgress;

    @BindView(R.id.login_overlay)
    public View loginOverlay;

    //记录用户首次点击返回键的时间
    private long firstTime = 0;

    //全局变量
    private String userID;
    private UserToken userToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //绑定activity
        ButterKnife.bind(this);
        //初始化加载进度条和遮罩
        loginProgress.setVisibility(View.GONE);
        loginOverlay.setVisibility(View.GONE);
        loginOverlay.setClickable(false);
    }

    @OnClick(R.id.sign_in_button)
    public void signIn() {

        userID = tvUserID.getText().toString();
        String password = tvPassword.getText().toString();
        String url = "http://" + this.getString(R.string.server_ip) + ":8080/login?userID=" + userID + "&password=" + password;
        //显示加载进度条和遮罩
        loginProgress.setVisibility(View.VISIBLE);
        loginOverlay.setVisibility(View.VISIBLE);
        loginOverlay.setClickable(true);
//        Toast.makeText(this, "点击了登录按钮！！！！", Toast.LENGTH_SHORT).show();
        Log.i(TAG, url);

        //异步请求+回调
        Log.i(TAG, "signIn: 开始请求数据");
        HttpUtil.getDataAsync(url, this, new Runnable() {
            @Override
            public void run() {
                String responseData = HttpUtil.getResponseData();
                Log.i(TAG, "run: " + responseData);
                //隐藏加载进度条和遮罩
                loginProgress.setVisibility(View.INVISIBLE);
                loginOverlay.setVisibility(View.INVISIBLE);
                loginOverlay.setClickable(false);
                if (responseData.equals("success")) {
                    Log.i(TAG, "run: login successful!!!");
                    Constant.setCurrUserID(userID);
                    getToken(userID);
                } else if (responseData.equals("invalid_user_id")) {
                    Toast.makeText(LoginActivity.this, "用户不存在", Toast.LENGTH_SHORT).show();
                } else if (responseData.equals("incorrect_password")) {
                    Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                } else if (responseData.equals("network_error")) {
                    Toast.makeText(LoginActivity.this, "网络错误，请稍后重试", Toast.LENGTH_SHORT).show();
                } else {
                    Log.i(TAG, "run: some error occured!!!");
                    Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    private void getToken(final String userID) {
        String url = "http://" + this.getString(R.string.server_ip) + ":8080/getToken?userID=" + userID;
        Log.i(TAG, "postId: 开始请求token");
        HttpUtil.getDataAsync(url, this, new Runnable() {
            @Override
            public void run() {
                final String responseData = HttpUtil.getResponseData();
                Log.i(TAG, "onResponse: 返回token：" + responseData);
                Gson gson = new Gson();
                userToken = gson.fromJson(responseData, new TypeToken<UserToken>() {
                }.getType());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG, "responseData" + responseData + ", token:" + userToken.getToken());
//                        Toast.makeText(LoginActivity.this, "responseData" + responseData + ", token:" + userToken.getToken(), Toast.LENGTH_SHORT).show();
                        Constant.setCurrToken(userToken.getToken());
                        connectRongCloud();
                    }
                });

            }
        });
    }

    private void connectRongCloud() {
        Log.i(TAG, "connectRongCloud: 连接服务器");
        if (getApplicationInfo().packageName.equals(App.getCurProcessName(getApplicationContext()))) {
            RongIM.connect(userToken.getToken(), new RongIMClient.ConnectCallback() {
                @Override
                public void onTokenIncorrect() {
                }

                @Override
                public void onSuccess(String userID) {
                    //userID，是我们在申请token时填入的userID
                    System.out.println("========userID" + userID);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.i(TAG, "connectRongCloud: 连接服务器成功");
                            Constant.setCurrToken(userToken.getToken());
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    });
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                }
            });
        }
    }

    @OnClick(R.id.sign_up_button)
    public void signUp() {
        Intent intent = new Intent(LoginActivity.this, RegisterNameActivity.class);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 2000) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            firstTime = secondTime;
        } else {
            finish();
        }
    }


}

