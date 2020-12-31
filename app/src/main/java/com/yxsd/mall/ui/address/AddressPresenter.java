package com.yxsd.mall.ui.address;

import android.app.Activity;
import android.content.res.AssetManager;
import android.text.TextUtils;

import com.blankj.utilcode.util.Utils;
import com.google.gson.Gson;
import com.yxsd.mall.base.BasePresenter;
import com.yxsd.mall.entity.Address;
import com.yxsd.mall.entity.JsonArea;
import com.yxsd.mall.entity.PickerData;
import com.yxsd.mall.http.HttpCallback;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

class AddressPresenter extends BasePresenter<AddressView> {

    AddressPresenter(Activity activity, AddressView view) {
        super(activity, view);
    }

    public void getAddress() {
        mCompositeSubscription.add(mHttpManager.addressQuery()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpCallback<List<Address>>(this, false) {
                    @Override
                    public void onResponse(List<Address> list) {
                        mView.getAddress(list);
                    }
                }));
    }

    public void addAddress(String name, String phone, String address, String detail) {
        mCompositeSubscription.add(mHttpManager.addressAdd(name, address, detail, phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpCallback<Object>(this) {
                    @Override
                    public void onResponse(Object o) {
                        mView.addAddress();
                    }
                }));
    }

    public void updateAddress(String id, String name, String phone, String address, String detail) {
        mCompositeSubscription.add(mHttpManager.addressUpdate(id, name, address, detail, phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpCallback<Object>(this) {
                    @Override
                    public void onResponse(Object o) {
                        mView.updateAddress();
                    }
                }));
    }

    public void deleteAddress(String id) {
        mCompositeSubscription.add(mHttpManager.addressRemove(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpCallback<Object>(this) {
                    @Override
                    public void onResponse(Object o) {
                        mView.deleteAddress();
                    }
                }));
    }

    public void loadData() {
        mCompositeSubscription.add(Observable.just("area.json")
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(this::showLoading)
                .observeOn(Schedulers.io())
                .map(this::parseData)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnCompleted(this::dismiss)
                .doOnError(throwable -> dismiss())
                .subscribe(pickerData -> mView.loadData(pickerData)));
    }

    private PickerData parseData(String file) {
        PickerData pickerData = new PickerData();
        ArrayList<JsonArea> options1Items = new ArrayList<>();
        ArrayList<ArrayList<JsonArea>> options2Items = new ArrayList<>();
        ArrayList<ArrayList<ArrayList<JsonArea>>> options3Items = new ArrayList<>();

        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = Utils.getApp().getAssets();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                    assetManager.open(file)));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<JsonArea> jsonAreaList = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(stringBuilder.toString());
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonArea entity = gson.fromJson(data.optJSONObject(i).toString(), JsonArea.class);
                jsonAreaList.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (JsonArea jsonArea : jsonAreaList) {
            if (TextUtils.equals(jsonArea.getRegion_type(), "1")) {
                options1Items.add(jsonArea);
            }
        }

        for (JsonArea options1Item : options1Items) {
            ArrayList<JsonArea> cityList = new ArrayList<>();
            for (JsonArea jsonArea : jsonAreaList) {
                if (TextUtils.equals(options1Item.getRegion_id(), jsonArea.getParent_id())
                        && TextUtils.equals(jsonArea.getRegion_type(), "2")) {
                    cityList.add(jsonArea);
                }
            }
            options2Items.add(cityList);
        }

        for (ArrayList<JsonArea> options2Item : options2Items) {
            ArrayList<ArrayList<JsonArea>> cityAreaList = new ArrayList<>();
            for (JsonArea jsonArea : options2Item) {
                ArrayList<JsonArea> areaList = new ArrayList<>();
                for (JsonArea bean : jsonAreaList) {
                    if (TextUtils.equals(jsonArea.getRegion_id(), bean.getParent_id())
                            && TextUtils.equals(bean.getRegion_type(), "3")) {
                        areaList.add(bean);
                    }
                }
                cityAreaList.add(areaList);
            }
            options3Items.add(cityAreaList);
        }

        pickerData.setOptions1Items(options1Items);
        pickerData.setOptions2Items(options2Items);
        pickerData.setOptions3Items(options3Items);
        return pickerData;
    }
}
