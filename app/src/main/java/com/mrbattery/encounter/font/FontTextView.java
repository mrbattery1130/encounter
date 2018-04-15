package com.mrbattery.encounter.font;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class FontTextView extends android.support.v7.widget.AppCompatTextView {
    public FontTextView(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
        init(context);
    }

    private void init(Context context) {
        //设置字体样式
        setTypeface(FontCustom.setFont(context));
    }

}
