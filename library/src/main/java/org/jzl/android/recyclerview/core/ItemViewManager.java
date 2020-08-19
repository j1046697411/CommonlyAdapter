package org.jzl.android.recyclerview.core;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import org.jzl.android.recyclerview.core.context.ItemContext;
import org.jzl.android.recyclerview.core.item.DataBinder;
import org.jzl.android.recyclerview.core.item.DataBindingMatchPolicy;
import org.jzl.android.recyclerview.core.item.ItemBindingMatchPolicy;
import org.jzl.android.recyclerview.core.item.ItemViewFactory;

public interface ItemViewManager<T, VH extends RecyclerView.ViewHolder> {

    VH onCreateViewHolder(LayoutInflater layoutInflater, ViewGroup parent, int viewType);

    void bindItemViewData(ItemContext context, VH holder, T data);

    void dataBinding(DataBinder<T, VH> dataBinder, DataBindingMatchPolicy matchPolicy);

    void createItemView(ItemViewFactory itemViewFactory, ItemBindingMatchPolicy matchPolicy);
}
