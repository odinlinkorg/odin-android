package com.yxsd.mall.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SpanUtils;
import com.yxsd.mall.R;
import com.yxsd.mall.base.rv.BaseViewHolder;
import com.yxsd.mall.base.rv.adapter.SingleAdapter;
import com.yxsd.mall.entity.Address;
import com.yxsd.mall.ui.address.EditAddressActivity;

import java.util.List;

public class AddressAdapter extends SingleAdapter<Address> {

    public AddressAdapter(List<Address> list) {
        super(list, R.layout.item_address);
    }

    @Override
    protected void bind(BaseViewHolder holder, Address data, int position) {
        TextView tvAddress = holder.getView(R.id.tv_address);
        ImageView ivEdit = holder.getView(R.id.iv_edit);

        SpanUtils.with(tvAddress)
                .append(data.getShippingName() + "  ")
                .setFontSize(16, true)
                .append(data.getShippingPhone() + "\n\n" + data.getShippingAddress() + data.getShippingAddressDetail())
                .setFontSize(14, true)
                .create();
        applyClickListener(v -> EditAddressActivity.start(mContext, data), ivEdit);
    }
}
