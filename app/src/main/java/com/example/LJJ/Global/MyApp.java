package com.example.LJJ.Global;

import com.amap.api.maps.model.LatLng;
import com.example.LJJ.MyUser.User;

import org.litepal.LitePalApplication;

/**
 * Created by Isuk on 2018/4/21.
 */

public class MyApp extends LitePalApplication{//存储全局变量,包括当前位置，经纬度，用户信息等
    private User user;
    private LatLng nowLoc;
    private boolean iswait;
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isIswait() {
        return iswait;
}

    public void setIswait(boolean iswait) {
        this.iswait = iswait;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LatLng getNowLoc() {
        return nowLoc;
    }

    public void setNowLoc(LatLng nowLoc) {
        this.nowLoc = nowLoc;
    }

    @Override
    public void onCreate(){
        super.onCreate();
    }
}
