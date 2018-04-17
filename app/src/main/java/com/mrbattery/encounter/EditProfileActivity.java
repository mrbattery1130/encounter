package com.mrbattery.encounter;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedImageView;
import com.mrbattery.encounter.constant.Constant;
import com.mrbattery.encounter.entity.User;
import com.mrbattery.encounter.util.HttpUtil;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.makeramen.roundedimageview.RoundedDrawable.TAG;

public class EditProfileActivity extends AppCompatActivity {

    @BindView(R.id.tv_user_id)
    EditText tvUserID;
    @BindView(R.id.tv_user_name)
    EditText tvUserName;
    @BindView(R.id.tv_constellation)
    EditText tvConstellation;
    @BindView(R.id.tv_script)
    EditText tvScript;
    @BindView(R.id.rb_male)
    RadioButton rbMale;
    @BindView(R.id.rb_female)
    RadioButton rbFemale;
    @BindView(R.id.iv_avatar)
    RoundedImageView ivAvatar;
    @BindView(R.id.iv_cover)
    ImageView ivCover;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        //接受上一个Activity传来的数据
        user = (User) getIntent().getSerializableExtra("user");
        Constant.setCurrConstellation(user.getConstellation());

        initProfile();
    }

    private void initProfile() {
        tvUserID.setText(String.valueOf(user.getUserID()));
        tvUserName.setText(user.getUserName());
        tvConstellation.setText(Constant.getConstellationName(user.getConstellation()));
        tvScript.setText(user.getScript());
        Picasso.get().load(user.getAvatar()).into(ivAvatar);
        Picasso.get().load(user.getCover()).into(ivCover);
        switch (user.getGender()) {
            case 1:
                rbMale.setChecked(true);
                break;
            case 2:
                rbFemale.setChecked(true);
                break;
            default:
                break;
        }
    }

    @OnClick(R.id.iv_okay)
    public void commit() {
        int gender;
        if (rbMale.isChecked()) {
            gender = Constant.MALE;
        } else if (rbFemale.isChecked()) {
            gender = Constant.FEMALE;
        } else {
            gender = Constant.UNKNOWNGENDER;
        }
        String userName = String.valueOf(tvUserName.getText());
        String script = String.valueOf(tvScript.getText());
        try {
            userName = URLEncoder.encode(userName, "utf-8");
            script = URLEncoder.encode(script, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = "http://" + this.getString(R.string.server_ip) + ":8080/post_profile?" +
                "userID=" + tvUserID.getText()
                + "&userName=" + userName
                + "&gender=" + gender
                + "&constellation=" + Constant.getCurrConstellation()
                + "&script=" + script;
        Log.i(TAG, url);

        HttpUtil.getDataAsync(url, this, new Runnable() {
            @Override
            public void run() {
                String responseData = HttpUtil.getResponseData();
                Log.d(TAG, "responseData: " + responseData);
                switch (responseData) {
                    case "success":
                        Toast.makeText(EditProfileActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    case "failure":
                        Toast.makeText(EditProfileActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(EditProfileActivity.this, "未知错误", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });


    }

    @OnClick(R.id.tv_constellation)
    public void chooseConstellation() {
        Intent intent = new Intent(EditProfileActivity.this, ChooseConstellationActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.iv_close)
    public void cancel() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.icon_encounter)
                .setTitle("放弃修改吗？")
//                .setMessage("不保留更改吗？")
                .setNegativeButton("取消", null)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).show();
    }

    @Override
    public void onBackPressed() {
        cancel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        tvConstellation.setText(Constant.getConstellationName(Constant.getCurrConstellation()));
    }
}
