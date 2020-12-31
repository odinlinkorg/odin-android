package com.yxsd.mall.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.SpanUtils;
import com.yxsd.mall.R;
import com.yxsd.mall.base.rv.BaseViewHolder;
import com.yxsd.mall.base.rv.adapter.SingleAdapter;
import com.yxsd.mall.entity.LogisticsInfo;

import java.util.List;

public class LogisticsAdapter extends SingleAdapter<LogisticsInfo.ResultBean.ListBean> {

    public LogisticsAdapter(List<LogisticsInfo.ResultBean.ListBean> list) {
        super(list, R.layout.item_logistics);
    }

    @Override
    protected void bind(BaseViewHolder holder, LogisticsInfo.ResultBean.ListBean data, int position) {
        TextView tvTime = holder.getView(R.id.tv_time);
        View viewDivider0 = holder.getView(R.id.view_divider0);
        ImageView ivCheck = holder.getView(R.id.iv_check);
        View viewDivider1 = holder.getView(R.id.view_divider1);
        TextView tvContent = holder.getView(R.id.tv_content);

        String[] strings = data.getTime().substring(5, 16).split(" ");
        if (getData().indexOf(data) == 0) {
            SpanUtils.with(tvTime)
                    .appendLine(strings[0])
                    .setFontSize(14, true)
                    .setForegroundColor(ColorUtils.getColor(R.color.light_black))
                    .append(strings[1])
                    .setFontSize(12, true)
                    .setForegroundColor(ColorUtils.getColor(R.color.light_black3))
                    .create();
            viewDivider0.setVisibility(View.GONE);
            ivCheck.setImageResource(R.drawable.ic_check_red);
            tvContent.setText(data.getStatus());
            tvContent.setTextColor(ColorUtils.getColor(R.color.app_color));
            tvContent.setTextSize(14);
        } else {
            SpanUtils.with(tvTime)
                    .appendLine(strings[0])
                    .setFontSize(10, true)
                    .setForegroundColor(ColorUtils.getColor(R.color.light_black3))
                    .append(strings[1])
                    .setFontSize(10, true)
                    .setForegroundColor(ColorUtils.getColor(R.color.light_black3))
                    .create();
            viewDivider0.setVisibility(View.VISIBLE);
            ivCheck.setImageResource(R.drawable.ic_check_gray);
            tvContent.setText(data.getStatus());
            tvContent.setTextColor(ColorUtils.getColor(R.color.light_black3));
            tvContent.setTextSize(12);
        }
        if (getData().indexOf(data) == getData().size() - 1) {
            viewDivider1.setVisibility(View.GONE);
        } else {
            viewDivider1.setVisibility(View.VISIBLE);
        }
    }
}
