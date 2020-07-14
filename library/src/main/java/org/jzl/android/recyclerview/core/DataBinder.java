package org.jzl.android.recyclerview.core;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

@FunctionalInterface
public interface DataBinder<T, VH extends RecyclerView.ViewHolder> {

    void bind(VH holder, T data, List<Object> payloads);
}
