package org.jzl.android.recyclerview.core.item;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import org.jzl.android.recyclerview.data.DataProvider;

import java.util.List;

public interface ItemViewManager<T, VH extends RecyclerView.ViewHolder> {

    VH onCreateViewHolder(LayoutInflater layoutInflater, ViewGroup parent, int viewType);

    void bindItemViewData(DataProvider.DataBlock dataBlock, VH holder, T data, List<Object> payloads);
}
