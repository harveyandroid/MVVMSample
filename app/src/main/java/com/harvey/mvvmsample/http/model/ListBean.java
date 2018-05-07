package com.harvey.mvvmsample.http.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hanhui on 2017/9/13 14:30
 */

public class ListBean<result> {

    private int total;
    private int offset;
    private int pageSize;

    private List<result> item;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<result> getItem() {
        if (item == null) {
            return new ArrayList<>();
        }
        return item;
    }

    public void setItem(List<result> item) {
        this.item = item;
    }
}
