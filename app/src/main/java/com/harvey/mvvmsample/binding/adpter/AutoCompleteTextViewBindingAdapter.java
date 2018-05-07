package com.harvey.mvvmsample.binding.adpter;

import android.databinding.BindingAdapter;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListAdapter;

import java.util.List;

/**
 * Created by hanhui on 2018/5/7 0007 11:12
 */

public class AutoCompleteTextViewBindingAdapter {

    @BindingAdapter({"android:entries"})
    public static <T> void setEntries(AutoCompleteTextView view, List<T> entries) {
        if (entries != null) {
            ListAdapter oldAdapter = view.getAdapter();
            if (oldAdapter instanceof ArrayAdapter) {
                ((ArrayAdapter) oldAdapter).clear();
                ((ArrayAdapter) oldAdapter).addAll(entries);
            } else {
                view.setAdapter(new ArrayAdapter(view.getContext(),
                        android.R.layout.simple_dropdown_item_1line, entries));
            }
        } else {
            view.setAdapter(null);
        }
    }

}
