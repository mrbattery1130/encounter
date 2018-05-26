package com.mrbattery.encounter.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedImageView;
import com.mrbattery.encounter.R;
import com.mrbattery.encounter.constant.Constant;
import com.mrbattery.encounter.entity.User;
import com.mrbattery.encounter.util.HttpUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.makeramen.roundedimageview.RoundedDrawable.TAG;
import static com.mrbattery.encounter.constant.API.getSERVER_IP;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {
    private ArrayList<User> mData;
    private Context context;
    private UserListAdapter.ViewHolder viewHolder;
    private UserListAdapter.OnItemClickListener mOnItemClickListener;
    private boolean scrolling;

    public UserListAdapter(ArrayList<User> mData, Context context) {
        this.mData = mData;
        this.context = context;
    }

    public void updateData(ArrayList<User> data) {
        this.mData = data;
        notifyDataSetChanged();
    }


    public void setOnItemClickListener(UserListAdapter.OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_item, parent, false);
        // 实例化viewHolder
        viewHolder = new UserListAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final User user = mData.get(position);
        //绑定数据
        holder.tvUserName.setText(user.getUserName());
        holder.tvScript.setText(user.getScript());
        loadAppreciate(holder, user);

        //加载图片资源
        Picasso.get().load(user.getAvatar()).into(holder.ivAvatar);
        //填充onCreateViewHolder方法返回的holder中的控件
        if (mOnItemClickListener != null) {
            ViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(holder.getLayoutPosition());
                }
            });
            ViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onLongClick(holder.getLayoutPosition());
                    return false;
                }
            });
        }
        //设置点击事件
        holder.tvAppreciate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleAppreciate(holder, user);
            }
        });

    }

    private void handleAppreciate(final ViewHolder holder, final User user) {
        if (holder.tvAppreciate.isSelected()) {
            //选中状态说明还没有欣赏，用户想要欣赏
            appreciate(holder, user);
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
                            unappreciate(holder, user);
                        }
                    }).show();
        }
    }

    private void appreciate(final ViewHolder holder, final User user) {
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
                loadAppreciate(holder, user);
            }
        });
    }

    private void unappreciate(final ViewHolder holder, final User user) {
        String url = "http://" + getSERVER_IP() + ":8080/unappreciate?userID=" + Constant.getCurrUserID() + "&targetUserID=" + user.getUserID();
        Log.i(TAG, "appreciate: url: " + url);
        final HttpUtil httpUtil = new HttpUtil();
        httpUtil.getDataAsync(url, context, new Runnable() {
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
                loadAppreciate(holder, user);
            }
        });
    }

    private void loadAppreciate(final ViewHolder holder, User user) {
        String url = "http://" + getSERVER_IP() + ":8080/get_appreciating?userID=" + Constant.getCurrUserID() + "&targetUserID=" + user.getUserID();
        Log.i(RoundedImageView.TAG, url);
        final HttpUtil httpUtil = new HttpUtil();
        httpUtil.getDataAsync(url, context, new Runnable() {
            @Override
            public void run() {
                String responseData = httpUtil.getResponseData();
                switch (responseData) {
                    case "true":
                        holder.tvAppreciate.setSelected(false);
                        holder.tvAppreciate.setText("已欣赏");
                        Log.i(TAG, "run: 已经欣赏啦！！！按钮不可用就好啦");
                        break;
                    case "false":
                        holder.tvAppreciate.setSelected(true);
                        holder.tvAppreciate.setText("欣赏TA");
                        Log.i(TAG, "run: 还没欣赏呐！！！按钮可用呐");
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public void setScrolling(boolean scrolling) {
        this.scrolling = scrolling;
    }

    public interface OnItemClickListener {

        void onClick(int position);

        void onLongClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        static View itemView;
        TextView tvUserName;
        TextView tvScript;
        ImageView ivAvatar;
        TextView tvAppreciate;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView.getRootView();
            tvUserName = itemView.findViewById(R.id.tv_user_name);
            tvScript = itemView.findViewById(R.id.tv_script);
            ivAvatar = itemView.findViewById(R.id.iv_avatar);
            tvAppreciate = itemView.findViewById(R.id.tv_appreciate);
        }
    }

    public ArrayList<User> getmData() {
        return mData;
    }

}
