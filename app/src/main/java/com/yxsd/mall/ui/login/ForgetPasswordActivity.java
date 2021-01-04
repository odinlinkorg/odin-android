package com.yxsd.mall.ui.login;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.yxsd.mall.R;
import com.yxsd.mall.base.BaseActivity;
import com.yxsd.mall.config.Constant;
import com.yxsd.mall.entity.User;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;

public class ForgetPasswordActivity extends BaseActivity<LoginPresenter> implements LoginView {

    public static final String PASS   = "pass";//通过
    public static final String CANCEL = "cancel";//取消
    @BindView(R.id.et_phone)
    EditText        mEtPhone;
    @BindView(R.id.et_verify_code)
    EditText        mEtVerifyCode;
    @BindView(R.id.btn_get_verify_code)
    QMUIRoundButton mBtnGetVerifyCode;
    @BindView(R.id.et_login_password)
    EditText        mEtLoginPassword;
    @BindView(R.id.et_login_password_again)
    EditText        mEtLoginPasswordAgain;
    @BindView(R.id.btn_reset_password)
    QMUIRoundButton mBtnResetPassword;
    @BindView(R.id.web_view)
    WebView         mWebView;
    private CountDownTimer mCountDownTimer;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, ForgetPasswordActivity.class);
        ActivityUtils.startActivity(intent);
    }

    @Override
    public void initData(@Nullable Bundle bundle) {
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_forget_password;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState, @Nullable View contentView) {
        mPresenter = new LoginPresenter(this, this);
        applyClickListener(mBtnGetVerifyCode, mBtnResetPassword);

        mCountDownTimer = new CountDownTimer(Constant.SMS_WAIT_TIME * 1000, 1000) {
            @Override
            public void onTick(long millisLeft) {
                mBtnGetVerifyCode.setEnabled(false);
                mBtnGetVerifyCode.setFocusable(false);
                mBtnGetVerifyCode.setText(String.format(getString(R.string.seconds), millisLeft / 1000 + ""));
            }

            @Override
            public void onFinish() {
                mBtnGetVerifyCode.setEnabled(true);
                mBtnGetVerifyCode.setFocusable(true);
                mBtnGetVerifyCode.setText(getString(R.string.get_verify_code));
            }
        };
        setVaptcha();
    }

    private void setVaptcha() {
        //设置mWebView颜色为透明
        mWebView.setBackgroundColor(Color.TRANSPARENT);
        mWebView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        // 禁止缓存加载，以确保可获取最新的验证图片。
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        // 设置不使用默认浏览器，而直接使用WebView组件加载页面。
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        // 设置WebView组件支持加载JavaScript。
        mWebView.getSettings().setJavaScriptEnabled(true);
        // 建立JavaScript调用Java接口的桥梁。
        mWebView.addJavascriptInterface(new VaptchaInterface(), "vaptchaInterface");
        mWebView.setBackgroundColor(Color.TRANSPARENT);
        mWebView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    public void doBusiness() {
    }

    @Override
    public void onViewClick(@NonNull View view) {
        switch (view.getId()) {
            case R.id.btn_get_verify_code: {
                String phone = mEtPhone.getText().toString();
                if (StringUtils.isEmpty(phone)) {
                    ToastUtils.showShort(mEtPhone.getHint());
                } else if (!RegexUtils.isMatch(Constant.REGEX_MOBILE_EXACT, phone)) {
                    ToastUtils.showShort(R.string.phone_format_error);
                } else {
                    mWebView.setVisibility(View.VISIBLE);
                    mWebView.loadUrl("https://v.vaptcha.com/app/android.html?vid=&scene=0&lang=zh-CN&offline_server=https://www.vaptchadowntime.com/dometime");
                }
            }
            break;
            case R.id.btn_reset_password: {
                String phone = mEtPhone.getText().toString();
                String verifyCode = mEtVerifyCode.getText().toString();
                String password = mEtLoginPassword.getText().toString();
                String passwordAgain = mEtLoginPasswordAgain.getText().toString();
                if (StringUtils.isEmpty(phone)) {
                    ToastUtils.showShort(mEtPhone.getHint());
                } else if (!RegexUtils.isMatch(Constant.REGEX_MOBILE_EXACT, phone)) {
                    ToastUtils.showShort(R.string.phone_format_error);
                } else if (StringUtils.isEmpty(verifyCode)) {
                    ToastUtils.showShort(R.string.input_verify_code);
                } else if (verifyCode.length() != 6) {
                    ToastUtils.showShort(R.string.verify_code_format_error);
                } else if (StringUtils.isEmpty(password)) {
                    ToastUtils.showShort(R.string.input_password);
                } else if (password.length() < 6) {
                    ToastUtils.showShort(R.string.input_login_password);
                } else if (!StringUtils.equals(password, passwordAgain)) {
                    ToastUtils.showShort(R.string.password_different);
                } else {
                    mPresenter.modifyPassword(phone, verifyCode, password);
                }
            }
            break;
            default:
                break;
        }
    }

    @Override
    public void login(User user) {
    }

    @Override
    public void register(User user) {
    }

    @Override
    public void modifyPassword() {
        ToastUtils.showShort(R.string.reset_password_success);
        finish();
    }

    public class VaptchaInterface {

        @JavascriptInterface
        public void signal(String json) {
            //json格式{signal:"",data:""}
            //signal: pass (通过) ； cancel（取消）
            try {
                JSONObject jsonObject = new JSONObject(json);
                String signal = jsonObject.getString("signal");
                if (PASS.equals(signal)) {//通过
                    runOnUiThread(() -> {
                        ToastUtils.showShort(R.string.verify_pass);
                        mWebView.setVisibility(View.GONE);
                        mCountDownTimer.start();
                        try {
                            mPresenter.getCode(mEtPhone.getText().toString(), NetworkUtils.getIPAddress(true), jsonObject.getString("data"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    });
                } else if (CANCEL.equals(signal)) {//取消
                    runOnUiThread(() -> {
                        ToastUtils.showShort(R.string.verify_cancel);
                        mWebView.setVisibility(View.GONE);
                    });
                } else {//其他html页面返回的状态参数
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}