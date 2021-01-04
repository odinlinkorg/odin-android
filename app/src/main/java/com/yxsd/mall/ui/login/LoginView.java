package com.yxsd.mall.ui.login;

import com.yxsd.mall.base.IView;
import com.yxsd.mall.entity.User;

interface LoginView extends IView {
    void login(User user);

    void register(User user);

    void modifyPassword();
}
