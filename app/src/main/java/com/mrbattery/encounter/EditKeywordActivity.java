package com.mrbattery.encounter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mrbattery.encounter.constant.Constant;
import com.mrbattery.encounter.entity.Keyword;
import com.mrbattery.encounter.util.HttpUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.makeramen.roundedimageview.RoundedImageView.TAG;
import static com.mrbattery.encounter.constant.Constant.getSERVER_IP;

public class EditKeywordActivity extends AppCompatActivity {

    @BindView(R.id.tv_keywords)
    TextView tvKeywords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_keyword);
        ButterKnife.bind(this);
        loadKeyword();
    }

    private void loadKeyword() {
        String url = "http://" + getSERVER_IP() + ":8080/get_keyword_list";
        Log.i(TAG, url);
        HttpUtil.getDataAsync(url, this, new Runnable() {
            @Override
            public void run() {
                String responseData = HttpUtil.getResponseData();
                Gson gson = new Gson();
                if (responseData == "[]") {

                } else if (responseData.equals("network_error")) {
                    Log.i(TAG, "loadKeyword: 网络错误");
                    Toast.makeText(EditKeywordActivity.this, "网络错误，无法加载标签", Toast.LENGTH_SHORT).show();
                } else {
                    ArrayList<Keyword> keywordList = gson.fromJson(responseData, new TypeToken<ArrayList<Keyword>>() {
                    }.getType());
                    for (Keyword keyword : keywordList) {
                        Log.i(TAG, "parseJSONListWithGSON: " + keyword.toString());
                    }
                    int i = 0;
                    String keywordDisplay = "";
                    for (Keyword keyword : keywordList) {
                        if (i == 0) {
                            //如果是第一个标签
                            keywordDisplay = keyword.getParentTopicName() + ": " + keyword.getKeywordName();
                            Log.i(TAG, "loadKeyword " + i + "============\n" + keyword.toString());
                        } else if (keyword.getParentTopicID() == keywordList.get(i - 1).getParentTopicID()) {
                            //如果不是第一个标签而且父级话题和上一个标签的父级话题一致
                            keywordDisplay = keywordDisplay + ", " + keyword.getKeywordName();
                        } else {
                            //如果不一致
                            keywordDisplay = keywordDisplay + "\n" + keyword.getParentTopicName() + ": " + keyword.getKeywordName();
                        }
                        i++;
                        Log.i(TAG, "run: keywordDisplay:" + keywordDisplay);
                    }
                    tvKeywords.setText(keywordDisplay);
                }
            }
        });
    }


    @OnClick(R.id.iv_close)
    public void close() {
        finish();
    }

}
