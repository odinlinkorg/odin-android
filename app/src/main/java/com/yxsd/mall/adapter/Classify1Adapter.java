package com.yxsd.mall.adapter;

import android.widget.TextView;

import com.blankj.utilcode.util.ColorUtils;
import com.yxsd.mall.R;
import com.yxsd.mall.base.rv.BaseViewHolder;
import com.yxsd.mall.base.rv.adapter.SingleAdapter;
import com.yxsd.mall.entity.Category;

import java.util.List;

public class Classify1Adapter extends SingleAdapter<Category> {

    public Classify1Adapter(List<Category> list) {
        super(list, R.layout.item_classify1);
    }

    @Override
    protected void bind(BaseViewHolder holder, Category data, int position) {
        TextView textView = holder.getView(R.id.text_view);

        textView.setText(data.getCateName());
        if (data.isCheck()) {
            textView.setTextSize(18);
            textView.setTextColor(ColorUtils.getColor(R.color.app_color));
        } else {
            textView.setTextSize(16);
            textView.setTextColor(ColorUtils.getColor(R.color.light_black3));
        }
    }
}
