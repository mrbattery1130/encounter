package com.mrbattery.encounter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mrbattery.encounter.R;
import com.mrbattery.encounter.constant.Constant;
import com.mrbattery.encounter.entity.MatchedUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static com.makeramen.roundedimageview.RoundedDrawable.TAG;

public class EncounterAdapter extends RecyclerView.Adapter<EncounterAdapter.ViewHolder> {

    private ArrayList<MatchedUser> mData;
    private Context context;
    private ViewHolder viewHolder;
    private OnItemClickListener mOnItemClickListener;
    private boolean scrolling;

    public EncounterAdapter(ArrayList<MatchedUser> mData, Context context) {
        this.mData = mData;
        this.context = context;
    }

    public void updateData(ArrayList<MatchedUser> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.encounter_card, parent, false);
        // 实例化viewHolder
        viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        MatchedUser matchedUser = mData.get(position);
        double degree = (double) (matchedUser.getMatchingDegree());
        // 绑定数据
        holder.tvUserName.setText(matchedUser.getUserName());
        holder.tvConstellation.setText(Constant.getConstellationName(matchedUser.getConstellation()));
        holder.tvScript.setText(matchedUser.getScript());

        holder.tvMatchingDegree.setText(String.valueOf((int) degree));
        holder.matchingDegreeBar.setLayoutParams(new LinearLayout.LayoutParams(0, MATCH_PARENT, (float) (degree / (100 - degree))));

        holder.ivGender.setImageResource(Constant.getGenderSrc(matchedUser.getGender()));

        //加载头像图片资源

        String avatarUrl = matchedUser.getAvatar();
        Log.i(TAG, "run: 开始加载头像" + position);
        Picasso.get().load(avatarUrl).into(holder.ivAvatar);
        /*HttpUtil.getPictureAsync(avatarUrl, context, new Runnable() {
            @Override
            public void run() {
                Bitmap avatarBmp = HttpUtil.getResponseBmp();
                    holder.ivAvatar.setImageBitmap(avatarBmp);
                    AnimationUtil.startAlphaAnimation(holder.ivAvatar);
                    Log.i(TAG, "run: 完成加载头像" + position);
            }
        });*/

        //加载封面图片资源
        String coverUrl = matchedUser.getCover();
        Log.i(TAG, "run: 开始加载封面" + position);
        Picasso.get().load(coverUrl).into(holder.ivCover);
        /*HttpUtil.getPictureAsync(coverUrl, context, new Runnable() {
            @Override
            public void run() {
                Bitmap coverBmp = HttpUtil.getResponseBmp();
                    holder.ivCover.setImageBitmap(coverBmp);
                    AnimationUtil.startAlphaAnimation(holder.ivCover);
                    Log.i(TAG, "run: 完成加载封面" + position);
            }
        });*/

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
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public void setScrolling(boolean scrolling) {
        this.scrolling = scrolling;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        static View itemView;
        TextView tvUserName;
        TextView tvConstellation;
        TextView tvScript;
        ImageView ivGender;
        ImageView ivAvatar;
        ImageView ivCover;
        TextView tvMatchingDegree;
        View matchingDegreeBar;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView.getRootView();
            tvMatchingDegree = itemView.findViewById(R.id.tv_matching_degree);
            matchingDegreeBar = itemView.findViewById(R.id.matching_degree_bar);
            tvUserName = itemView.findViewById(R.id.tv_user_name);
            tvConstellation = itemView.findViewById(R.id.tv_constellation);
            tvScript = itemView.findViewById(R.id.tv_script);
            ivGender = itemView.findViewById(R.id.iv_gender);
            ivAvatar = itemView.findViewById(R.id.iv_avatar);
            ivCover = itemView.findViewById(R.id.iv_cover);
        }
    }

    public interface OnItemClickListener {
        void onClick(int position);

        void onLongClick(int position);
    }

    public ArrayList<MatchedUser> getmData() {
        return mData;
    }
}
