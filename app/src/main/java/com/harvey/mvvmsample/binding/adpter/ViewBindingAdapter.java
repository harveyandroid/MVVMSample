package com.harvey.mvvmsample.binding.adpter;

import android.animation.Animator;
import android.databinding.BindingAdapter;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

/**
 * Created by hanhui on 2018/5/4 0004 17:17
 */

public class ViewBindingAdapter {

    @BindingAdapter(value = {"animatorListener","animatorDuration","animatorAlpha"}, requireAll = false)
    public static void setAnimatorListener(View view, Animator.AnimatorListener listener, long duration, float value) {
        view.animate().setDuration(duration).alpha(value).setListener(listener);
    }

}
