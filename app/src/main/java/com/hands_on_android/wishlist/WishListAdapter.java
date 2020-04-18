package com.hands_on_android.wishlist;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hands_on_android.wishlist.listeners.OnItemChangedListener;
import com.hands_on_android.wishlist.model.WishList;
import com.hands_on_android.wishlist.model.WishListItem;

public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.WishListViewHolder> implements OnItemChangedListener {

    public WishListAdapter() {
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return WishList.getInstance().get(position).getId();
    }

    @NonNull
    @Override
    public WishListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_wish_list_item, parent, false);
        return new WishListViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull WishListViewHolder holder, int position) {
        holder.populate(WishList.getInstance().get(position), this);
    }

    @Override
    public int getItemCount() {
        return WishList.getInstance().getCount();
    }

    @Override
    public void onItemRemoved(WishListItem item) {
        WishList.getInstance().remove(item);
        notifyDataSetChanged();
    }

    @Override
    public void onFavClicked(WishListItem item) {
        item.setFavourite(!item.isFavourite());
    }

    static class WishListViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
        private TextView categoryTextView;
        private View favButtonContainer;
        private View favButton;

        private WishListItem wishListItem;
        private OnItemChangedListener listener;

        WishListViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.title);
            categoryTextView = itemView.findViewById(R.id.category);
            favButton = itemView.findViewById(R.id.fav_button);
            favButtonContainer = itemView.findViewById(R.id.fav_button_container);
        }

        public void populate(WishListItem item, OnItemChangedListener listener) {
            this.wishListItem = item;
            this.listener = listener;

            titleTextView.setText(item.getName());
            categoryTextView.setText(item.getCategory());
            favButton.setSelected(item.isFavourite());
            favButtonContainer.setOnClickListener(v -> {
                this.listener.onFavClicked(this.wishListItem);
                favButton.setSelected(!favButton.isSelected());
            });
            itemView.setOnClickListener(this::onItemClick);
        }

        private void onItemClick(View v) {
            new AlertDialog.Builder(v.getContext())
                    .setTitle(R.string.wish_list_delete_title)
                    .setMessage(R.string.wish_list_delete_message)
                    .setPositiveButton(R.string.delete, (a,b) ->  listener.onItemRemoved(wishListItem) )
                    .setNegativeButton(R.string.cancel, (a,b) -> {})
                    .show();

        }
    }
}
