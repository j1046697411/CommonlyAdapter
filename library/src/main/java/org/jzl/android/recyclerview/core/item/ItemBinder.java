package org.jzl.android.recyclerview.core.item;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public interface ItemBinder<T, VH extends RecyclerView.ViewHolder> {

    void binding(VH holder, T data, List<Object> payloads);

}
