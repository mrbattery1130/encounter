package com.mrbattery.encounter.util;

import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

public class AnimationUtil {

    public static void startAlphaAnimation(ImageView imageView) {
        //渐变动画    从显示（1.0）到隐藏（0.0）
        AlphaAnimation alphaAnim = new AlphaAnimation(0.0f, 1.0f);
        //执行三秒
        alphaAnim.setDuration(1000);
        imageView.startAnimation(alphaAnim);
    }
}
