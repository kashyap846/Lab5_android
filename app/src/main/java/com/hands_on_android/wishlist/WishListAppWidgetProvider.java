package com.hands_on_android.wishlist;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.view.View;
import android.widget.RemoteViews;

import com.hands_on_android.wishlist.database.WishListDBHelper;
import com.hands_on_android.wishlist.model.WishListItem;

import java.util.ArrayList;


public class WishListAppWidgetProvider extends AppWidgetProvider {
    private static final int[] CONTAINER_IDS = { R.id.task1_container, R.id.task2_container };
    private static final int[] TITLE_VIEW_IDS = { R.id.task1, R.id.task2 };
    private static final int[] CATEGORY_VIEW_IDS = { R.id.task1_category, R.id.task2_category };
    private static final int MORE_VIEW_IDS = R.id.more_items;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        ArrayList<WishListItem> favourites = WishListDBHelper.getInstance().getFavouriteItems();
        for (int i = 0; i < appWidgetIds.length; i++) {
            int favouriteCount = favourites.size();
            int appWidgetId = appWidgetIds[i];
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.app_widget_wish_list);
            int moreViewCount = favouriteCount - WishLishConsts.WIDGET_CONTAINER_LIMIT;
            if(moreViewCount> 0){
                populateMoreText(remoteViews,moreViewCount);
                remoteViews.setViewVisibility(MORE_VIEW_IDS,View.VISIBLE);
            }else{
                remoteViews.setViewVisibility(MORE_VIEW_IDS,View.INVISIBLE);
            }
            for (int j = 0; j < 2; j++) {
                if (j < favouriteCount) {
                    populate(remoteViews, favourites.get(j), TITLE_VIEW_IDS[j], CATEGORY_VIEW_IDS[j]);
                    remoteViews.setViewVisibility(CONTAINER_IDS[j], View.VISIBLE);
                } else {
                    remoteViews.setViewVisibility(CONTAINER_IDS[j], View.INVISIBLE);

                }
            }
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
    }

    private void populate(RemoteViews remoteViews, WishListItem item, int titleView, int categoryView) {
    remoteViews.setTextViewText(titleView,item.getName());
    remoteViews.setTextViewText(categoryView,item.getCategory());

    }

    private void populateMoreText(RemoteViews remoteViews, int count) {
        remoteViews.setTextViewText(MORE_VIEW_IDS,formatText(count));
    }

    private String formatText(int countToDisplay) {
        if (countToDisplay <= 0 ) {
            return "";
        }

        return countToDisplay + " more item" + (countToDisplay == 1 ? "" : "s");
    }
}
