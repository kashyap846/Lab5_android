package com.hands_on_android.wishlist.model;

import com.hands_on_android.wishlist.database.WishListDBHelper;

import java.util.ArrayList;

public class WishList {
    private static WishList INSTANCE;
    private ArrayList<WishListItem> wishListItems;

    public static void initialize() {
        INSTANCE = new WishList();
    }

    private WishList() {
        wishListItems = WishListDBHelper.getInstance().getItems();

    }

    public static WishList getInstance() {
        return INSTANCE;
    }

    public int getCount() {
        return wishListItems.size();
    }

    public WishListItem get(int position) {
        return wishListItems.get(position);
    }

    public void add(String name, String category) {
        WishListItem item = new WishListItem(name, category);
        wishListItems.add(0, item);
        WishListDBHelper.getInstance().insert(item);
    }

    public void remove(WishListItem item) {
        wishListItems.remove(item);
        WishListDBHelper.getInstance().delete(item);
    }
}
