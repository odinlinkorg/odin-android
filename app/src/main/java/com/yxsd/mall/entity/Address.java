package com.yxsd.mall.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Address implements Parcelable {

    private String     searchValue;
    private String     createBy;
    private String     createTime;
    private String     updateBy;
    private String     updateTime;
    private String     remark;
    private int        id;
    private int        userId;
    private String     shippingName;
    private String     shippingAddress;
    private String     shippingAddressDetail;
    private String     shippingPhone;
    private String     avatar;

    public String getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getShippingName() {
        return shippingName;
    }

    public void setShippingName(String shippingName) {
        this.shippingName = shippingName;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getShippingAddressDetail() {
        return shippingAddressDetail;
    }

    public void setShippingAddressDetail(String shippingAddressDetail) {
        this.shippingAddressDetail = shippingAddressDetail;
    }

    public String getShippingPhone() {
        return shippingPhone;
    }

    public void setShippingPhone(String shippingPhone) {
        this.shippingPhone = shippingPhone;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.searchValue);
        dest.writeString(this.createBy);
        dest.writeString(this.createTime);
        dest.writeString(this.updateBy);
        dest.writeString(this.updateTime);
        dest.writeString(this.remark);
        dest.writeInt(this.id);
        dest.writeInt(this.userId);
        dest.writeString(this.shippingName);
        dest.writeString(this.shippingAddress);
        dest.writeString(this.shippingAddressDetail);
        dest.writeString(this.shippingPhone);
        dest.writeString(this.avatar);
    }

    public Address() {
    }

    protected Address(Parcel in) {
        this.searchValue = in.readString();
        this.createBy = in.readString();
        this.createTime = in.readString();
        this.updateBy = in.readString();
        this.updateTime = in.readString();
        this.remark = in.readString();
        this.id = in.readInt();
        this.userId = in.readInt();
        this.shippingName = in.readString();
        this.shippingAddress = in.readString();
        this.shippingAddressDetail = in.readString();
        this.shippingPhone = in.readString();
        this.avatar = in.readString();
    }

    public static final Parcelable.Creator<Address> CREATOR = new Parcelable.Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel source) {
            return new Address(source);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };
}
