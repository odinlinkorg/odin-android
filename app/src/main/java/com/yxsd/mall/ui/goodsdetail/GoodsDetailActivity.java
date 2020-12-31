package com.yxsd.mall.ui.goodsdetail;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.yxsd.mall.R;
import com.yxsd.mall.adapter.ImageBannerAdapter;
import com.yxsd.mall.base.BaseActivity;
import com.yxsd.mall.entity.GoodsDetail;
import com.yxsd.mall.widget.viewholder.ImageBannerViewHolder;
import com.zhpan.bannerview.BannerViewPager;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

public class GoodsDetailActivity extends BaseActivity<GoodsDetailPresenter> implements GoodsDetailView {

    @BindView(R.id.banner_view)
    BannerViewPager<String, ImageBannerViewHolder> mBannerView;
    @BindView(R.id.tv_name)
    TextView                                       mTvName;
    @BindView(R.id.tv_store)
    TextView                                       mTvStore;
    @BindView(R.id.tv_sold_number)
    TextView                                       mTvSoldNumber;
    @BindView(R.id.tv_price)
    TextView                                       mTvPrice;
    @BindView(R.id.btn_hashrate)
    Button                                         mBtnHashrate;
    @BindView(R.id.tv_select_spec)
    TextView                                       mTvSelectSpec;
    @BindView(R.id.web_view)
    WebView                                        mWebView;
    @BindView(R.id.btn_buy_now)
    Button                                         mBtnBuyNow;
    private String      mGoodsId;
    private GoodsDetail mGoodsDetail;
    private int         mSpecIndex = -1;
    private int         mNumber    = 1;

    @Override
    public void onResume() {
        super.onResume();
        if (mBannerView != null) {
            mBannerView.startLoop();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mBannerView != null) {
            mBannerView.stopLoop();
        }
    }

    public static void start(Context context, String goodsId) {
        Intent intent = new Intent(context, GoodsDetailActivity.class);
        intent.putExtra("goodsId", goodsId);
        ActivityUtils.startActivity(intent);
    }

    @Override
    public void initData(@Nullable Bundle bundle) {
        assert bundle != null;
        mGoodsId = bundle.getString("goodsId");
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_goods_detail;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState, @Nullable View contentView) {
        mPresenter = new GoodsDetailPresenter(this, this);
        setTitleText(R.string.goods_detail);
        applyClickListener(mTvSelectSpec, mBtnBuyNow);

        mBannerView.setAdapter(new ImageBannerAdapter()).create();
    }

    @Override
    public void doBusiness() {
        mPresenter.getGoodsDetail(mGoodsId);
    }

    @Override
    public void onViewClick(@NonNull View view) {
        switch (view.getId()) {
            case R.id.tv_select_spec:
            case R.id.btn_buy_now: {
                SelectSpecActivity.start(this, mGoodsDetail, mSpecIndex, mNumber);
            }
            break;
            default:
                break;
        }
    }

    @Override
    public void getGoodsDetail(GoodsDetail goodsDetail) {
        if (!ObjectUtils.isEmpty(goodsDetail)) {
            mGoodsDetail = goodsDetail;
            setData();
        }
    }

    @SuppressLint("SetTextI18n")
    private void setData() {
        List<String> list = Arrays.asList(mGoodsDetail.getSliderImage().split(","));
        mBannerView.refreshData(list);
        mTvName.setText(mGoodsDetail.getGoodsName());
        mTvStore.setText(mGoodsDetail.getStoreName());
        long soldNumber = mGoodsDetail.getSales() + mGoodsDetail.getVirtualSales();
        mTvSoldNumber.setText(String.format(getString(R.string.sold_number), soldNumber + ""));
        mTvPrice.setText(mGoodsDetail.getOdinPrice() + " " + StringUtils.getString(R.string.odin) + "/" + mGoodsDetail.getShopPrice() + " " + StringUtils.getString(R.string.bgc));
        mBtnHashrate.setText(String.format(StringUtils.getString(R.string.str_hashrate), mGoodsDetail.getGivePower()));
        mWebView.loadData(mGoodsDetail.getDescription(), "text/html", "UTF-8");
    }
}