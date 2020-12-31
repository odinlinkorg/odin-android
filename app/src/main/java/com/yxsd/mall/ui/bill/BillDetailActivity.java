package com.yxsd.mall.ui.bill;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.StringUtils;
import com.yxsd.mall.R;
import com.yxsd.mall.base.BaseActivity;
import com.yxsd.mall.entity.Transaction;
import com.yxsd.mall.entity.User;
import com.yxsd.mall.utils.ImageLoaderHelper;

import java.util.List;

import butterknife.BindView;

public class BillDetailActivity extends BaseActivity<BillPresenter> implements BillView {

    @BindView(R.id.iv_head_portrait)
    ImageView mIvHeadPortrait;
    @BindView(R.id.tv_nickname)
    TextView  mTvNickname;
    @BindView(R.id.tv_value)
    TextView  mTvValue;
    @BindView(R.id.tv_status)
    TextView  mTvStatus;
    @BindView(R.id.tv_transaction_type)
    TextView  mTvTransactionType;
    @BindView(R.id.tv_opposite_account)
    TextView  mTvOppositeAccount;
    @BindView(R.id.tv_transaction_time)
    TextView  mTvTransactionTime;
    @BindView(R.id.tv_order_no)
    TextView  mTvOrderNo;
    private Transaction mTransaction;

    public static void start(Context context, Transaction transaction) {
        Intent intent = new Intent(context, BillDetailActivity.class);
        intent.putExtra("transaction", transaction);
        ActivityUtils.startActivity(intent);
    }

    @Override
    public void initData(@Nullable Bundle bundle) {
        assert bundle != null;
        mTransaction = bundle.getParcelable("transaction");
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_bill_detail;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState, @Nullable View contentView) {
        mPresenter = new BillPresenter(this, this);
        setTitleText(R.string.bill_detail);

        switch (mTransaction.getOperateType()) {
            case 0: {
                mTvTransactionType.setText(R.string.transfer);
                mPresenter.getUser(mTransaction.getToAddress());
            }
            break;
            case 1: {
                mTvTransactionType.setText(R.string.withdraw);
            }
            break;
            case 2: {
                mTvTransactionType.setText(R.string.hashrate_drop);
            }
            break;
            case 3: {
                mTvTransactionType.setText(R.string.activities_presented);
            }
            break;
            case 4: {
                mTvTransactionType.setText(R.string.buy_goods);
            }
            break;
            case 5: {
                mTvTransactionType.setText(R.string.charge);
            }
            break;
            default:
                break;
        }
        if (mTransaction.getType() == 1) {
            mTvValue.setText((mTransaction.getSymbol() + mTransaction.getNum() + " " + StringUtils.getString(R.string.bgc)));
        } else if (mTransaction.getType() == 2) {
            mTvValue.setText((mTransaction.getSymbol() + mTransaction.getNum() + " " + StringUtils.getString(R.string.odin)));
        }
        switch (mTransaction.getStatus()) {
            case "0": {
                mTvStatus.setText(R.string.in_review);
                mTvStatus.setTextColor(ColorUtils.getColor(R.color.yellow));
            }
            break;
            case "1":
            case "2":
            case "3":
            case "9": {
                mTvStatus.setText(R.string.transfer_success);
                mTvStatus.setTextColor(ColorUtils.getColor(R.color.green));
            }
            break;
            case "4": {
                mTvStatus.setText(R.string.transfer_fail);
                mTvStatus.setTextColor(ColorUtils.getColor(R.color.app_color));
            }
            break;
            case "5": {
                mTvStatus.setText(R.string.review_fail);
                mTvStatus.setTextColor(ColorUtils.getColor(R.color.app_color));
            }
            break;
            default:
                break;
        }
        mTvOppositeAccount.setText(mTransaction.getToAddress());
        mTvTransactionTime.setText(mTransaction.getCreateTime());
        mTvOrderNo.setText(mTransaction.getId());
    }

    @Override
    public void doBusiness() {
    }

    @Override
    public void onViewClick(@NonNull View view) {
    }

    @Override
    public void getUser(User user) {
        ImageLoaderHelper.loadCircleImage(this, user.getAvatar(), mIvHeadPortrait);
        mTvNickname.setText(user.getUserName());
    }

    @Override
    public void getTransactionList(List<Transaction> list) {
    }
}