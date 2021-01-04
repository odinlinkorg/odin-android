package com.yxsd.mall.ui.assets;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.qmuiteam.qmui.widget.tab.QMUITab;
import com.qmuiteam.qmui.widget.tab.QMUITabBuilder;
import com.qmuiteam.qmui.widget.tab.QMUITabSegment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yxsd.mall.R;
import com.yxsd.mall.adapter.TransactionAdapter;
import com.yxsd.mall.base.BaseActivity;
import com.yxsd.mall.base.rv.RecycleViewDivider;
import com.yxsd.mall.entity.Transaction;
import com.yxsd.mall.entity.WalletBalance;
import com.yxsd.mall.ui.charge.ChargeActivity;
import com.yxsd.mall.ui.withdraw.WithdrawActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class AssetsActivity extends BaseActivity<AssetsPresenter> implements AssetsView {

    @BindView(R.id.tv_odin_available)
    TextView           mTvOdinAvailable;
    @BindView(R.id.tv_odin_freeze)
    TextView           mTvOdinFreeze;
    @BindView(R.id.tv_bgc_available)
    TextView           mTvBgcAvailable;
    @BindView(R.id.tv_bgc_freeze)
    TextView           mTvBgcFreeze;
    @BindView(R.id.tab_segment)
    QMUITabSegment     mTabSegment;
    @BindView(R.id.tv_no_data)
    TextView           mTvNoData;
    @BindView(R.id.recycler_view)
    RecyclerView       mRecyclerView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.btn_charge)
    QMUIRoundButton    mBtnCharge;
    @BindView(R.id.btn_withdraw)
    QMUIRoundButton    mBtnWithdraw;
    private TransactionAdapter mAdapter;
    private int                mPage = 1;

    public static void start(Context context) {
        Intent intent = new Intent(context, AssetsActivity.class);
        ActivityUtils.startActivity(intent);
    }

    @Override
    public void initData(@Nullable Bundle bundle) {
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_assets;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState, @Nullable View contentView) {
        mPresenter = new AssetsPresenter(this, this);
        setTitleText(R.string.assets);
        applyClickListener(mBtnCharge, mBtnWithdraw);

        QMUITabBuilder builder = mTabSegment.tabBuilder();
        builder.setNormalColor(ColorUtils.getColor(R.color.light_black3))
                .setSelectColor(ColorUtils.getColor(R.color.light_black1));
        QMUITab tab0 = builder
                .setText(getString(R.string.all))
                .build(mActivity);
        QMUITab tab1 = builder
                .setText(getString(R.string.charge))
                .build(mActivity);
        QMUITab tab2 = builder
                .setText(getString(R.string.withdraw))
                .build(mActivity);
        mTabSegment.addTab(tab0)
                .addTab(tab1)
                .addTab(tab2);
        mTabSegment.addOnTabSelectedListener(new QMUITabSegment.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int index) {
                mRefreshLayout.autoRefresh();
            }

            @Override
            public void onTabUnselected(int index) {
            }

            @Override
            public void onTabReselected(int index) {
                mRefreshLayout.autoRefresh();
            }

            @Override
            public void onDoubleTap(int index) {
            }
        });

        mRefreshLayout.setOnRefreshListener(refreshlayout -> {
            mPage = 1;
            getData();
        });
        mRefreshLayout.setOnLoadMoreListener(refreshLayout -> {
            mPage += 1;
            getData();
        });

        mAdapter = new TransactionAdapter(new ArrayList<>());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new RecycleViewDivider(mActivity, RecycleViewDivider.VERTICAL, R.drawable.divider));

        mTabSegment.selectTab(0);
    }

    @Override
    public void doBusiness() {
    }

    @Override
    public void onViewClick(@NonNull View view) {
        switch (view.getId()) {
            case R.id.btn_charge: {
                ChargeActivity.start(this);
            }
            break;
            case R.id.btn_withdraw: {
                WithdrawActivity.start(this);
            }
            break;
            default:
                break;
        }
    }

    @Override
    public void onMessageEvent(Message msg) {
        super.onMessageEvent(msg);
        if (msg.what == R.id.msg_refresh_transaction_list) {
            mRefreshLayout.autoRefresh();
        }
    }

    @Override
    public void getWalletBalance(WalletBalance walletBalance) {
        mTvOdinAvailable.setText(walletBalance.getAvailableOdin());
        mTvOdinFreeze.setText(walletBalance.getFreezeOdin());
        mTvBgcAvailable.setText(walletBalance.getAvailableBgc());
        mTvBgcFreeze.setText(walletBalance.getFreezeBgc());
    }

    @Override
    public void getTransactionList(List<Transaction> list) {
        setData(list);
    }

    private void getData() {
        mPresenter.getWalletBalance();
        switch (mTabSegment.getSelectedIndex()) {
            case 0: {
                mPresenter.getTransactionList("6", mPage + "");
            }
            break;
            case 1: {
                mPresenter.getTransactionList("5", mPage + "");
            }
            break;
            case 2: {
                mPresenter.getTransactionList("1", mPage + "");
            }
            break;
            default:
                break;
        }
    }

    private void setData(List<Transaction> list) {
        mRefreshLayout.finishRefresh();
        mRefreshLayout.finishLoadMore();
        List<Transaction> data = mAdapter.getData();
        if (mPage == 1) {
            data = list;
            mRecyclerView.scrollToPosition(0);
        } else {
            if (ObjectUtils.isEmpty(list)) {
                mPage -= 1;
            } else {
                data.addAll(list);
            }
        }
        mAdapter.update(data);
        if (ObjectUtils.isEmpty(data)) {
            mTvNoData.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mTvNoData.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }
}