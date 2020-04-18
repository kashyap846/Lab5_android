package com.hands_on_android.wishlist.database;

import android.provider.BaseColumns;

public class WishListContract {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "WishList.db";

    //This class is not meant to be initialized
    private WishListContract() {
    }

    public static class WishListEntry implements BaseColumns {
        //Name of the table
        public static final String TABLE_NAME = "wishListEntry";

        //Listing all columns with a format COLUMN_NAME_<name of column>
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_CATEGORY = "category";
        public static final String COLUMN_NAME_IS_FAVOURITE = "isFavourite";
        public static final String COLUMN_NAME_CREATION_TIME = "creationTime";
    }
}
