package com.yxsd.mall.ui.login;

import android.app.Activity;

import com.yxsd.mall.base.BasePresenter;
import com.yxsd.mall.entity.User;
import com.yxsd.mall.http.HttpCallback;
import com.yxsd.mall.utils.EncryptUtils;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

class LoginPresenter extends BasePresenter<LoginView> {

    LoginPresenter(Activity activity, LoginView view) {
        super(activity, view);
    }

    public void getCode(String phone, String clientIp, String vaptchaToken) {

        mCompositeSubscription.add(mHttpManager.sendMessage(phone, clientIp, vaptchaToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpCallback<Object>(this) {
                    @Override
                    public void onResponse(Object o) {
                    }
                }));
    }

    public void accountLogin(String phone, String password) {
        mCompositeSubscription.add(mHttpManager.login(phone, EncryptUtils.encodeBase64(password))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpCallback<User>(this) {
                    @Override
                    public void onResponse(User user) {
                        mView.login(user);
                    }
                }));
    }

    public void smsLogin(String phone, String verifyCode) {
        mCompositeSubscription.add(mHttpManager.msglogin(phone, verifyCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpCallback<User>(this) {
                    @Override
                    public void onResponse(User user) {
                        mView.login(user);
                    }
                }));
    }

    public void register(String phone, String verifyCode, String loginPassword, String payPassword, String inviteCode) {
        mCompositeSubscription.add(mHttpManager.register(phone,
                EncryptUtils.encodeBase64(loginPassword),
                verifyCode,
                EncryptUtils.encodeBase64(payPassword),
                inviteCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpCallback<User>(this) {
                    @Override
                    public void onResponse(User user) {
                        mView.register(user);
                    }
                }));
    }

    public void modifyPassword(String phone, String verifyCode, String password) {
        mCompositeSubscription.add(mHttpManager.forgetPassword(phone, EncryptUtils.encodeBase64(password), verifyCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpCallback<Object>(this) {
                    @Override
                    public void onResponse(Object o) {
                        mView.modifyPassword();
                    }
                }));
    }
}
