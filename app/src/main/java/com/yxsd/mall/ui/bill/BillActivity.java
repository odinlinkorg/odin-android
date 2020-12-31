package com.yxsd.mall.ui.bill;

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
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yxsd.mall.R;
import com.yxsd.mall.adapter.BillAdapter;
import com.yxsd.mall.base.BaseActivity;
import com.yxsd.mall.base.rv.RecycleViewDivider;
import com.yxsd.mall.entity.Transaction;
import com.yxsd.mall.entity.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class BillActivity extends BaseActivity<BillPresenter> implements BillView {

    @BindView(R.id.tv_no_data)
    TextView           mTvNoData;
    @BindView(R.id.recycler_view)
    RecyclerView       mRecyclerView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    private BillAdapter mAdapter;
    private int         mPage = 1;

    public static void start(Context context) {
        Intent intent = new Intent(context, BillActivity.class);
        ActivityUtils.startActivity(intent);
    }

    @Override
    public void initData(@Nullable Bundle bundle) {
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_bill;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState, @Nullable View contentView) {
        mPresenter = new BillPresenter(this, this);
        setTitleText(R.string.bill);

        mRefreshLayout.setOnRefreshListener(refreshlayout -> {
            mPage = 1;
            getData();
        });
        mRefreshLayout.setOnLoadMoreListener(refreshLayout -> {
            mPage += 1;
            getData();
        });

        mAdapter = new BillAdapter(new ArrayList<>());
        mAdapter.setOnItemClickListener((view, position) -> BillDetailActivity.start(this, mAdapter.getData().get(position)));
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
    public void getUser(User user) {
    }

    @Override
    public void getTransactionList(List<Transaction> list) {
        setData(list);
    }

    private void getData() {
        mPresenter.getTransactionList(mPage + "");
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