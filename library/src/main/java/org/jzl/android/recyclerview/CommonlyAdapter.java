package org.jzl.android.recyclerview;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jzl.android.recyclerview.component.ComponentManager;
import org.jzl.android.recyclerview.core.item.ItemViewManager;
import org.jzl.android.recyclerview.data.DataManager;
import org.jzl.android.recyclerview.data.DataProvider;
import org.jzl.android.recyclerview.listener.ListenerManager;
import org.jzl.lang.util.ObjectUtils;

import java.util.List;

public class CommonlyAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    private LayoutInflater layoutInflater;
    private DataProvider<T> dataProvider;
    private ItemViewManager<T, VH> itemViewManager;
    private ListenerManager<T, VH> listenerManager;
    private ComponentManager<T, VH> componentManager;

    protected CommonlyAdapter(DataProvider<T> dataProvider, ItemViewManager<T, VH> itemViewManager, ListenerManager<T, VH> listenerManager, ComponentManager<T, VH> componentManager) {
        this.dataProvider = ObjectUtils.requireNonNull(dataProvider, "dataProvider");
        this.itemViewManager = ObjectUtils.requireNonNull(itemViewManager, "itemViewManager");
        this.listenerManager = ObjectUtils.requireNonNull(listenerManager, "listenerManager");
        this.componentManager = ObjectUtils.requireNonNull(componentManager, "componentManager");
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        layoutInflater = LayoutInflater.from(recyclerView.getContext());
        listenerManager.onAttachedToRecyclerView(this, recyclerView);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull VH holder) {
        super.onViewAttachedToWindow(holder);
        listenerManager.onViewAttachedToWindow(holder);
    }

    @Override
    public boolean onFailedToRecycleView(@NonNull VH holder) {
        return listenerManager.onFailedToRecycleView(holder);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        listenerManager.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull VH holder) {
        listenerManager.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onViewRecycled(@NonNull VH holder) {
        listenerManager.onViewRecycled(holder);
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
    public void onBindViewHolder(@NonNull VH holder, int position, @NonNull List<Object> payloads) {
        DataProvider.DataBlock dataBlock = dataProvider.findDataBlock(position);
        if (holder instanceof DataBinderCallback) {
            ((DataBinderCallback) holder).beforeBindViewHolder();
            itemViewManager.bindItemViewData(dataBlock, holder, dataProvider.getData(position), payloads);
            ((DataBinderCallback) holder).afterBindViewHolder();
        } else {
            itemViewManager.bindItemViewData(dataBlock, holder, dataProvider.getData(position), payloads);
        }
    }


    @Override
    public int getItemCount() {
        return dataProvider.getDataCount();
    }

    @Override
    public int getItemViewType(int position) {
        return dataProvider.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return dataProvider.getItemId(position);
    }

    public interface DataBinderCallback {

        void beforeBindViewHolder();

        void afterBindViewHolder();
    }

    public ItemViewManager<T, VH> getItemViewManager() {
        return itemViewManager;
    }

    public ListenerManager<T, VH> getListenerManager() {
        return listenerManager;
    }

    public ComponentManager<T, VH> getComponentManager() {
        return componentManager;
    }

    public static <T, VH extends RecyclerView.ViewHolder> CommonlyAdapter<T, VH> of(DataManager<T> dataManager, ItemViewManager<T, VH> itemViewManager, ListenerManager<T, VH> listenerManager, ComponentManager<T, VH> componentManager) {
        return new CommonlyAdapter<>(dataManager, itemViewManager, listenerManager,componentManager);
    }

}
