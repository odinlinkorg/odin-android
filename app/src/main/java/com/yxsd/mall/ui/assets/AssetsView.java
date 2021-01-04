package com.yxsd.mall.ui.assets;

import com.yxsd.mall.base.IView;
import com.yxsd.mall.entity.Transaction;
import com.yxsd.mall.entity.WalletBalance;

import java.util.List;

interface AssetsView extends IView {
    void getWalletBalance(WalletBalance walletBalance);

    void getTransactionList(List<Transaction> list);
}
