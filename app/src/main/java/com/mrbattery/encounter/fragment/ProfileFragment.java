package com.mrbattery.encounter.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mrbattery.encounter.Constant;
import com.mrbattery.encounter.EditProfileActivity;
import com.mrbattery.encounter.LoginActivity;
import com.mrbattery.encounter.MainActivity;
import com.mrbattery.encounter.R;
import com.mrbattery.encounter.entity.User;
import com.mrbattery.encounter.util.HttpUtil;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.makeramen.roundedimageview.RoundedDrawable.TAG;

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
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        loadProfile();

        // Inflate the layout for this fragment
        return view;
    }

    private void loadProfile() {
        String url = "http://" + this.getString(R.string.server_ip) + ":8080/get_profile?userID=" + Constant.getCurrUserID();
        Log.i(TAG, url);
        HttpUtil.getDataAsync(url, getContext(), new Runnable() {
            @Override
            public void run() {
                user = HttpUtil.parseJSONWithGSON();
                tvUserID.setText(String.valueOf(user.getUserID()));
                tvUserName.setText(user.getUserName());
                tvConstellation.setText(Constant.getConstellationName(user.getConstellation()));
                tvScript.setText(user.getScript());
                ivGender.setImageResource(Constant.getGenderSrc(user.getGender()));
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
