package com.yxsd.mall.base.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ThreadUtils;

public abstract class BaseDialog extends Dialog {

    protected Activity mActivity;

    public abstract int bindLayout();

    public abstract void initView(BaseDialog dialog, View contentView);

    public abstract void setWindowStyle(Window window);

    public BaseDialog(@NonNull Context context) {
        this(context, 0);
    }

    public BaseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        Activity activity = ActivityUtils.getActivityByContext(context);
        if (activity == null) {
            throw new IllegalArgumentException("context is not instance of Activity.");
        }
        mActivity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View contentView = View.inflate(mActivity, bindLayout(), null);
        setContentView(contentView);
        initView(this, contentView);
        setWindowStyle(getWindow());
    }

    @Override
    public void show() {
        ThreadUtils.runOnUiThread(() -> {
            if (ActivityUtils.isActivityAlive(getContext())) {
                BaseDialog.super.show();
            }
        });
    }

    @Override
    public void dismiss() {
        ThreadUtils.runOnUiThread(() -> {
            if (ActivityUtils.isActivityAlive(getContext())) {
                BaseDialog.super.dismiss();
            }
        });
    }
}