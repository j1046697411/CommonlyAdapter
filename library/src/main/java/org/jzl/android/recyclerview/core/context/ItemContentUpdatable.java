package org.jzl.android.recyclerview.core.context;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public interface ItemContentUpdatable<T, VH extends RecyclerView.ViewHolder> {

    void update(VH holder, T data, List<Object> payloads);

}
