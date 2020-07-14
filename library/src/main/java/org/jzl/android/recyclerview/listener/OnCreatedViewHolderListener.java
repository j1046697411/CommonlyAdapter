package org.jzl.android.recyclerview.listener;

import androidx.recyclerview.widget.RecyclerView;

public interface OnCreatedViewHolderListener<VH extends RecyclerView.ViewHolder> {

    void onCreatedViewHolder(VH holder);
}
