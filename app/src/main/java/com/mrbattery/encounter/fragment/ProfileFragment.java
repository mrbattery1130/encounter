package com.mrbattery.encounter.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mrbattery.encounter.EPQActivity;
import com.mrbattery.encounter.EditKeywordActivity;
import com.mrbattery.encounter.constant.Constant;
import com.mrbattery.encounter.EditProfileActivity;
import com.mrbattery.encounter.R;
import com.mrbattery.encounter.constant.EPQTest;
import com.mrbattery.encounter.entity.Keyword;
import com.mrbattery.encounter.entity.User;
import com.mrbattery.encounter.util.HttpUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.makeramen.roundedimageview.RoundedImageView.TAG;
import static com.mrbattery.encounter.constant.Constant.getSERVER_IP;

public class ProfileFragment extends Fragment {


    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tv_user_id)
    TextView tvUserID;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.iv_gender)
    ImageView ivGender;
    @BindView(R.id.tv_constellation)
    TextView tvConstellation;
    @BindView(R.id.tv_script)
    TextView tvScript;

    @BindView(R.id.iv_avatar)
    RoundedImageView ivAvatar;
    @BindView(R.id.iv_cover)
    ImageView ivCover;

    @BindView(R.id.tv_e_score)
    TextView tvEScore;
    @BindView(R.id.tv_n_score)
    TextView tvNScore;
    @BindView(R.id.tv_p_score)
    TextView tvPScore;

    @BindView(R.id.tv_result_analyse)
    TextView tvResultAnalyse;

    @BindView(R.id.tv_keywords)
    TextView tvKeywords;

    private Unbinder unbinder;
    User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //ButterKnife绑定fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        unbinder = ButterKnife.bind(this, view);

        //toolbar代替actionbar
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        loadProfile();

        // Inflate the layout for this fragment
        return view;
    }

    private void loadProfile() {
        String url = "http://" + getSERVER_IP() + ":8080/get_profile?userID=" + Constant.getCurrUserID();
        Log.i(TAG, url);
        HttpUtil.getDataAsync(url, getActivity(), new Runnable() {
            @Override
            public void run() {
                String responseData = HttpUtil.getResponseData();
                Gson gson = new Gson();
                user = gson.fromJson(responseData, new TypeToken<User>() {
                }.getType());
                //个人信息卡片
                tvUserID.setText(String.valueOf(user.getUserID()));
                tvUserName.setText(user.getUserName());
                tvConstellation.setText(Constant.getConstellationName(user.getConstellation()));
                tvScript.setText(user.getScript());
                ivGender.setImageResource(Constant.getGenderSrc(user.getGender()));
                //EPQ测试卡片
                tvEScore.setText(String.valueOf((int) user.geteScore()));
                tvNScore.setText(String.valueOf((int) user.getnScore()));
                tvPScore.setText(String.valueOf((int) user.getpScore()));
                tvResultAnalyse.setText(EPQTest.resultAnalyse(user.geteScore(), user.getnScore(), user.getpScore(), user.getlScore()));

                //加载头像图片资源
                String avatarUrl = user.getAvatar();
                Picasso.get().load(avatarUrl).into(ivAvatar);

                //加载封面图片资源
                String coverUrl = user.getCover();
                Picasso.get().load(coverUrl).into(ivCover);

                //加载标签
                loadKeyword();
            }
        });


    }

    private void loadKeyword() {
        String url = "http://" + getSERVER_IP() + ":8080/get_keyword?userID=" + Constant.getCurrUserID();
        Log.i(TAG, url);
        HttpUtil.getDataAsync(url, getActivity(), new Runnable() {
            @Override
            public void run() {
                String responseData = HttpUtil.getResponseData();
                Gson gson = new Gson();
                if (responseData == "[]") {

                } else if (responseData.equals("network_error")) {
                    Log.i(TAG, "loadKeyword: 网络错误");
                    Toast.makeText(getContext(), "网络错误，无法加载标签", Toast.LENGTH_SHORT).show();
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


    @OnClick(R.id.cv_profile)
    public void editProfile() {
        Intent intent = new Intent(getContext(), EditProfileActivity.class);
        //用Bundle携带数据
        Bundle bundle = new Bundle();
        //传递user对象
        bundle.putSerializable("user", user);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @OnClick(R.id.cv_epq)
    public void startEPQ() {
        Intent intent = new Intent(getContext(), EPQActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.cv_keyword)
    public void editKeyword(){
        Intent intent = new Intent(getContext(), EditKeywordActivity.class);
        startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.e(TAG, "onCreateOptionsMenu()");
        menu.clear();
        inflater.inflate(R.menu.menu_parent_fragment, menu);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override       //这里是实现了自动更新
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        loadProfile();
    }


}
