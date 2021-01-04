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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.SpanUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.yxsd.mall.R;
import com.yxsd.mall.base.BaseActivity;
import com.yxsd.mall.config.Constant;
import com.yxsd.mall.entity.User;
import com.yxsd.mall.ui.home.HomeActivity;
import com.yxsd.mall.ui.setting.PrivacyAgreementActivity;
import com.yxsd.mall.utils.SPManager;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;

public class RegisterActivity extends BaseActivity<LoginPresenter> implements LoginView {

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
    @BindView(R.id.et_pay_password)
    EditText        mEtPayPassword;
    @BindView(R.id.et_invite_code)
    EditText        mEtInviteCode;
    @BindView(R.id.btn_register_now)
    QMUIRoundButton mBtnRegisterNow;
    @BindView(R.id.check_box)
    CheckBox        mCheckBox;
    @BindView(R.id.tv_agreement)
    TextView        mTvAgreement;
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
        Intent intent = new Intent(context, RegisterActivity.class);
        ActivityUtils.startActivity(intent);
    }

    @Override
    public void initData(@Nullable Bundle bundle) {
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_register;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState, @Nullable View contentView) {
        mPresenter = new LoginPresenter(this, this);
        applyClickListener(mBtnGetVerifyCode, mBtnRegisterNow, mTvAgreement);

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

        SpanUtils.with(mTvAgreement)
                .append(getString(R.string.register_agree))
                .setForegroundColor(ColorUtils.getColor(R.color.light_black3))
                .append(getString(R.string.service_agreement))
                .setForegroundColor(ColorUtils.getColor(R.color.app_color))
                .create();
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
                {
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
            }
            break;
            case R.id.btn_register_now: {
                String phone = mEtPhone.getText().toString();
                String verifyCode = mEtVerifyCode.getText().toString();
                String loginPassword = mEtLoginPassword.getText().toString();
                String payPassword = mEtPayPassword.getText().toString();
                String inviteCode = mEtInviteCode.getText().toString();
                if (StringUtils.isEmpty(phone)) {
                    ToastUtils.showShort(mEtPhone.getHint());
                } else if (!RegexUtils.isMatch(Constant.REGEX_MOBILE_EXACT, phone)) {
                    ToastUtils.showShort(R.string.phone_format_error);
                } else if (StringUtils.isEmpty(verifyCode)) {
                    ToastUtils.showShort(R.string.input_verify_code);
                } else if (verifyCode.length() != 6) {
                    ToastUtils.showShort(R.string.verify_code_format_error);
                } else if (loginPassword.length() < 6) {
                    ToastUtils.showShort(mEtLoginPassword.getHint());
                } else if (StringUtils.isEmpty(payPassword)
                        || !RegexUtils.isMatch(Constant.REGEX_PAY_PASSWORD, payPassword)) {
                    ToastUtils.showShort(mEtPayPassword.getHint());
                } else if (!mCheckBox.isChecked()) {
                    ToastUtils.showShort(R.string.agree_service_agreement);
                } else {
                    mPresenter.register(phone, verifyCode, loginPassword, payPassword, inviteCode);
                }
            }
            break;
            case R.id.tv_agreement: {
                PrivacyAgreementActivity.start(this);
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
        SPManager.setUser(user.getUserId(),
                user.getToken(),
                user.getCreateTime(),
                user.getUserName(),
                user.getInviteCode(),
                user.getAvatar(),
                user.getLoginName(),
                user.getSex(),
                user.getRemake());
        HomeActivity.start(this);
    }

    @Override
    public void modifyPassword() {
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