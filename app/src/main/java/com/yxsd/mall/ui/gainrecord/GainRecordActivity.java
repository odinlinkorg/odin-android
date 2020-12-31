package com.yxsd.mall.ui.gainrecord;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.SpanUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yxsd.mall.R;
import com.yxsd.mall.adapter.GainAdapter;
import com.yxsd.mall.base.BaseActivity;
import com.yxsd.mall.base.rv.RecycleViewDivider;
import com.yxsd.mall.entity.Gain;
import com.yxsd.mall.entity.TotalGain;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class GainRecordActivity extends BaseActivity<GainRecordPresenter> implements GainRecordView {

    @BindView(R.id.tv_total_gain)
    TextView           mTvTotalGain;
    @BindView(R.id.tv_no_data)
    TextView           mTvNoData;
    @BindView(R.id.recycler_view)
    RecyclerView       mRecyclerView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    private GainAdapter mAdapter;

    public static void start(Context context) {
        Intent intent = new Intent(context, GainRecordActivity.class);
        ActivityUtils.startActivity(intent);
    }

    @Override
    public void initData(@Nullable Bundle bundle) {
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_gain_record;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState, @Nullable View contentView) {
        mPresenter = new GainRecordPresenter(this, this);
        setTitleText(R.string.gain_record);

        mRefreshLayout.setOnRefreshListener(refreshlayout -> getData());
        mRefreshLayout.setEnableLoadMore(false);

        mAdapter = new GainAdapter(new ArrayList<>());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new RecycleViewDivider(mActivity, RecycleViewDivider.VERTICAL, R.drawable.divider));

        mRefreshLayout.autoRefresh();
    }

    @Override
    public void doBusiness() {
    }

    @Override
    public void onViewClick(@NonNull View view) {
    }

    @Override
    public void getTotalGain(TotalGain totalGain) {
        SpanUtils.with(mTvTotalGain)
                .append(getString(R.string.total_gain) + "\n\n")
                .setFontSize(16, true)
                .append(totalGain.getProfitTotal())
                .setFontSize(20, true)
                .create();
    }

    @Override
    public void getGainList(List<Gain> list) {
        setData(list);
    }

    private void getData() {
        mPresenter.getTotalGain();
        mPresenter.getGainList();
    }

    private void setData(List<Gain> list) {
        mRefreshLayout.finishRefresh();
        mRefreshLayout.finishLoadMore();
        mAdapter.update(list);
        if (ObjectUtils.isEmpty(list)) {
            mTvNoData.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mTvNoData.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }
}