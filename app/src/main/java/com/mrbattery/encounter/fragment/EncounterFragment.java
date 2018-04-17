package com.mrbattery.encounter.fragment;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mrbattery.encounter.R;
import com.mrbattery.encounter.UserDetailActivity;
import com.mrbattery.encounter.recyclerView.EncounterAdapter;
import com.mrbattery.encounter.constant.Constant;
import com.mrbattery.encounter.entity.MatchedUser;
import com.mrbattery.encounter.util.HttpUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.content.ContentValues.TAG;
import static android.widget.LinearLayout.VERTICAL;

public class EncounterFragment extends Fragment {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private Unbinder unbinder;

    @BindView(R.id.recycler_view_container)
    RecyclerView recyclerView;

    @BindView(R.id.swipe_refresh_widget)
    SwipeRefreshLayout swipeRefreshWidget;

    private EncounterAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //ButterKnife绑定fragment
        View view = inflater.inflate(R.layout.fragment_encounter, container, false);
        unbinder = ButterKnife.bind(this, view);

        //toolbar代替actionbar
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.menu_parent_fragment);
        setHasOptionsMenu(true);

        //初始化布局管理器
        layoutManager = new LinearLayoutManager(this.getContext(), VERTICAL, false);

        getData();

        // Inflate the layout for this fragment
        return view;
    }

    private void getData() {
        String url = "http://" + this.getString(R.string.server_ip) + ":8080/get_matching_user?userID=" + Constant.getCurrUserID();
        Log.i(TAG, "getData: " + url);
        HttpUtil.getDataAsync(url, getContext(), new Runnable() {
            @Override
            public void run() {
                ArrayList<MatchedUser> matchedUsers = HttpUtil.parseJSONListWithGSON();
                for (MatchedUser matchedUser : matchedUsers) {
                    Log.i(TAG, "\nrun: " + matchedUser.toString());
                }
                //初始化adapter
                adapter = new EncounterAdapter(matchedUsers, getContext());
                //设置点击事件
                adapter.setOnItemClickListener(new EncounterAdapter.OnItemClickListener() {
                    @Override
                    public void onClick(int position) {
                        Log.i("itemViewOnClick", "onClick:点击了用户： " + adapter.getmData().get(position).toString());
                        Log.i("itemViewOnClick", "onClick: position = " + position);
                        Intent intent = new Intent(getContext(), UserDetailActivity.class);
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

            }
        });
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //设置 进度条的颜色变化，最多可以设置4种颜色
        swipeRefreshWidget.setColorScheme(R.color.main_color_deep, R.color.main_color_dark,
                R.color.main_color, R.color.main_color_light);
        swipeRefreshWidget.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Toast.makeText(getContext(), "刷新完成", Toast.LENGTH_SHORT).show();
                        swipeRefreshWidget.setRefreshing(false);
                    }
                }, 6000);
            }

        });

    }
}
