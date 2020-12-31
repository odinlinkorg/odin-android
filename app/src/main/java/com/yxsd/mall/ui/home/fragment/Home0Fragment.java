package com.yxsd.mall.ui.home.fragment;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yxsd.mall.R;
import com.yxsd.mall.adapter.ActiveAdapter;
import com.yxsd.mall.adapter.FeaturesAreaAdapter;
import com.yxsd.mall.adapter.HomeImageBannerAdapter;
import com.yxsd.mall.adapter.RecommendAdapter;
import com.yxsd.mall.base.BaseFragment;
import com.yxsd.mall.entity.HomeActive;
import com.yxsd.mall.entity.HomeGoods;
import com.yxsd.mall.entity.HomeImage;
import com.yxsd.mall.ui.goodsdetail.GoodsDetailActivity;
import com.yxsd.mall.ui.home.HomeActivity;
import com.yxsd.mall.ui.messagecenter.MessageCenterActivity;
import com.yxsd.mall.ui.searchgoods.SearchGoodsActivity;
import com.yxsd.mall.ui.specialarea.SpecialAreaActivity;
import com.yxsd.mall.ui.web.WebActivity;
import com.yxsd.mall.utils.ImageLoaderHelper;
import com.yxsd.mall.widget.viewholder.HomeImageBannerViewHolder;
import com.zhpan.bannerview.BannerViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class Home0Fragment extends BaseFragment {

    @BindView(R.id.btn_search_goods)
    QMUIRoundButton                                                mBtnSearchGoods;
    @BindView(R.id.iv_message)
    ImageView                                                      mIvMessage;
    @BindView(R.id.tv_number)
    TextView                                                       mTvNumber;
    @BindView(R.id.banner_view)
    BannerViewPager<HomeImage.ListBean, HomeImageBannerViewHolder> mBannerView;
    @BindView(R.id.rv_active)
    RecyclerView                                                   mRvActive;
    @BindView(R.id.iv_features_area)
    ImageView                                                      mIvFeaturesArea;
    @BindView(R.id.rv_features_area)
    RecyclerView                                                   mRvFeaturesArea;
    @BindView(R.id.rv_recommend)
    RecyclerView                                                   mRvRecommend;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout                                             mRefreshLayout;
    private ActiveAdapter       mActiveAdapter;
    private FeaturesAreaAdapter mFeaturesAreaAdapter;
    private RecommendAdapter    mRecommendAdapter;
    private HomeImage.ListBean  mListBean;

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

    public static Home0Fragment newInstance() {
        Bundle args = new Bundle();
        Home0Fragment fragment = new Home0Fragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initData(@Nullable Bundle bundle) {
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_home0;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState, @Nullable View contentView) {
        BarUtils.addMarginTopEqualStatusBarHeight(mBtnSearchGoods);
        applyClickListener(mBtnSearchGoods, mIvMessage, mIvFeaturesArea);

        mRefreshLayout.setOnRefreshListener(refreshlayout -> getData());
        mRefreshLayout.setEnableLoadMore(false);

        mBannerView.setAdapter(new HomeImageBannerAdapter()).create();

        mActiveAdapter = new ActiveAdapter(new ArrayList<>());
        mActiveAdapter.setOnItemClickListener((view, position) -> SpecialAreaActivity.start(mActivity, mActiveAdapter.getData().get(position).getTitle(), mActiveAdapter.getData().get(position).getActivityId()));
        mRvActive.setAdapter(mActiveAdapter);

        mFeaturesAreaAdapter = new FeaturesAreaAdapter(new ArrayList<>());
        mFeaturesAreaAdapter.setOnItemClickListener((view, position) -> GoodsDetailActivity.start(mActivity, mFeaturesAreaAdapter.getData().get(position).getGoodsId()));
        mRvFeaturesArea.setAdapter(mFeaturesAreaAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        mRvFeaturesArea.setLayoutManager(linearLayoutManager);

        mRecommendAdapter = new RecommendAdapter(new ArrayList<>());
        mRecommendAdapter.setOnItemClickListener((view, position) -> GoodsDetailActivity.start(mActivity, mRecommendAdapter.getData().get(position).getGoodsId()));
        mRvRecommend.setAdapter(mRecommendAdapter);
        mRvRecommend.setLayoutManager(new GridLayoutManager(mActivity, 2));

        mRefreshLayout.autoRefresh();
    }

    private void getData() {
        ((HomeActivity) mActivity).mPresenter.getHomeImage();
        ((HomeActivity) mActivity).mPresenter.getHomeActive();
        ((HomeActivity) mActivity).mPresenter.getHomeGoods();
    }

    @Override
    public void doBusiness() {
    }

    @Override
    public void onViewClick(@NonNull View view) {
        switch (view.getId()) {
            case R.id.btn_search_goods: {
                SearchGoodsActivity.start(mActivity);
            }
            break;
            case R.id.iv_message: {
                MessageCenterActivity.start(mActivity);
            }
            break;
            case R.id.iv_features_area: {
                if (!ObjectUtils.isEmpty(mListBean)) {
                    String imgType = mListBean.getImgType();
                    switch (imgType) {
                        case "1": {
                            GoodsDetailActivity.start(ActivityUtils.getTopActivity(), mListBean.getGoodsId());
                        }
                        break;
                        case "2": {
                            WebActivity.start(ActivityUtils.getTopActivity(), mListBean.getUrl());
                        }
                        break;
                        default:
                            break;
                    }
                }
            }
            break;
            default:
                break;
        }
    }

    @Override
    public void onMessageEvent(Message msg) {
        super.onMessageEvent(msg);
        switch (msg.what) {
            case R.id.msg_get_home_image: {
                HomeImage homeImage = (HomeImage) msg.obj;
                setHomeImage(homeImage);
            }
            break;
            case R.id.msg_get_home_active: {
                List<HomeActive> list = (List<HomeActive>) msg.obj;
                setHomeActive(list);
            }
            break;
            case R.id.msg_get_home_goods: {
                HomeGoods homeGoods = (HomeGoods) msg.obj;
                setHomeGoods(homeGoods);
            }
            break;
            default:
                break;
        }
    }

    private void setHomeImage(HomeImage homeImage) {
        if (!ObjectUtils.isEmpty(homeImage)) {
            List<HomeImage.ListBean> slideshowList = homeImage.getSlideshowList();
            mBannerView.refreshData(slideshowList);
            if (!ObjectUtils.isEmpty(homeImage.getFeatureList())) {
                mListBean = homeImage.getFeatureList().get(0);
                ImageLoaderHelper.loadImage(mActivity, homeImage.getFeatureList().get(0).getImgUrl(), mIvFeaturesArea);
            }
        }
    }

    private void setHomeActive(List<HomeActive> list) {
        if (!ObjectUtils.isEmpty(list)) {
            mRvActive.setLayoutManager(new GridLayoutManager(mActivity, list.size()));
            mActiveAdapter.update(list);
        }
    }

    private void setHomeGoods(HomeGoods homeGoods) {
        mRefreshLayout.finishRefresh();
        mRefreshLayout.finishLoadMore();
        if (!ObjectUtils.isEmpty(homeGoods)) {
            mFeaturesAreaAdapter.update(homeGoods.getFeatureList());
            mRecommendAdapter.update(homeGoods.getRecommendList());
        }
    }
}