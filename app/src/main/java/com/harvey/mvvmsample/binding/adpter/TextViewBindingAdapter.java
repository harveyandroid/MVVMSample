package com.harvey.mvvmsample.binding.adpter;

import android.databinding.BindingAdapter;
import android.view.KeyEvent;
import android.widget.TextView;

/**
 * Created by hanhui on 2018/5/4 0004 17:17
 */

public class TextViewBindingAdapter {

    @BindingAdapter({"android:onEditorAction"})
    public static void onEditorAction(TextView textView, final TextView.OnEditorActionListener listener) {
        textView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                return listener.onEditorAction(v, actionId, event);
            }
        });
    }


    @BindingAdapter({"android:error"})
    public static void setError(TextView textView, String text) {
        textView.setError(text);
    }


}
