package org.jzl.android.recyclerview.core.vh;

import androidx.recyclerview.widget.RecyclerView;

public interface DataBinder<T, VH extends RecyclerView.ViewHolder> {

    void binding(VH holder, T data);

}