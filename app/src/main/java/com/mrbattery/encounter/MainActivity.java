package com.mrbattery.encounter;

import android.content.Context;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mrbattery.encounter.entity.User;
import com.mrbattery.encounter.fragment.EncounterFragment;
import com.mrbattery.encounter.fragment.ProfileFragment;
import com.mrbattery.encounter.util.DragPointView;
import com.mrbattery.encounter.util.HttpUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imkit.widget.adapter.ConversationListAdapter;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;

import static android.view.View.GONE;
import static com.makeramen.roundedimageview.RoundedDrawable.TAG;
import static com.mrbattery.encounter.constant.API.getSERVER_IP;
import static java.lang.Thread.sleep;

public class MainActivity extends FragmentActivity
        implements View.OnClickListener,
        DragPointView.OnDragListener,
        IUnReadMessageObserver {
    //fragment嵌入在这里
    @BindView(R.id.id_main_container)
    public FrameLayout mainFrameLayout;
    //Fragment管理
    private FragmentManager fragmentManager;
    //Fragment类
    private android.support.v4.app.Fragment messageFragment;
    private EncounterFragment encounterFragment;
    private ProfileFragment profileFragment;
    //底部3个点击按钮
    @BindView(R.id.iv_message)
    public ImageView ivMessage;
    @BindView(R.id.iv_encounter)
    public ImageView ivEncounter;
    @BindView(R.id.iv_profile)
    public ImageView ivProfile;
    //Toolbar
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    //记录用户首次点击返回键的时间
    private long firstTime = 0;

    Context context = this;

    @BindView(R.id.msg_num)
    public DragPointView mUnreadNumView;
    /**
     * 会话列表的fragment
     */
    private ConversationListFragment mConversationListFragment = null;
    private Conversation.ConversationType[] mConversationsTypes = null;


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
        mUnreadNumView.setDragListener(this);

        toolbar.setVisibility(View.VISIBLE);

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        encounterFragment = new EncounterFragment();
        transaction.add(R.id.id_main_container, encounterFragment);
        transaction.commit();

        ivEncounter.setSelected(true);

        initData();

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
//                toolbar.setVisibility(View.VISIBLE);
                toolbar.setTitle(R.string.title_fragment_message);
                if (messageFragment == null) {
                    messageFragment = initConversationList();
                    transaction.add(R.id.id_main_container, messageFragment);
                } else {
                    transaction.show(messageFragment);
                }
                break;

            case R.id.iv_encounter:
                reIvSelect();
                ivEncounter.setSelected(true);
//                toolbar.setVisibility(GONE);
                toolbar.setTitle(R.string.title_fragment_encounter);
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
//                toolbar.setVisibility(GONE);
                toolbar.setTitle(R.string.title_fragment_profile);
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

    private android.support.v4.app.Fragment initConversationList() {
        if (mConversationListFragment == null) {
            ConversationListFragment listFragment = new ConversationListFragment();
            listFragment.setAdapter(new ConversationListAdapter(RongContext.getInstance()));
            Uri uri;
            uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                    .appendPath("conversationlist")
                    .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
                    .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")//群组
                    .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
                    .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//订阅号
                    .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")//系统
                    .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "true")
                    .build();
            mConversationsTypes = new Conversation.ConversationType[]{
                    Conversation.ConversationType.PRIVATE,
                    Conversation.ConversationType.GROUP,
                    Conversation.ConversationType.PUBLIC_SERVICE,
                    Conversation.ConversationType.APP_PUBLIC_SERVICE,
                    Conversation.ConversationType.SYSTEM,
                    Conversation.ConversationType.DISCUSSION
            };

            listFragment.setUri(uri);
            mConversationListFragment = listFragment;
            return listFragment;
        } else {
            return mConversationListFragment;
        }
    }

    protected void initData() {

        final Conversation.ConversationType[] conversationTypes = {
                Conversation.ConversationType.PRIVATE,
                Conversation.ConversationType.GROUP, Conversation.ConversationType.SYSTEM,
                Conversation.ConversationType.PUBLIC_SERVICE, Conversation.ConversationType.APP_PUBLIC_SERVICE
        };

        //未读消息
        RongIM.getInstance().addUnReadMessageCountChangedObserver(this, conversationTypes);

        //用户头像
        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
            String responseData;
            User u;
            UserInfo userInfo;

            @Override
            public io.rong.imlib.model.UserInfo getUserInfo(String userID) {
                final String url = "http://" + getSERVER_IP() + ":8080/get_profile?userID=" + userID;
                Log.i(TAG, "getUserInfo: URL:" + url);
                final HttpUtil httpUtil = new HttpUtil();
                httpUtil.getDataAsync(url, context, new Runnable() {
                    @Override
                    public void run() {
                        responseData = httpUtil.getResponseData();
                        Log.i(TAG, "getUserInfo: responseData:" + responseData);
                        if (responseData == null) {

                        } else if (responseData.equals("network_error")) {

                        } else {
                            Gson gson = new Gson();
                            Log.i(TAG, "getUserInfo: URL:" + url);
                            u = gson.fromJson(responseData, new TypeToken<User>() {
                            }.getType());
                            userInfo = new UserInfo(String.valueOf(u.getUserID()), u.getUserName(), Uri.parse(u.getAvatar()));
                        }
                    }
                });
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (userInfo == null) {
                    return null;
                } else {
                    return userInfo;
                }
            }
        }, true);
        //刷新用户
//        RongIM.getInstance().refreshUserInfoCache(new UserInfo(connectResultId, nickName, Uri.parse(portraitUri)));
    }

    @Override
    public void onDragOut() {
        mUnreadNumView.setVisibility(GONE);
//        Toast.makeText(this, "监听到拖拽", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "onDragOut: 监听到拖拽");
        RongIM.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> conversations) {
                Log.i(TAG, "onDragOut: 开始清理");
                int i = 0;
                if (conversations != null && conversations.size() > 0) {
                    for (Conversation c : conversations) {
                        Log.i(TAG, "onSuccess:开始清理第" + i + "条未读");
                        RongIM.getInstance().clearMessagesUnreadStatus(c.getConversationType(), c.getTargetId(), null);
                        Log.i(TAG, "onSuccess:第" + i + "条未读清理成功");
                        i++;
                    }
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode e) {

            }
        }, mConversationsTypes);

    }

    @Override
    public void onCountChanged(int count) {
        if (count == 0) {
            mUnreadNumView.setVisibility(GONE);
        } else if (count > 0 && count < 100) {
            mUnreadNumView.setVisibility(View.VISIBLE);
            mUnreadNumView.setText(String.valueOf(count));
        } else {
            mUnreadNumView.setVisibility(View.VISIBLE);
            mUnreadNumView.setText("...");
        }
    }
}
