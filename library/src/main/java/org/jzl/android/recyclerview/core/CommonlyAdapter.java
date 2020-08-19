package org.jzl.android.recyclerview.core;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jzl.android.provider.ContextProvider;
import org.jzl.android.recyclerview.R;
import org.jzl.android.recyclerview.core.context.ExtraItemContext;
import org.jzl.android.recyclerview.core.context.ItemContentUpdatable;
import org.jzl.android.recyclerview.core.context.ItemContext;
import org.jzl.android.recyclerview.core.context.ItemContextFactory;
import org.jzl.android.recyclerview.util.Logger;
import org.jzl.lang.util.ObjectUtils;

import java.util.List;

public class CommonlyAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    private static final Logger log = Logger.logger(CommonlyAdapter.class);

    private LayoutInflater layoutInflater;
    private DataProvider<T> dataProvider;
    private ItemViewManager<T, VH> itemViewManager;
    private ItemContextFactory<T, VH> contextFactory;
    private ListenerManager<T, VH> listenerManager;

    public CommonlyAdapter(DataProvider<T> dataProvider, ItemViewManager<T, VH> itemViewManager, ItemContextFactory<T, VH> contextFactory, ListenerManager<T, VH> listenerManager) {
        this.dataProvider = ObjectUtils.requireNonNull(dataProvider, "dataProvider");
        this.itemViewManager = ObjectUtils.requireNonNull(itemViewManager, "itemViewManager");
        this.contextFactory = ObjectUtils.requireNonNull(contextFactory, "contextFactory");
        this.listenerManager = ObjectUtils.requireNonNull(listenerManager, "listenerManager");
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        VH holder = itemViewManager.onCreateViewHolder(layoutInflater, parent, viewType);
        listenerManager.onCreatedViewHolder(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onBindViewHolder(@NonNull VH holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        ItemContext context = getItemContext(holder);
        T data = dataProvider.getData(position);
        if (context instanceof ItemContentUpdatable) {
            ((ItemContentUpdatable<T, VH>) context).update(holder, data, payloads);
        }
        if (context instanceof ExtraItemContext) {
            listenerManager.onUpdatedItemContext((ExtraItemContext) context, dataProvider, holder, data);
        }
        if (holder instanceof DataBinderCallback) {
            ((DataBinderCallback) holder).beforeBindViewHolder();
            itemViewManager.bindItemViewData(context, holder, data);
            ((DataBinderCallback) holder).afterBindViewHolder();
        } else {
            itemViewManager.bindItemViewData(context, holder, data);
        }
        log.d("bindMode => " + context.getAdapterPosition() + ":" + context.getBindMode());
    }

    private ItemContext getItemContext(VH holder) {
        ItemContext context = (ItemContext) holder.itemView.getTag(R.id.tag_item_context);
        if (ObjectUtils.isNull(context)) {
            context = contextFactory.createItemContext(this, dataProvider, itemViewManager, listenerManager);
            holder.itemView.setTag(R.id.tag_item_context, context);
        }
        return context;
    }

    @Override
    public int getItemCount() {
        return dataProvider.getDataCount();
    }

    @Override
    public int getItemViewType(int position) {
        return dataProvider.getDataType(position);
    }

    @Override
    public long getItemId(int position) {
        return dataProvider.getDataId(position);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        ContextProvider contextProvider = ContextProvider.of(recyclerView);
        this.layoutInflater = LayoutInflater.from(contextProvider.provide());
        listenerManager.onAttachedToRecyclerView(this, recyclerView);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        listenerManager.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull VH holder) {
        super.onViewAttachedToWindow(holder);
        listenerManager.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull VH holder) {
        super.onViewDetachedFromWindow(holder);
        listenerManager.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onViewRecycled(@NonNull VH holder) {
        super.onViewRecycled(holder);
        listenerManager.onViewRecycled(holder);
    }

    @Override
    public boolean onFailedToRecycleView(@NonNull VH holder) {
        return listenerManager.onFailedToRecycleView(holder);
    }

    public DataProvider<T> getDataProvider() {
        return dataProvider;
    }

    public ListenerManager<T, VH> getListenerManager() {
        return listenerManager;
    }

    public ItemViewManager<T, VH> getItemViewManager() {
        return itemViewManager;
    }

}
