package org.jzl.android.recyclerview.listener;

import androidx.recyclerview.widget.RecyclerView;

public interface OnViewDetachedFromWindowListener<VH extends RecyclerView.ViewHolder> {

    void onViewDetachedFromWindow(VH holder);

}
