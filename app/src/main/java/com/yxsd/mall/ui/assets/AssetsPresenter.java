package com.yxsd.mall.ui.assets;

import android.app.Activity;

import com.yxsd.mall.base.BasePresenter;
import com.yxsd.mall.entity.Transaction;
import com.yxsd.mall.entity.WalletBalance;
import com.yxsd.mall.http.ExceptionHandler;
import com.yxsd.mall.http.HttpCallback;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

class AssetsPresenter extends BasePresenter<AssetsView> {

    AssetsPresenter(Activity activity, AssetsView view) {
        super(activity, view);
    }

    public void getWalletBalance() {
        mCompositeSubscription.add(mHttpManager.walletBalance()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpCallback<WalletBalance>(this, false) {
                    @Override
                    public void onResponse(WalletBalance walletBalance) {
                        mView.getWalletBalance(walletBalance);
                    }
                }));
    }

    public void getTransactionList(String type, String page) {
        mCompositeSubscription.add(mHttpManager.recordTrans(type, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpCallback<List<Transaction>>(this, false) {
                    @Override
                    public void onResponse(List<Transaction> list) {
                        mView.getTransactionList(list);
                    }

                    @Override
                    public void onErrors(ExceptionHandler.ResponseThrowable throwable) {
                        super.onErrors(throwable);
                        mView.getTransactionList(null);
                    }
                }));
    }
}
