package com.mrbattery.encounter;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mrbattery.encounter.constant.Constant;
import com.mrbattery.encounter.entity.User;
import com.mrbattery.encounter.entity.UserToken;
import com.mrbattery.encounter.util.HttpUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

import static com.makeramen.roundedimageview.RoundedDrawable.TAG;
import static com.mrbattery.encounter.constant.Constant.getSERVER_IP;

public class RegisterPasswordActivity extends AppCompatActivity {

    private String userName;
    private String password;

    @BindView(R.id.tv_password)
    EditText tvPassword;
    @BindView(R.id.tv_password2)
    EditText tvPassword2;
    @BindView(R.id.tv_input_error)
    TextView tvInpueError;
    private UserToken userToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_password);
        ButterKnife.bind(this);
        //接受上一个Activity传来的数据
        userName = String.valueOf(getIntent().getSerializableExtra("userName"));
    }

    @OnClick(R.id.sign_up_button)
    public void register() {
        if (checkInputValid()) {//如果输入正确
            //将昵称编码
            try {
                userName = URLEncoder.encode(userName, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            String url = "http://" + getSERVER_IP() + ":8080/register?" +
                    "userName=" + userName + "&password=" + password;
            Log.i(TAG, "register: " + url);
            HttpUtil.getDataAsync(url, this, new Runnable() {
                @Override
                public void run() {
                    String responseData = HttpUtil.getResponseData();
                    if (responseData != null) {
                        Gson gson = new Gson();
                        User user = gson.fromJson(responseData, new TypeToken<User>() {
                        }.getType());
                        getToken(String.valueOf(user.getUserID()));
                        Intent intent = new Intent(RegisterPasswordActivity.this, RegisterIDActivity.class);
                        //用Bundle携带数据
                        Bundle bundle = new Bundle();
                        //传递user对象
                        bundle.putSerializable("user", user);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else {
                        Toast.makeText(RegisterPasswordActivity.this, "服务器返回空", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

    private boolean checkInputValid() {
        if (tvPassword.getText() == null || tvPassword2.getText() == null) {//如果输入为空
//            Toast.makeText(this, R.string.error_field_required, Toast.LENGTH_SHORT).show();
            tvInpueError.setText(R.string.error_field_required);
            return false;
        } else if (String.valueOf(tvPassword.getText()).equals(String.valueOf(tvPassword2.getText()))) {//如果两次密码一致
            password = String.valueOf(tvPassword.getText());
            return true;
        } else {//如果两次密码不一致
//            Toast.makeText(this, R.string.error_mismatching_password, Toast.LENGTH_SHORT).show();
            tvInpueError.setText(R.string.error_mismatching_password);
            return false;
        }
    }


    @OnClick(R.id.cancel_button)
    public void cancel() {
        finish();
    }

    private void getToken(String userID) {
        String url = "http://" + getSERVER_IP() + ":8080/getToken?userID=" + userID;
        Log.i(ContentValues.TAG, "postId: 开始请求token");
        HttpUtil.getDataAsync(url, this, new Runnable() {
            @Override
            public void run() {
                String responseData = HttpUtil.getResponseData();
                Log.i(ContentValues.TAG, "onResponse: 返回token：" + responseData);
                Gson gson = new Gson();
                userToken = gson.fromJson(responseData, new TypeToken<UserToken>() {
                }.getType());
                Log.i(ContentValues.TAG, "responseData" + responseData + ", token:" + userToken.getToken());
                Constant.setCurrToken(userToken.getToken());
                connectRongCloud();
            }
        });
    }


    private void connectRongCloud() {
        String packageName = getApplicationInfo().packageName;
        String context = App.getCurProcessName(getApplicationContext());
        Log.i(ContentValues.TAG, "connectRongCloud: 连接服务器");
        if (packageName.equals(context)) {
            Log.i(ContentValues.TAG, "connectRongCloud: 验证包名一致！！！！！！！！！！！！！！！！！！！" +
                    "\ngetApplicationInfo().packageName=========" + packageName +
                    "\nApp.getCurProcessName(getApplicationContext())==========" + context);

            RongIM.connect(Constant.getCurrToken(), new RongIMClient.ConnectCallback() {
                @Override
                public void onTokenIncorrect() {
                    Log.i(ContentValues.TAG, "onTokenIncorrect: Token不正确");
                }

                @Override
                public void onSuccess(String userID) {
                    //userID，是我们在申请token时填入的userID
                    System.out.println("========userID" + userID);
                    Log.i(ContentValues.TAG, "connectRongCloud: 连接服务器成功");
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    Log.i(ContentValues.TAG, "onError: 连接融云出错");
                }
            });
        } else {
            Log.i(ContentValues.TAG, "connectRongCloud: 包名不一致！！！！！！！！！！！！！！！！！！！" +
                    "\nAgetApplicationInfo().packageName=========" + packageName +
                    "\nApp.getCurProcessName(getApplicationContext())==========" + context);
        }
    }
}
