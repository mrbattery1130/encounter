package com.mrbattery.encounter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mrbattery.encounter.constant.Constant;
import com.mrbattery.encounter.entity.Keyword;
import com.mrbattery.encounter.entity.User;
import com.mrbattery.encounter.util.HttpUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

import static com.makeramen.roundedimageview.RoundedDrawable.TAG;
import static com.mrbattery.encounter.constant.Constant.getSERVER_IP;

public class UserDetailActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tv_user_id)
    TextView tvUserID;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_user_name2)
    TextView tvUserName2;
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

    @BindView(R.id.tv_keywords)
    TextView tvKeywords;

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

        String url = "http://" + getSERVER_IP() + ":8080/get_profile?userID=" + user.getUserID();
        Log.i(TAG, url);
        HttpUtil.getDataAsync(url, this, new Runnable() {
            @Override
            public void run() {
                String responseData = HttpUtil.getResponseData();
                Gson gson = new Gson();
                user = gson.fromJson(responseData, new TypeToken<User>() {
                }.getType());
                //个人信息
                tvUserID.setText(String.valueOf(user.getUserID()));
                tvUserName.setText(user.getUserName());
                tvUserName2.setText(user.getUserName());
                tvConstellation.setText(Constant.getConstellationName(user.getConstellation()));
                tvScript.setText(user.getScript());
                tvGender.setText(Constant.getGender(user.getGender()));
                //加载头像图片资源
                String avatarUrl = user.getAvatar();
                Picasso.get().load(avatarUrl).into(ivAvatar);

                //加载封面图片资源
                String coverUrl = user.getCover();
                Picasso.get().load(coverUrl).into(ivCover);

                loadKeyword(user.getUserID());
            }
        });
    }
    private void loadKeyword(int userID) {
        String url = "http://" + getSERVER_IP() + ":8080/get_keyword?userID=" + userID;
        Log.i(RoundedImageView.TAG, url);
        HttpUtil.getDataAsync(url, this, new Runnable() {
            @Override
            public void run() {
                String responseData = HttpUtil.getResponseData();
                Gson gson = new Gson();
                if (responseData == "[]") {

                } else if (responseData.equals("network_error")) {
                    Log.i(RoundedImageView.TAG, "loadKeyword: 网络错误");
                    Toast.makeText(context, "网络错误，无法加载标签", Toast.LENGTH_SHORT).show();
                } else {
                    ArrayList<Keyword> keywordList = gson.fromJson(responseData, new TypeToken<ArrayList<Keyword>>() {
                    }.getType());
                    for (Keyword keyword : keywordList) {
                        Log.i(RoundedImageView.TAG, "parseJSONListWithGSON: " + keyword.toString());
                    }
                    int i = 0;
                    String keywordDisplay = "";
                    for (Keyword keyword : keywordList) {
                        if (i == 0) {
                            //如果是第一个标签
                            keywordDisplay = keyword.getParentTopicName() + ": " + keyword.getKeywordName();
                            Log.i(RoundedImageView.TAG, "loadKeyword " + i + "============\n" + keyword.toString());
                        } else if (keyword.getParentTopicID() == keywordList.get(i - 1).getParentTopicID()) {
                            //如果不是第一个标签而且父级话题和上一个标签的父级话题一致
                            keywordDisplay = keywordDisplay + ", " + keyword.getKeywordName();
                        } else {
                            //如果不一致
                            keywordDisplay = keywordDisplay + "\n" + keyword.getParentTopicName() + ": " + keyword.getKeywordName();
                        }
                        i++;
                        Log.i(RoundedImageView.TAG, "run: keywordDisplay:" + keywordDisplay);
                    }
                    tvKeywords.setText(keywordDisplay);
                }
            }
        });
    }


    @OnClick(R.id.btn_chat)
    public void chat() {
        RongIM.getInstance().startConversation(this, Conversation.ConversationType.PRIVATE, String.valueOf(user.getUserID()), user.getUserName());
    }
}
