package com.mrbattery.encounter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mrbattery.encounter.constant.Constant;
import com.mrbattery.encounter.customView.KeywordTextView;
import com.mrbattery.encounter.entity.Keyword;
import com.mrbattery.encounter.util.HttpUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.makeramen.roundedimageview.RoundedImageView.TAG;
import static com.mrbattery.encounter.constant.Constant.getCurrUserID;
import static com.mrbattery.encounter.constant.API.getSERVER_IP;
import static com.mrbattery.encounter.util.UnitConversionUtil.dp2px;

public class EditKeywordActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private int[] keywordIDSelected;

    @BindView(R.id.flexbox)
    FlexboxLayout flexboxLayout;

    @BindView(R.id.iv_okay)
    ImageView ivOkay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_keyword);
        ButterKnife.bind(this);
        ivOkay.setVisibility(View.GONE);
        getKeywordSelected();
    }

    private void getKeywordSelected() {
        String url = "http://" + getSERVER_IP() + ":8080/get_keyword?userID=" + Constant.getCurrUserID();
        Log.i(TAG, url);
        final HttpUtil httpUtil = new HttpUtil();
        httpUtil.getDataAsync(url, this, new Runnable() {
            @Override
            public void run() {
                String responseData = httpUtil.getResponseData();
                Gson gson = new Gson();
                if (responseData == "[]") {

                } else if (responseData.equals("network_error")) {
                    Log.i(TAG, "loadKeyword: 网络错误");
                    Toast.makeText(EditKeywordActivity.this, "网络错误，无法加载标签", Toast.LENGTH_SHORT).show();
                } else {
                    ArrayList<Keyword> keywordSelected = gson.fromJson(responseData, new TypeToken<ArrayList<Keyword>>() {
                    }.getType());
                    int i = 0;
                    keywordIDSelected = new int[keywordSelected.size()];
                    for (Keyword k : keywordSelected) {
                        keywordIDSelected[i] = k.getKeywordID();
                        Log.i(TAG, "run: 第" + i + "个已选关键词：" + k.getKeywordName() + keywordIDSelected[i]);
                        i++;
                    }
                    loadKeyword();
                }
            }
        });
    }

    private void loadKeyword() {
        //加载关键词
        buildProgressDialog(R.string.notice_loading_data);
        String url = "http://" + getSERVER_IP() + ":8080/get_keyword_list";
        Log.i(TAG, url);
        final HttpUtil httpUtil = new HttpUtil();
        httpUtil.getDataAsync(url, this, new Runnable() {
            @Override
            public void run() {
                cancelProgressDialog();
                String responseData = httpUtil.getResponseData();
                Gson gson = new Gson();
                if (responseData == "[]") {

                } else if (responseData.equals("network_error")) {
                    Log.i(TAG, "loadKeyword: 网络错误");
                    Toast.makeText(EditKeywordActivity.this, "网络错误，无法获取关键词", Toast.LENGTH_SHORT).show();
                } else {
                    ArrayList<Keyword> keywordList = gson.fromJson(responseData, new TypeToken<ArrayList<Keyword>>() {
                    }.getType());
                    Log.i(TAG, "loadKeyword: 获取关键词成功");
                    int i = 0;
                    for (Keyword keyword : keywordList) {
                        //检查当前关键词是否已选择
                        boolean isSelected = ifContain(keywordIDSelected, keyword.getKeywordID());
                        /*if (Arrays.asList(keywordIDSelected).contains(keyword.getKeywordID())) {
                            isSelected = true;
                        }*/
                        //分析话题类别
                        if (i == 0) {
                            //如果是第一个关键词，开新的话题
                            addTopic(keyword, flexboxLayout.getId());
                            addKeyword(keyword, flexboxLayout.getId(), isSelected);

                        } else if (keyword.getParentTopicID() == keywordList.get(i - 1).getParentTopicID()) {
                            //如果不是第一个关键词而且父级话题和上一个关键词的父级话题一致，在当前话题添加关键词
                            addKeyword(keyword, flexboxLayout.getId(), isSelected);
                        } else {
                            //如果不一致,开新的一个话题
                            addTopic(keyword, flexboxLayout.getId());
                            addKeyword(keyword, flexboxLayout.getId(), isSelected);
                        }
                        Log.i(TAG, "加载Keyword: " + keyword.getKeywordName() + ",选择状态：" + isSelected);
                        i++;
                    }
                    ivOkay.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    //数组是否包含某个值
    private boolean ifContain(int[] arr, int value) {
        for (int i : arr) {
            if (i == value) {
                return true;
            }
        }
        return false;
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


    @OnClick(R.id.iv_okay)
    public void confirm() {
        buildProgressDialog(R.string.notice_posting_data);
        String url = "http://" + getSERVER_IP() + ":8080/post_keyword?userID=" + getCurrUserID();
        for (int i = 0; i < flexboxLayout.getChildCount(); i++) {
            if (flexboxLayout.getChildAt(i).isSelected()) {
                url = url + "&keyword=" + ((KeywordTextView) flexboxLayout.getChildAt(i)).getKeyword().getKeywordID();
            }
        }
        Log.i(TAG, url);
        final HttpUtil httpUtil = new HttpUtil();
        httpUtil.getDataAsync(url, this, new Runnable() {
            @Override
            public void run() {
                cancelProgressDialog();
                if (httpUtil.getResponseData().equals("success")) {
                    finish();
                    Toast.makeText(EditKeywordActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(EditKeywordActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @SuppressLint("ResourceType")
    private void addKeyword(Keyword keyword, int containerId, boolean isSelected) {
        FlexboxLayout flexboxLayout = findViewById(containerId);
        KeywordTextView keywordTextView = new KeywordTextView(this, keyword);
        keywordTextView.setSelected(isSelected);
        flexboxLayout.addView(keywordTextView);
        //通过FlexboxLayout.LayoutParams 设置子元素支持的属性
        ViewGroup.LayoutParams params = keywordTextView.getLayoutParams();
        if (params instanceof FlexboxLayout.LayoutParams) {
            FlexboxLayout.LayoutParams layoutParams = (FlexboxLayout.LayoutParams) params;
            layoutParams.setMargins(dp2px(getApplicationContext(), 4),
                    dp2px(getApplicationContext(), 4),
                    dp2px(getApplicationContext(), 4),
                    dp2px(getApplicationContext(), 4));
            layoutParams.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
            keywordTextView.setLayoutParams(layoutParams);
        }
    }

    @SuppressLint("ResourceType")
    private void addTopic(Keyword keyword, int containerId) {
        FlexboxLayout flexboxLayout = findViewById(containerId);
        TextView topicTextView = new TextView(this);
        topicTextView.setText(keyword.getParentTopicName());
        topicTextView.setPadding(dp2px(getApplicationContext(), 4),
                dp2px(getApplicationContext(), 16), 0, 0);
        topicTextView.setTextColor(getResources().getColor(R.color.main_color_dark));
        topicTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_small));
        topicTextView.setClickable(false);
        topicTextView.setSelected(false);
        flexboxLayout.addView(topicTextView);
        //通过FlexboxLayout.LayoutParams 设置子元素支持的属性
        ViewGroup.LayoutParams params = topicTextView.getLayoutParams();
        if (params instanceof FlexboxLayout.LayoutParams) {
            FlexboxLayout.LayoutParams layoutParams = (FlexboxLayout.LayoutParams) params;
            layoutParams.setWrapBefore(true);
            layoutParams.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            topicTextView.setLayoutParams(layoutParams);
        }
    }

    /**
     * 加载框
     */
    public void buildProgressDialog(int id) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(EditKeywordActivity.this);
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
