package com.yxsd.mall.ui.searchgoods;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.qmuiteam.qmui.widget.QMUIFloatLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yxsd.mall.R;
import com.yxsd.mall.adapter.MallAdapter;
import com.yxsd.mall.base.BaseActivity;
import com.yxsd.mall.base.rv.RecycleViewDivider;
import com.yxsd.mall.entity.Goods;
import com.yxsd.mall.entity.HotSearch;
import com.yxsd.mall.ui.goodsdetail.GoodsDetailActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SearchGoodsActivity extends BaseActivity<SearchGoodsPresenter> implements SearchGoodsView {

    @BindView(R.id.et_search)
    EditText           mEtSearch;
    @BindView(R.id.tv_synthesis)
    TextView           mTvSynthesis;
    @BindView(R.id.tv_hashrate)
    TextView           mTvHashrate;
    @BindView(R.id.tv_price)
    TextView           mTvPrice;
    @BindView(R.id.tv_no_data)
    TextView           mTvNoData;
    @BindView(R.id.recycler_view)
    RecyclerView       mRecyclerView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.tv_hot_search)
    TextView           mTvHotSearch;
    @BindView(R.id.float_layout)
    QMUIFloatLayout    mFloatLayout;
    private MallAdapter mAdapter;
    private int         mPage = 1;

    public static void start(Context context) {
        Intent intent = new Intent(context, SearchGoodsActivity.class);
        ActivityUtils.startActivity(intent);
    }

    @Override
    public void initData(@Nullable Bundle bundle) {
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_search_goods;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState, @Nullable View contentView) {
        mPresenter = new SearchGoodsPresenter(this, this);
        applyClickListener(mTvSynthesis, mTvHashrate, mTvPrice);

        KeyboardUtils.showSoftInput(mEtSearch);
        mEtSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                search();
                return true;
            }
            return false;
        });

        mTvSynthesis.setTag(0);
        mTvHashrate.setTag(0);
        mTvPrice.setTag(0);

        mRefreshLayout.setOnRefreshListener(refreshlayout -> {
            mPage = 1;
            search();
        });
        mRefreshLayout.setOnLoadMoreListener(refreshLayout -> {
            mPage += 1;
            search();
        });

        mAdapter = new MallAdapter(new ArrayList<>());
        mAdapter.setOnItemClickListener((view, position) -> GoodsDetailActivity.start(this, mAdapter.getData().get(position).getGoodsId()));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new RecycleViewDivider(mActivity, RecycleViewDivider.VERTICAL, R.drawable.divider16));
    }

    private void search() {
        KeyboardUtils.hideSoftInput(mActivity);
        mTvHotSearch.setVisibility(View.GONE);
        mFloatLayout.setVisibility(View.GONE);
        String key = mEtSearch.getText().toString();
        if (0 == (int) mTvSynthesis.getTag()) {
            mPresenter.getGoodsList(key, "0", mPage + "");
        } else if (1 == (int) mTvHashrate.getTag()) {
            mPresenter.getGoodsList(key, "1", mPage + "");
        } else if (2 == (int) mTvHashrate.getTag()) {
            mPresenter.getGoodsList(key, "2", mPage + "");
        } else if (1 == (int) mTvPrice.getTag()) {
            mPresenter.getGoodsList(key, "3", mPage + "");
        } else if (2 == (int) mTvPrice.getTag()) {
            mPresenter.getGoodsList(key, "4", mPage + "");
        }
    }

    @Override
    public void doBusiness() {
        mPresenter.getHotSearch();
    }

    @Override
    public void onViewClick(@NonNull View view) {
        switch (view.getId()) {
            case R.id.tv_synthesis: {
                int tag = (int) mTvSynthesis.getTag();
                if (tag != 0) {
                    mTvSynthesis.setTextColor(ColorUtils.getColor(R.color.light_black));
                    mTvSynthesis.setTag(0);
                    mTvHashrate.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_sort_default, 0);
                    mTvHashrate.setTextColor(ColorUtils.getColor(R.color.light_black2));
                    mTvHashrate.setTag(0);
                    mTvPrice.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_sort_default, 0);
                    mTvPrice.setTextColor(ColorUtils.getColor(R.color.light_black2));
                    mTvPrice.setTag(0);
                    mRefreshLayout.autoRefresh();
                }
            }
            break;
            case R.id.tv_hashrate: {
                int tag = (int) mTvHashrate.getTag();
                if (tag == 0 || tag == 2) {
                    mTvSynthesis.setTextColor(ColorUtils.getColor(R.color.light_black2));
                    mTvSynthesis.setTag(1);
                    mTvHashrate.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_sort_down, 0);
                    mTvHashrate.setTextColor(ColorUtils.getColor(R.color.light_black));
                    mTvHashrate.setTag(1);
                    mTvPrice.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_sort_default, 0);
                    mTvPrice.setTextColor(ColorUtils.getColor(R.color.light_black2));
                    mTvPrice.setTag(0);
                    mRefreshLayout.autoRefresh();
                } else if (tag == 1) {
                    mTvSynthesis.setTextColor(ColorUtils.getColor(R.color.light_black2));
                    mTvSynthesis.setTag(1);
                    mTvHashrate.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_sort_up, 0);
                    mTvHashrate.setTextColor(ColorUtils.getColor(R.color.light_black));
                    mTvHashrate.setTag(2);
                    mTvPrice.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_sort_default, 0);
                    mTvPrice.setTextColor(ColorUtils.getColor(R.color.light_black2));
                    mTvPrice.setTag(0);
                    mRefreshLayout.autoRefresh();
                }
            }
            break;
            case R.id.tv_price: {
                int tag = (int) mTvPrice.getTag();
                if (tag == 0 || tag == 1) {
                    mTvSynthesis.setTextColor(ColorUtils.getColor(R.color.light_black2));
                    mTvSynthesis.setTag(1);
                    mTvHashrate.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_sort_default, 0);
                    mTvHashrate.setTextColor(ColorUtils.getColor(R.color.light_black2));
                    mTvHashrate.setTag(0);
                    mTvPrice.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_sort_up, 0);
                    mTvPrice.setTextColor(ColorUtils.getColor(R.color.light_black));
                    mTvPrice.setTag(2);
                    mRefreshLayout.autoRefresh();
                } else if (tag == 2) {
                    mTvSynthesis.setTextColor(ColorUtils.getColor(R.color.light_black2));
                    mTvSynthesis.setTag(1);
                    mTvHashrate.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_sort_default, 0);
                    mTvHashrate.setTextColor(ColorUtils.getColor(R.color.light_black2));
                    mTvHashrate.setTag(0);
                    mTvPrice.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_sort_down, 0);
                    mTvPrice.setTextColor(ColorUtils.getColor(R.color.light_black));
                    mTvPrice.setTag(1);
                    mRefreshLayout.autoRefresh();
                }
            }
            break;
            default:
                break;
        }
    }

    @Override
    public void getHotSearch(List<HotSearch> list) {
        for (HotSearch hotSearch : list) {
            View view = LayoutInflater.from(mActivity).inflate(R.layout.item_hot_search, null);
            Button btnSearch = view.findViewById(R.id.btn_search);
            btnSearch.setOnClickListener(v -> {
                KeyboardUtils.hideSoftInput(mActivity);
                mEtSearch.setText(hotSearch.getName());
                mEtSearch.setSelection(mEtSearch.getText().length());
                search();
            });
            btnSearch.setText(hotSearch.getName());
            mFloatLayout.addView(btnSearch);
        }
    }

    @Override
    public void getGoodsList(List<Goods> list) {
        mRefreshLayout.finishRefresh();
        mRefreshLayout.finishLoadMore();
        List<Goods> data = mAdapter.getData();
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