package com.mrbattery.encounter.customView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;

import com.mrbattery.encounter.R;
import com.mrbattery.encounter.entity.Keyword;

import static com.makeramen.roundedimageview.RoundedDrawable.TAG;
import static com.mrbattery.encounter.util.UnitConversionUtil.dp2px;


public class KeywordTextView extends android.support.v7.widget.AppCompatTextView {

    private Keyword keyword;

    @SuppressLint("ResourceType")
    public KeywordTextView(Context context, Keyword keyword) {
        super(context);
        this.setKeyword(keyword);
        //设置TextView属性
        setText(this.keyword.getKeywordName());
        setTextColor(getResources().getColorStateList(R.drawable.keyword_text_color));
        setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_medium));
        setBackgroundResource(R.drawable.keyword_bg);
        setGravity(Gravity.CENTER);
        setPadding(dp2px(context, 12),
                dp2px(context, 4),
                dp2px(context, 12),
                dp2px(context, 4));
        //点击事件
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelected(!isSelected());
                Log.i(TAG, "onClick: 点击了" + getKeyword().getKeywordName());
            }
        });
//        Log.i(TAG, "KeywordTextView: " + toString());
    }

    public Keyword getKeyword() {
        return keyword;
    }

    public void setKeyword(Keyword keyword) {
        this.keyword = keyword;
    }

    @Override
    public String toString() {
        return "KeywordTextView{" +
                "keywordID=" + keyword.getKeywordID() +
                ", keywordName='" + keyword.getKeywordName() + '\'' +
                ", parentTopicID=" + keyword.getParentTopicID() +
                ", parentTopicName=" + keyword.getParentTopicName() +
                ", paddingLeft=" + getPaddingLeft() +
                ", paddingRight=" + getPaddingRight() +
                ", paddingTop=" + getPaddingTop() +
                ", paddingBottom=" + getPaddingBottom() +
                '}';
    }
}
