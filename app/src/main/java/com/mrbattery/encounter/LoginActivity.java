package com.mrbattery.encounter;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mrbattery.encounter.util.HttpUtil;
import com.mrbattery.encounter.util.ToastUtil;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.makeramen.roundedimageview.RoundedDrawable.TAG;

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
    private  String userID;

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
        HttpUtil.getDataAsync(url, this, new Runnable() {
            @Override
            public void run() {
                String responseData = HttpUtil.getResponseData();
                //隐藏加载进度条和遮罩
                loginProgress.setVisibility(View.INVISIBLE);
                loginOverlay.setVisibility(View.INVISIBLE);
                loginOverlay.setClickable(false);
                if (responseData.equals("success")) {
                    Constant.setCurrUserID(userID);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } else if (responseData.equals("invalid_user_id")) {
                    Toast.makeText(LoginActivity.this, "用户不存在", Toast.LENGTH_SHORT).show();
                } else if (responseData.equals("incorrect_password")){
                    Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @OnClick(R.id.sign_up_button)
    public void signUp(){
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

