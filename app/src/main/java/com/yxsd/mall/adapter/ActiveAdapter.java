package com.yxsd.mall.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.yxsd.mall.R;
import com.yxsd.mall.base.rv.BaseViewHolder;
import com.yxsd.mall.base.rv.adapter.SingleAdapter;
import com.yxsd.mall.entity.HomeActive;
import com.yxsd.mall.utils.ImageLoaderHelper;

import java.util.List;

public class ActiveAdapter extends SingleAdapter<HomeActive> {

    public ActiveAdapter(List<HomeActive> list) {
        super(list, R.layout.item_active);
    }

    @Override
    protected void bind(BaseViewHolder holder, HomeActive data, int position) {
        ImageView imageView = holder.getView(R.id.image_view);
        TextView textView = holder.getView(R.id.text_view);

        ImageLoaderHelper.loadImage(mContext, data.getIcon(), imageView);
        textView.setText(data.getTitle());
    }
}
