package com.yxsd.mall.ui.address;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ActivityUtils;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yxsd.mall.R;
import com.yxsd.mall.adapter.AddressAdapter;
import com.yxsd.mall.base.BaseActivity;
import com.yxsd.mall.base.rv.RecycleViewDivider;
import com.yxsd.mall.entity.Address;
import com.yxsd.mall.entity.PickerData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class AddressActivity extends BaseActivity<AddressPresenter> implements AddressView {

    @BindView(R.id.recycler_view)
    RecyclerView       mRecyclerView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    private int            mType;
    private AddressAdapter mAdapter;

    public static void start(Context context, int type) {
        Intent intent = new Intent(context, AddressActivity.class);
        intent.putExtra("type", type);
        ActivityUtils.startActivity(intent);
    }

    @Override
    public void initData(@Nullable Bundle bundle) {
        assert bundle != null;
        mType = bundle.getInt("type");
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_address;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState, @Nullable View contentView) {
        mPresenter = new AddressPresenter(this, this);
        setTitleText(R.string.address);

        mAdapter = new AddressAdapter(new ArrayList<>());
        mAdapter.setHeaderView(new View(this));
        View footer = LayoutInflater.from(this).inflate(R.layout.footer_address, null, false);
        footer.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        footer.setOnClickListener(v -> EditAddressActivity.start(this, null));
        mAdapter.setFooterView(footer);
        mAdapter.setOnItemClickListener((view, position) -> {
            if (mType == 1) {
                sendMessage(R.id.msg_select_address, mAdapter.getData().get(position));
                finish();
            }
        });
        mAdapter.setOnItemLongClickListener((view, position) -> {
            new QMUIDialog.MessageDialogBuilder(this)
                    .setMessage(getString(R.string.confirm_delete_address))
                    .addAction(R.string.cancel, (dialog, index) -> dialog.dismiss())
                    .addAction(R.string.confirm, (dialog, index) -> {
                        dialog.dismiss();
                        mPresenter.deleteAddress(mAdapter.getData().get(position).getId() + "");
                    })
                    .create(com.qmuiteam.qmui.R.style.QMUI_Dialog).show();
            return false;
        });
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new RecycleViewDivider(mActivity, RecycleViewDivider.VERTICAL, R.drawable.divider16));

        mRefreshLayout.setOnRefreshListener(refreshlayout -> mPresenter.getAddress());
        mRefreshLayout.setEnableLoadMore(false);

        mRefreshLayout.autoRefresh();
    }

    @Override
    public void doBusiness() {
    }

    @Override
    public void onViewClick(@NonNull View view) {
    }

    @Override
    public void onMessageEvent(Message msg) {
        super.onMessageEvent(msg);
        if (msg.what == R.id.msg_update_address) {
            mRefreshLayout.autoRefresh();
        }
    }

    @Override
    public void getAddress(List<Address> list) {
        mRefreshLayout.finishRefresh();
        mAdapter.update(list);
    }

    @Override
    public void addAddress() {
    }

    @Override
    public void updateAddress() {
    }

    @Override
    public void deleteAddress() {
        mRefreshLayout.autoRefresh();
    }

    @Override
    public void loadData(PickerData pickerData) {
    }
}