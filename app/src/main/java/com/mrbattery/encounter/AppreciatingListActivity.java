package com.mrbattery.encounter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.makeramen.roundedimageview.RoundedDrawable;
import com.mrbattery.encounter.adapter.UserListAdapter;
import com.mrbattery.encounter.constant.Constant;
import com.mrbattery.encounter.entity.MatchedUser;
import com.mrbattery.encounter.entity.User;
import com.mrbattery.encounter.util.HttpUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.ContentValues.TAG;
import static android.widget.LinearLayout.VERTICAL;
import static com.mrbattery.encounter.constant.API.getSERVER_IP;

public class AppreciatingListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private UserListAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.swipe_refresh_widget)
    SwipeRefreshLayout swipeRefreshWidget;
    @BindView(R.id.appreciating_container)
    RecyclerView recyclerView;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appreciating_list);
        context = this;
        //绑定ButterKnife
        ButterKnife.bind(this);
        //设置Toolbar为Actionbar
        setSupportActionBar(toolbar);
        //初始化布局管理器
        layoutManager = new LinearLayoutManager(this, VERTICAL, false);
        //设置 进度条的颜色变化，最多可以设置4种颜色
        swipeRefreshWidget.setColorScheme(R.color.main_color);
        swipeRefreshWidget.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
//                        Toast.makeText(getContext(), "刷新完成", Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "run: 刷新完成");
                        swipeRefreshWidget.setRefreshing(false);
                    }
                }, 6000);
            }

        });
        loadData();
    }

    private void loadData() {
        final String url = "http://" + getSERVER_IP() + ":8080/get_appreciating_list?userID=" + Constant.getCurrUserID();
        Log.i(TAG, "loadData: " + url);
        final HttpUtil httpUtil = new HttpUtil();
        httpUtil.getDataAsync(url, this, new Runnable() {
            @Override
            public void run() {
                String responseData = httpUtil.getResponseData();
                Gson gson = new Gson();
                ArrayList<User> users = gson.fromJson(responseData, new TypeToken<ArrayList<User>>() {
                }.getType());
                for (User user : users) {
                    Log.i(TAG, "\nrun: " + user.toString());
                }
                //初始化adapter
                adapter = new UserListAdapter(users, context);
                //设置点击事件
                adapter.setOnItemClickListener(new UserListAdapter.OnItemClickListener() {
                    @Override
                    public void onClick(int position) {
                        Log.i("itemViewOnClick", "onClick:点击了用户： " + adapter.getmData().get(position).toString());
                        Log.i("itemViewOnClick", "onClick: position = " + position);
                        Intent intent = new Intent(getApplicationContext(), UserDetailActivity.class);
                        //用Bundle携带数据
                        Bundle bundle = new Bundle();
                        //传递user对象
                        bundle.putSerializable("user", adapter.getmData().get(position));
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }

                    @Override
                    public void onLongClick(int position) {

                    }
                });
                //设置布局管理器
                recyclerView.setLayoutManager(layoutManager);
                //设置adapter
                recyclerView.setAdapter(adapter);
                //停止刷新
                swipeRefreshWidget.setRefreshing(false);
            }
        });
    }

    @Override
    public void onRefresh() {
        loadData();
    }

    @OnClick(R.id.iv_back)
    public void goBack() {
        finish();
    }
}
