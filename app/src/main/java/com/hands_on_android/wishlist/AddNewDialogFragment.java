package com.hands_on_android.wishlist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.textfield.TextInputEditText;
import com.hands_on_android.wishlist.listeners.OnNewItemListener;
import com.hands_on_android.wishlist.model.WishList;

public class AddNewDialogFragment extends DialogFragment {

    public static class Builder {
        private AddNewDialogFragment fragment = new AddNewDialogFragment();

        public Builder addOnNewItemListener(OnNewItemListener listener) {
            fragment.listener = listener;
            return this;
        }

        public void show(FragmentManager fragmentManager, String tag) {
            fragment.show(fragmentManager, tag);
        }
    }

    private AddNewDialogFragment() {
        //Make sure the dialog can only be created through builder
    }

    private OnNewItemListener listener;

    private TextInputEditText nameEditText;
    private TextInputEditText categoryEditText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View layout = inflater.inflate(R.layout.fragment_add_new, container, false);

        nameEditText = layout.findViewById(R.id.name);
        categoryEditText = layout.findViewById(R.id.category);

        Button addButton = layout.findViewById(R.id.add);
        addButton.setOnClickListener(this::addButtonClick);

        return layout;
    }

    private void addButtonClick(View view) {
        if (nameEditText.getText() == null || categoryEditText.getText() == null) {
            return;
        }
        String name = nameEditText.getText().toString();
        String category = categoryEditText.getText().toString();
        if (name.isEmpty() || category.isEmpty()) {
            return;
        }

        WishList.getInstance().add(name, category);
        if (listener != null) {
            listener.onNewItem();
        }
        dismiss();
    }
}