package org.jzl.android.recyclerview.core.listener;

import androidx.recyclerview.widget.RecyclerView;

import org.jzl.android.recyclerview.core.DataProvider;
import org.jzl.android.recyclerview.core.context.ExtraItemContext;


public interface OnUpdatedItemContextListener<T, VH extends RecyclerView.ViewHolder> {

    void onUpdatedItemContext(ExtraItemContext context, DataProvider<T> dataProvider, VH holder, T data);

}
