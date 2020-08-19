package org.jzl.android.recyclerview.core.item;

import androidx.recyclerview.widget.RecyclerView;

public interface DataBinder<T, VH extends RecyclerView.ViewHolder> {

    void binding(VH holder, T data);

}
