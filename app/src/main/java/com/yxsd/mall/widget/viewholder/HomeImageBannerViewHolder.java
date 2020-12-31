package com.yxsd.mall.widget.viewholder;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ActivityUtils;
import com.yxsd.mall.R;
import com.yxsd.mall.entity.HomeImage;
import com.yxsd.mall.ui.goodsdetail.GoodsDetailActivity;
import com.yxsd.mall.ui.web.WebActivity;
import com.yxsd.mall.utils.ImageLoaderHelper;
import com.zhpan.bannerview.BaseViewHolder;

public class HomeImageBannerViewHolder extends BaseViewHolder<HomeImage.ListBean> {

    public HomeImageBannerViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    @Override
    public void bindData(HomeImage.ListBean data, int position, int pageSize) {
        ImageView imageView = findView(R.id.image_view);
        imageView.setOnClickListener(v -> {
            String imgType = data.getImgType();
            switch (imgType) {
                case "1": {
                    GoodsDetailActivity.start(ActivityUtils.getTopActivity(), data.getGoodsId());
                }
                break;
                case "2": {
                    WebActivity.start(ActivityUtils.getTopActivity(), data.getUrl());
                }
                break;
                default:
                    break;
            }
        });
        ImageLoaderHelper.loadImage(imageView.getContext(), data.getImgUrl(), imageView);
    }
}
