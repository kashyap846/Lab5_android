package com.hands_on_android.wishlist.model;

import com.hands_on_android.wishlist.database.WishListDBHelper;

import java.util.Date;
import java.util.Random;

public class WishListItem {
    private long id;
    private String name;
    private String category;
    private boolean isFavourite;
    private long creationTime;

    //Constructor for adding a new wish list item
    public WishListItem(String name, String category) {
        this(new Random().nextLong(), name, category, false, new Date().getTime());
    }

    public WishListItem(long id, String name, String category, boolean isFavourite, long creationTime) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.isFavourite = isFavourite;
        this.creationTime = creationTime;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
        WishListDBHelper.getInstance().updateFav(this);
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public long getCreationTime() {
        return creationTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WishListItem that = (WishListItem) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
