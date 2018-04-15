package com.mrbattery.encounter.font;

import android.content.Context;
import android.graphics.Typeface;

public class FontCustom {
    // fontUrl是自定义字体分类的名称
    private static String fontUrl = "font/DINCond-Bold.ttf";
    //Typeface是字体，这里我们创建一个对象
    private static Typeface tf;

    /**
     * 设置字体
     */
    public static Typeface setFont(Context context)
    {
        if (tf == null)
        {
            //给它设置你传入的自定义字体文件，再返回回来
            tf = Typeface.createFromAsset(context.getAssets(),fontUrl);
        }
        return tf;
    }
}
