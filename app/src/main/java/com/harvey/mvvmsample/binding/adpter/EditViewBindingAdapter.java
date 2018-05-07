package com.harvey.mvvmsample.binding.adpter;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by hanhui on 2018/5/4 0004 17:17
 */

public class EditViewBindingAdapter {

    /**
     * EditText重新获取焦点的事件绑定
     */
    @BindingAdapter(value = {"android:requestFocus"})
    public static void requestFocus(EditText editText, final Boolean needRequestFocus) {
        if (needRequestFocus) {
            editText.setSelection(editText.getText().length());
            editText.requestFocus();
            InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        }
        editText.setFocusableInTouchMode(needRequestFocus);
    }

}
