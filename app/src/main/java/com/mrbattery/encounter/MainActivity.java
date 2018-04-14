package com.mrbattery.encounter;

import android.app.Activity;
import android.app.Fragment;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.mrbattery.encounter.fragment.EncounterFragment;
import com.mrbattery.encounter.fragment.MessageFragment;
import com.mrbattery.encounter.fragment.ProfileFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //fragmet嵌入在这里
    @BindView(R.id.id_main_container)
    public FrameLayout mainFrameLayout;
    //Fragment管理
    private FragmentManager fragmentManager;
    //Fragment类
    private MessageFragment messageFragment;
    private EncounterFragment encounterFragment;
    private ProfileFragment profileFragment;
    //底部3个点击按钮
    @BindView(R.id.iv_message)
    public ImageView ivMessage;
    @BindView(R.id.iv_encounter)
    public ImageView ivEncounter;
    @BindView(R.id.iv_profile)
    public ImageView ivProfile;

    //记录用户首次点击返回键的时间
    private long firstTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //绑定activity
        ButterKnife.bind(this);
        //设置监听器
        ivEncounter.setOnClickListener(this);
        ivMessage.setOnClickListener(this);
        ivProfile.setOnClickListener(this);

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        encounterFragment = new EncounterFragment();
        transaction.add(R.id.id_main_container, encounterFragment);
        transaction.commit();

        ivEncounter.setSelected(true);

    }

    @Override
    public void onClick(View view) {
        //v4包导入getSupportFragmentManager，app包使用getFragmentManager，app包3.0后才使用
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideAllFragment(transaction);
        switch (view.getId()) {
            case R.id.iv_message:
                reIvSelect();
                ivMessage.setSelected(true);
                if (messageFragment == null) {
                    messageFragment = new MessageFragment();
                    transaction.add(R.id.id_main_container, messageFragment);
                } else {
                    transaction.show(messageFragment);
                }
                break;

            case R.id.iv_encounter:
                reIvSelect();
                ivEncounter.setSelected(true);
                if (encounterFragment == null) {
                    encounterFragment = new EncounterFragment();
                    transaction.add(R.id.id_main_container, encounterFragment);
                } else {
                    transaction.show(encounterFragment);
                }
                break;

            case R.id.iv_profile:
                reIvSelect();
                ivProfile.setSelected(true);
                if (profileFragment == null) {
                    profileFragment = new ProfileFragment();
                    transaction.add(R.id.id_main_container, profileFragment);
                } else {
                    transaction.show(profileFragment);
                }
                break;
            default:
                break;
        }
        transaction.commit();
    }

    //初始化底部菜单选择状态
    private void reIvSelect() {
        ivMessage.setSelected(false);
        ivEncounter.setSelected(false);
        ivProfile.setSelected(false);
    }

    //隐藏所有Fragment
    private void hideAllFragment(FragmentTransaction transaction) {
        if (messageFragment != null) {
            transaction.hide(messageFragment);
        }
        if (encounterFragment != null) {
            transaction.hide(encounterFragment);
        }
        if (profileFragment != null) {
            transaction.hide(profileFragment);
        }
    }

    @Override
    public void onBackPressed() {
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 2000) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            firstTime = secondTime;
        } else {
            System.exit(0);
        }
    }
}
