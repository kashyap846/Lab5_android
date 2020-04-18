package com.hands_on_android.wishlist;

import android.app.Application;

import com.hands_on_android.wishlist.database.WishListDBHelper;

public class WishListApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        WishListDBHelper.initialize(getApplicationContext());
    }
}
