package com.mrbattery.encounter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mrbattery.encounter.entity.User;
import com.mrbattery.encounter.util.HttpUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.makeramen.roundedimageview.RoundedDrawable.TAG;

public class RegisterPasswordActivity extends AppCompatActivity {

    private String userName;
    private String password;

    @BindView(R.id.tv_password)
    EditText tvPassword;
    @BindView(R.id.tv_password2)
    EditText tvPassword2;
    @BindView(R.id.tv_input_error)
    TextView tvInpueError;

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
        if (checkInputCorrect()) {//如果输入正确
            //将昵称编码
            try {
                userName = URLEncoder.encode(userName, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            String url = "http://" + this.getString(R.string.server_ip) + ":8080/register?" +
                    "userName=" + userName + "&password=" + password;
            Log.i(TAG, "register: " + url);

            HttpUtil.getDataAsync(url, this, new Runnable() {
                @Override
                public void run() {
                    if (HttpUtil.getResponseData() != null) {
                        User user = HttpUtil.parseJSONWithGSON();
                        Constant.setCurrUserID(user.getUserID());
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

    private boolean checkInputCorrect() {
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
}
