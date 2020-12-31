package com.yxsd.mall.entity;

import java.util.List;

public class ActiveDetail {

    private String      title;
    private List<Goods> goodsList;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Goods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<Goods> goodsList) {
        this.goodsList = goodsList;
    }
}
