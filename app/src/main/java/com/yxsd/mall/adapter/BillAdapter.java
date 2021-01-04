package com.yxsd.mall.adapter;

import android.annotation.SuppressLint;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.StringUtils;
import com.yxsd.mall.R;
import com.yxsd.mall.base.rv.BaseViewHolder;
import com.yxsd.mall.base.rv.adapter.SingleAdapter;
import com.yxsd.mall.entity.Transaction;

import java.util.List;

public class BillAdapter extends SingleAdapter<Transaction> {

    public BillAdapter(List<Transaction> list) {
        super(list, R.layout.item_bill);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void bind(BaseViewHolder holder, Transaction data, int position) {
        ImageView ivType = holder.getView(R.id.iv_type);
        TextView tvType = holder.getView(R.id.tv_type);
        TextView tvStatus = holder.getView(R.id.tv_status);
        TextView tvTime = holder.getView(R.id.tv_time);
        TextView tvValue = holder.getView(R.id.tv_value);

        switch (data.getOperateType()) {
            case 0: {
                ivType.setImageResource(R.drawable.ic_transfer);
                tvType.setText(R.string.transfer);
            }
            break;
            case 1: {
                ivType.setImageResource(R.drawable.ic_withdraw);
                tvType.setText(R.string.withdraw);
            }
            break;
            case 2: {
                ivType.setImageResource(R.drawable.ic_hashrate_drop);
                tvType.setText(R.string.hashrate_drop);
            }
            break;
            case 3: {
                ivType.setImageResource(R.drawable.ic_hashrate_drop);
                tvType.setText(R.string.activities_presented);
            }
            break;
            case 4: {
                ivType.setImageResource(R.drawable.ic_buy_goods);
                tvType.setText(R.string.buy_goods);
            }
            break;
            case 5: {
                ivType.setImageResource(R.drawable.ic_charge);
                tvType.setText(R.string.charge);
            }
            break;
            default:
                break;
        }
        switch (data.getStatus()) {
            case "0": {
                tvStatus.setText(R.string.in_review);
                tvStatus.setTextColor(ColorUtils.getColor(R.color.yellow));
            }
            break;
            case "1":
            case "2":
            case "3": {
                tvStatus.setText(R.string.transfer_success);
                tvStatus.setTextColor(ColorUtils.getColor(R.color.green));
            }
            break;
            case "4": {
                tvStatus.setText(R.string.transfer_fail);
                tvStatus.setTextColor(ColorUtils.getColor(R.color.app_color));
            }
            break;
            case "5": {
                tvStatus.setText(R.string.review_fail);
                tvStatus.setTextColor(ColorUtils.getColor(R.color.app_color));
            }
            break;
            case "9": {
                tvStatus.setText("");
                tvStatus.setTextColor(ColorUtils.getColor(R.color.app_color));
            }
            break;
            default:
                break;
        }
        tvTime.setText(data.getCreateTime());
        if (data.getType() == 1) {
            tvValue.setText(data.getSymbol() + data.getNum() + " " + StringUtils.getString(R.string.bgc));
        } else if (data.getType() == 2) {
            tvValue.setText(data.getSymbol() + data.getNum() + " " + StringUtils.getString(R.string.odin));
        }
    }
}
