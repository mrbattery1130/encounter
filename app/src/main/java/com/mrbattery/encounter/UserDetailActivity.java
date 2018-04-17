package com.mrbattery.encounter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.mrbattery.encounter.constant.Constant;
import com.mrbattery.encounter.entity.User;
import com.mrbattery.encounter.util.AnimationUtil;
import com.mrbattery.encounter.util.HttpUtil;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.makeramen.roundedimageview.RoundedDrawable.TAG;

public class UserDetailActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tv_user_id)
    TextView tvUserID;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_gender)
    TextView tvGender;
    @BindView(R.id.tv_constellation)
    TextView tvConstellation;
    @BindView(R.id.tv_script)
    TextView tvScript;

    @BindView(R.id.iv_avatar)
    RoundedImageView ivAvatar;
    @BindView(R.id.iv_cover)
    ImageView ivCover;

    Context context;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        context = this;

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        //接受上一个Activity传来的数据
        user = (User) getIntent().getSerializableExtra("user");
        Constant.setCurrConstellation(user.getConstellation());

        String url = "http://" + this.getString(R.string.server_ip) + ":8080/get_profile?userID=" + user.getUserID();
        Log.i(TAG, url);
        HttpUtil.getDataAsync(url, this, new Runnable() {
            @Override
            public void run() {
                user = HttpUtil.parseJSONWithGSON();
                //个人信息
                tvUserID.setText(String.valueOf(user.getUserID()));
                tvUserName.setText(user.getUserName());
                tvConstellation.setText(Constant.getConstellationName(user.getConstellation()));
                tvScript.setText(user.getScript());
                tvGender.setText(Constant.getGender(user.getGender()));
                //加载头像图片资源
                String avatarUrl = user.getAvatar();
                Picasso.get().load(avatarUrl).into(ivAvatar);
                /*HttpUtil.getPictureAsync(avatarUrl, context, new Runnable() {
                    @Override
                    public void run() {
                        Bitmap avatarBmp = HttpUtil.getResponseBmp();
                        ivAvatar.setImageBitmap(avatarBmp);
                        AnimationUtil.startAlphaAnimation(ivAvatar);
                    }
                });*/
                //加载封面图片资源
                String coverUrl = user.getCover();
                Picasso.get().load(coverUrl).into(ivCover);
                /*HttpUtil.getPictureAsync(coverUrl, context, new Runnable() {
                    @Override
                    public void run() {
                        Bitmap coverBmp = HttpUtil.getResponseBmp();
                        ivCover.setImageBitmap(coverBmp);
                        AnimationUtil.startAlphaAnimation(ivCover);
                    }
                });*/
            }
        });


    }
}
