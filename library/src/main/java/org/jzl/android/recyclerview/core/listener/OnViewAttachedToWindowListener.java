package org.jzl.android.recyclerview.core.listener;

import androidx.recyclerview.widget.RecyclerView;

public interface OnViewAttachedToWindowListener<VH extends RecyclerView.ViewHolder> {

    void onViewAttachedToWindow(VH holder);

}
