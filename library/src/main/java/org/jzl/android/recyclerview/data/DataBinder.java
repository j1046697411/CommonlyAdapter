package org.jzl.android.recyclerview.data;

import androidx.recyclerview.widget.RecyclerView;

import org.jzl.android.recyclerview.core.CAContext;

public interface DataBinder<T, VH extends RecyclerView.ViewHolder> {

    void bind(CAContext context, VH holder, T data);

}
