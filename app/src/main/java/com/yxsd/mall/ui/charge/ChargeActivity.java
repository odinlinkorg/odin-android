package com.yxsd.mall.ui.charge;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ActivityUtils;
import com.yxsd.mall.R;
import com.yxsd.mall.base.BaseActivity;
import com.yxsd.mall.entity.ChargeData;
import com.yxsd.mall.utils.ClipboardUtils;
import com.yxsd.mall.utils.ShareUtils;
import com.yxsd.mall.widget.WindowInsetConstraintLayout;

import butterknife.BindView;

public class ChargeActivity extends BaseActivity<ChargePresenter> implements ChargeView {

    @BindView(R.id.image_view)
    ImageView                   mImageView;
    @BindView(R.id.tv_address)
    TextView                    mTvAddress;
    @BindView(R.id.tv_save_image)
    TextView                    mTvSaveImage;
    @BindView(R.id.tv_copy_address)
    TextView                    mTvCopyAddress;
    @BindView(R.id.layout_root)
    WindowInsetConstraintLayout mLayoutRoot;

    public static void start(Context context) {
        Intent intent = new Intent(context, ChargeActivity.class);
        ActivityUtils.startActivity(intent);
    }

    @Override
    public void initData(@Nullable Bundle bundle) {
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_charge;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState, @Nullable View contentView) {
        mPresenter = new ChargePresenter(this, this);
        setTitleText(R.string.charge);
        applyClickListener(mTvSaveImage, mTvCopyAddress);
    }

    @Override
    public void doBusiness() {
        mPresenter.getChargeData();
    }

    @Override
    public void onViewClick(@NonNull View view) {
        switch (view.getId()) {
            case R.id.tv_save_image: {
                ShareUtils.save(this, mLayoutRoot);
            }
            break;
            case R.id.tv_copy_address: {
                ClipboardUtils.copyText(mTvAddress.getText().toString());
            }
            break;
            default:
                break;
        }
    }

    @Override
    public void getChargeData(ChargeData chargeData) {
        mPresenter.createQRCode(chargeData.getAddress());
        mTvAddress.setText(chargeData.getAddress());
    }

    @Override
    public void createQRCode(Bitmap bitmap) {
        mImageView.setImageBitmap(bitmap);
    }
}