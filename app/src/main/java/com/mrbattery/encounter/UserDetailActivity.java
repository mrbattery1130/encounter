package com.mrbattery.encounter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
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
import static com.mrbattery.encounter.constant.API.getSERVER_IP;

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

    @BindView(R.id.tv_appreciate)
    TextView tvAppreciate;

    Context context;

    private ProgressDialog progressDialog;
    private boolean profileLoaded;
    private boolean keywordLoaded;
    private boolean appreciateLoaded;

    //当前页面对应的用户
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

        profileLoaded = false;
        keywordLoaded = false;
        appreciateLoaded = false;

        buildProgressDialog(R.string.notice_loading_data);
        loadProfile();
        loadKeyword();
        loadAppreciate();
    }

    private void loadProfile() {
        String url = "http://" + getSERVER_IP() + ":8080/get_profile?userID=" + user.getUserID();
        Log.i(TAG, url);
        final HttpUtil httpUtil = new HttpUtil();
        httpUtil.getDataAsync(url, this, new Runnable() {
            @Override
            public void run() {
                String responseData = httpUtil.getResponseData();
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

                profileLoaded = true;
                if (keywordLoaded && keywordLoaded) {
                    cancelProgressDialog();
                }
            }
        });
    }

    private void loadAppreciate() {
        String url = "http://" + getSERVER_IP() + ":8080/get_appreciating?userID=" + Constant.getCurrUserID() + "&targetUserID=" + user.getUserID();
        Log.i(RoundedImageView.TAG, url);
        final HttpUtil httpUtil = new HttpUtil();
        httpUtil.getDataAsync(url, this, new Runnable() {
            @Override
            public void run() {
                String responseData = httpUtil.getResponseData();
                switch (responseData) {
                    case "true":
                        tvAppreciate.setSelected(false);
                        tvAppreciate.setText("已欣赏");
                        Log.i(TAG, "run: 已经欣赏啦！！！按钮不可用就好啦");
                        break;
                    case "false":
                        tvAppreciate.setSelected(true);
                        tvAppreciate.setText("欣赏TA");
                        Log.i(TAG, "run: 还没欣赏呐！！！按钮可用呐");
                        break;
                    default:
                        break;
                }

                appreciateLoaded = true;
                if (profileLoaded && keywordLoaded) {
                    cancelProgressDialog();
                }
            }
        });

    }

    private void loadKeyword() {
        String url = "http://" + getSERVER_IP() + ":8080/get_keyword?userID=" + user.getUserID();
        Log.i(RoundedImageView.TAG, url);
        final HttpUtil httpUtil = new HttpUtil();
        httpUtil.getDataAsync(url, this, new Runnable() {
            @Override
            public void run() {
                String responseData = httpUtil.getResponseData();
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
                    if (keywordDisplay.equals("")) {
                        keywordDisplay = getString(R.string.error_no_keyword);
                    }
                    tvKeywords.setText(keywordDisplay);
                }

                keywordLoaded = true;
                if (profileLoaded && appreciateLoaded) {
                    cancelProgressDialog();
                }
            }
        });
    }


    @OnClick(R.id.btn_chat)
    public void chat() {
        RongIM.getInstance().startConversation(this, Conversation.ConversationType.PRIVATE, String.valueOf(user.getUserID()), user.getUserName());
    }

    @OnClick(R.id.tv_appreciate)
    public void handleAppreciate() {
        if (tvAppreciate.isSelected()) {
            //选中状态说明还没有欣赏，用户想要欣赏
            appreciate();
        } else {
            //未选中状态说明已经欣赏，用户想要取消欣赏
            new AlertDialog.Builder(context)
                    .setIcon(R.drawable.icon_encounter)
                    .setTitle("确定不再欣赏" + user.getUserName() + "吗？")
//                .setMessage("不保留更改吗？")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            unappreciate();
                        }
                    }).show();
        }
    }

    public void appreciate() {
        String url = "http://" + getSERVER_IP() + ":8080/appreciate?userID=" + Constant.getCurrUserID() + "&targetUserID=" + user.getUserID();
        Log.i(TAG, "appreciate: url: " + url);
        final HttpUtil httpUtil = new HttpUtil();
        httpUtil.getDataAsync(url, context, new Runnable() {
            @Override
            public void run() {
                String responseData = httpUtil.getResponseData();
                switch (responseData) {
                    case "1":
                        Log.i(TAG, "run: 欣赏成功");
                        Toast.makeText(context, "欣赏成功", Toast.LENGTH_SHORT).show();
                        break;
                    case "0":
                        Log.i(TAG, "run: 欣赏失败");
                        Toast.makeText(context, "欣赏失败", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                loadAppreciate();
            }
        });

    }

    public void unappreciate() {
        String url = "http://" + getSERVER_IP() + ":8080/unappreciate?userID=" + Constant.getCurrUserID() + "&targetUserID=" + user.getUserID();
        Log.i(TAG, "appreciate: url: " + url);
        final HttpUtil httpUtil = new HttpUtil();
        httpUtil.getDataAsync(url, this, new Runnable() {
            @Override
            public void run() {
                String responseData = httpUtil.getResponseData();
                switch (responseData) {
                    case "1":
                        Log.i(TAG, "run: 取消欣赏成功");
                        Toast.makeText(context, "取消欣赏成功", Toast.LENGTH_SHORT).show();
                        break;
                    case "0":
                        Log.i(TAG, "run: 未欣赏该用户，不能取消欣赏");
                        Toast.makeText(context, "未欣赏该用户，不能取消欣赏", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                loadAppreciate();
            }
        });

    }

    @OnClick(R.id.iv_back)
    public void goBack() {
        finish();
    }


    /**
     * 加载框
     */
    public void buildProgressDialog(int id) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(UserDetailActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        }
        progressDialog.setMessage(getString(id));
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    /**
     * @Description: TODO 取消加载框
     */
    public void cancelProgressDialog() {
        if (progressDialog != null)
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
    }


}
