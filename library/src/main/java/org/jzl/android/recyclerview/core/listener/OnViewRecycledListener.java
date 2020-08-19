package org.jzl.android.recyclerview.core.listener;

import androidx.recyclerview.widget.RecyclerView;

public interface OnViewRecycledListener<VH extends RecyclerView.ViewHolder> {
    void onViewRecycled(VH holder);
}
