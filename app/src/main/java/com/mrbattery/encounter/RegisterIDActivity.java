package com.mrbattery.encounter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.mrbattery.encounter.constant.Constant;
import com.mrbattery.encounter.entity.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.makeramen.roundedimageview.RoundedDrawable.TAG;

public class RegisterIDActivity extends AppCompatActivity {

    @BindView(R.id.tv_welcome)
    TextView tvWelcome;
    @BindView(R.id.tv_user_id)
    TextView tvUserID;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_id);
        //接受上一个Activity传来的数据
        user = (User) getIntent().getSerializableExtra("user");
        Log.i(TAG, "onCreate: "+user.getUserName());
        Log.i(TAG, "onCreate: "+user.getUserID());
        ButterKnife.bind(this);

        tvWelcome.setText(user.getUserName() + "，欢迎！");
        tvUserID.setText(String.valueOf(user.getUserID()));
    }

    @OnClick(R.id.encounter_button)
    public void encounter() {
        Constant.setCurrUserID(user.getUserID());
        Intent intent = new Intent(RegisterIDActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
