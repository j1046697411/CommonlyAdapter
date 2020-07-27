package org.jzl.android.recyclerview.listener;

import androidx.recyclerview.widget.RecyclerView;

public interface OnFailedToRecycleViewListener<VH extends RecyclerView.ViewHolder> {

    boolean onFailedToRecycleView(VH holder);

}
