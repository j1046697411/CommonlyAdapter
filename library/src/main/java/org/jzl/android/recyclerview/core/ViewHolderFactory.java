package org.jzl.android.recyclerview.core;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

@FunctionalInterface
public interface ViewHolderFactory<VH extends RecyclerView.ViewHolder> {
    VH createViewHolder(View itemView, int viewType);
}
