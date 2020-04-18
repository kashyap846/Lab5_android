package com.hands_on_android.wishlist.database;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.hands_on_android.wishlist.WishListAppWidgetProvider;
import com.hands_on_android.wishlist.model.WishList;
import com.hands_on_android.wishlist.model.WishListItem;

import java.util.ArrayList;

public class WishListDBHelper extends SQLiteOpenHelper {
    private static final String SQL_CREATE = "CREATE TABLE " + WishListContract.WishListEntry.TABLE_NAME + " (" +
            WishListContract.WishListEntry._ID + " INTEGER PRIMARY KEY," +
            WishListContract.WishListEntry.COLUMN_NAME_NAME+ " TEXT," +
            WishListContract.WishListEntry.COLUMN_NAME_CATEGORY + " TEXT," +
            WishListContract.WishListEntry.COLUMN_NAME_IS_FAVOURITE + " INTEGER," +
            WishListContract.WishListEntry.COLUMN_NAME_CREATION_TIME + " INTEGER)";

    private static WishListDBHelper INSTANCE;

    public static WishListDBHelper getInstance() {
        return INSTANCE;
    }

    public static void initialize(Context context) {
        INSTANCE = new WishListDBHelper(context);
        WishList.initialize();
    }

    private Context context;

    private WishListDBHelper(@Nullable Context context) {
        super(context, WishListContract.DATABASE_NAME, null, WishListContract.DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
        insert(db, new WishListItem("Item 1", "Category 1"));
        insert(db, new WishListItem("Item 2", "Category 2"));
        insert(db, new WishListItem("Item 3", "Category 3"));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void sendAppWidgetBroadcast() {
        Intent appWidgetIntent = new Intent(context, WishListAppWidgetProvider.class);
        appWidgetIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] widgetIdsArray = appWidgetManager.getAppWidgetIds(new ComponentName(context,WishListAppWidgetProvider.class));
        appWidgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,widgetIdsArray);
        context.sendBroadcast(appWidgetIntent);
    }

    public ArrayList<WishListItem> getItems() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<WishListItem> result = new ArrayList<>();

        String sortOrder = WishListContract.WishListEntry.COLUMN_NAME_CREATION_TIME+ " DESC";
        Cursor cursor = db.query(
                WishListContract.WishListEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                sortOrder
        );

        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndex(WishListContract.WishListEntry._ID));
            String name = cursor.getString(cursor.getColumnIndex(WishListContract.WishListEntry.COLUMN_NAME_NAME));
            String category = cursor.getString(cursor.getColumnIndex(WishListContract.WishListEntry.COLUMN_NAME_CATEGORY));
            boolean isFavourite = cursor.getInt(cursor.getColumnIndex(WishListContract.WishListEntry.COLUMN_NAME_IS_FAVOURITE)) == 1;
            long creationTime = cursor.getLong(cursor.getColumnIndex(WishListContract.WishListEntry.COLUMN_NAME_CREATION_TIME));
            result.add(new WishListItem(id, name, category, isFavourite, creationTime));
        }
        cursor.close();

        return result;
    }

    public ArrayList<WishListItem> getFavouriteItems() {
        SQLiteDatabase db = getReadableDatabase();
        String sortOrder = WishListContract.WishListEntry.COLUMN_NAME_CREATION_TIME+ " DESC";
        String selection = WishListContract.WishListEntry.COLUMN_NAME_IS_FAVOURITE + "= 1";
        Cursor dbCursor = db.query(
          WishListContract.WishListEntry.TABLE_NAME,
                null,
                selection,
                null,
                null,
                null,
        sortOrder

        );
        ArrayList<WishListItem> result = new ArrayList<>();
        while (dbCursor.moveToNext()){
            long id =dbCursor.getLong(dbCursor.getColumnIndex(WishListContract.WishListEntry._ID));
            String nameOfWish = dbCursor.getString(dbCursor.getColumnIndex(WishListContract.WishListEntry.COLUMN_NAME_NAME));
            boolean isFav = dbCursor.getInt(dbCursor.getColumnIndex(WishListContract.WishListEntry.COLUMN_NAME_IS_FAVOURITE)) == 1;
            long createdTime = dbCursor.getLong(dbCursor.getColumnIndex(WishListContract.WishListEntry.COLUMN_NAME_CREATION_TIME));
            String categoryOfWish = dbCursor.getString(dbCursor.getColumnIndex(WishListContract.WishListEntry.COLUMN_NAME_CATEGORY));
            result.add(new WishListItem(id, nameOfWish, categoryOfWish, isFav, createdTime));
        }
        dbCursor.close();


        return result;
    }


    private void insert(SQLiteDatabase db, WishListItem itemToAdd) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(WishListContract.WishListEntry._ID,itemToAdd.getId());
        contentValues.put(WishListContract.WishListEntry.COLUMN_NAME_IS_FAVOURITE,itemToAdd.isFavourite());
        contentValues.put(WishListContract.WishListEntry.COLUMN_NAME_NAME,itemToAdd.getName());
        contentValues.put(WishListContract.WishListEntry.COLUMN_NAME_CREATION_TIME,itemToAdd.getCreationTime());
        contentValues.put(WishListContract.WishListEntry.COLUMN_NAME_CATEGORY,itemToAdd.getCategory());
        db.insert(WishListContract.WishListEntry.TABLE_NAME,null,contentValues);
        sendAppWidgetBroadcast();
    }

    public void insert(WishListItem itemToAdd) {
        SQLiteDatabase db = getWritableDatabase();
        insert(db, itemToAdd);
    }

    public void updateFav(WishListItem item) {
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = WishListContract.WishListEntry._ID + "= ?";
        String[] whereArgs = { String.valueOf(item.getId()) };

        ContentValues values = new ContentValues();
        values.put(WishListContract.WishListEntry.COLUMN_NAME_IS_FAVOURITE, item.isFavourite());

        db.update(WishListContract.WishListEntry.TABLE_NAME, values, whereClause, whereArgs);
        sendAppWidgetBroadcast();
    }

    public void delete(WishListItem item) {
        String where = WishListContract.WishListEntry._ID + "= ?";
        String[] whereArgsArray = {String.valueOf(item.getId())};
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete(WishListContract.WishListEntry.TABLE_NAME,where,whereArgsArray);
        sendAppWidgetBroadcast();
    }
}
