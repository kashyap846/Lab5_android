package com.hands_on_android.wishlist.listeners;

import com.hands_on_android.wishlist.model.WishListItem;

public interface OnItemChangedListener {
    void onItemRemoved(WishListItem item);
    void onFavClicked(WishListItem item);
}
