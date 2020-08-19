package org.jzl.android.recyclerview.core.context;

import androidx.recyclerview.widget.RecyclerView;

import org.jzl.android.recyclerview.core.CommonlyAdapter;
import org.jzl.android.recyclerview.core.DataProvider;
import org.jzl.android.recyclerview.core.ItemViewManager;
import org.jzl.android.recyclerview.core.ListenerManager;
import org.jzl.android.recyclerview.core.context.ItemContext;

public interface ItemContextFactory<T, VH extends RecyclerView.ViewHolder> {

    ItemContext createItemContext(CommonlyAdapter<T, VH> adapter, DataProvider<T> dataProvider, ItemViewManager<T, VH> itemViewManager, ListenerManager<T, VH> listenerManager);

}
