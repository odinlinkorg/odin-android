package com.yxsd.mall.adapter;

import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yxsd.mall.R;
import com.yxsd.mall.base.rv.BaseViewHolder;
import com.yxsd.mall.base.rv.adapter.SingleAdapter;
import com.yxsd.mall.entity.Category;
import com.yxsd.mall.ui.goodslist.GoodsListActivity;

import java.util.List;

public class Classify2Adapter extends SingleAdapter<Category> {

    public Classify2Adapter(List<Category> list) {
        super(list, R.layout.item_classify2);
    }

    @Override
    protected void bind(BaseViewHolder holder, Category data, int position) {
        TextView tvClassify1 = holder.getView(R.id.tv_classify1);
        RecyclerView rvClassify2 = holder.getView(R.id.rv_classify2);

        tvClassify1.setText(data.getCateName());
        ClassifyChildAdapter classifyChildAdapter = new ClassifyChildAdapter(data.getSonList());
        classifyChildAdapter.setOnItemClickListener((view, position1) -> GoodsListActivity.start(mContext, classifyChildAdapter.getData().get(position1)));
        rvClassify2.setAdapter(classifyChildAdapter);
        rvClassify2.setLayoutManager(new GridLayoutManager(mContext, 3));
    }
}
