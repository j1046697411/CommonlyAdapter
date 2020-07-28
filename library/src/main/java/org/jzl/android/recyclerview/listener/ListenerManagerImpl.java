package org.jzl.android.recyclerview.listener;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jzl.android.provider.ContextProvider;
import org.jzl.android.recyclerview.CommonlyAdapter;
import org.jzl.android.recyclerview.core.ObjectBinder;
import org.jzl.android.recyclerview.core.item.ItemBindingMatchPolicy;
import org.jzl.android.recyclerview.data.DataManager;
import org.jzl.android.recyclerview.util.Binary;
import org.jzl.lang.util.CollectionUtils;
import org.jzl.lang.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ListenerManagerImpl<T, VH extends RecyclerView.ViewHolder> implements ListenerManager<T, VH> {

    private DataManager<T> dataManager;

    private final List<ObjectBinder<CommonlyAdapter<T, VH>>> objectBinders = new ArrayList<>();
    private final List<ObjectBinder<RecyclerView>> recyclerViewObjectBinders = new ArrayList<>();

    private final Set<OnAttachedToRecyclerViewListener<T, VH>> attachedToRecyclerViewListeners = new HashSet<>();
    private final Map<ItemBindingMatchPolicy, OnCreatedViewHolderListener<VH>> createdViewHolderListeners = new HashMap<>();
    private final Map<ItemBindingMatchPolicy, OnViewAttachedToWindowListener<VH>> viewAttachedToWindowListeners = new HashMap<>();
    private final List<OnDetachedFromRecyclerViewListener> detachedFromRecyclerViewListeners = new ArrayList<>();
    private final List<Binary<ItemBindingMatchPolicy, OnViewDetachedFromWindowListener<VH>>> viewDetachedFromWindowListeners = new ArrayList<>();
    private OnFailedToRecycleViewListener<VH> failedToRecycleViewListener;

    public ListenerManagerImpl(DataManager<T> dataManager) {
        this.dataManager = ObjectUtils.requireNonNull(dataManager, "dataManager");
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull CommonlyAdapter<T, VH> adapter, @NonNull RecyclerView recyclerView) {
        bindCommonlyAdapter(adapter);
        bindRecyclerView(recyclerView);
        attachedToRecyclerView(adapter, recyclerView);
    }

    private void attachedToRecyclerView(CommonlyAdapter<T, VH> adapter, RecyclerView recyclerView) {
        ContextProvider contextProvider = ContextProvider.of(recyclerView);
        for (OnAttachedToRecyclerViewListener<T, VH> listener : attachedToRecyclerViewListeners) {
            listener.onAttachedToRecyclerView(contextProvider, recyclerView, adapter);
        }
    }

    private void bindCommonlyAdapter(CommonlyAdapter<T, VH> adapter) {
        for (ObjectBinder<CommonlyAdapter<T, VH>> binder : objectBinders) {
            binder.bind(adapter);
        }
    }

    private void bindRecyclerView(RecyclerView recyclerView) {
        for (ObjectBinder<RecyclerView> objectBinder : recyclerViewObjectBinders) {
            objectBinder.bind(recyclerView);
        }
    }

    @Override
    public void onCreatedViewHolder(@NonNull VH holder) {
        for (Map.Entry<ItemBindingMatchPolicy, OnCreatedViewHolderListener<VH>> entry : this.createdViewHolderListeners.entrySet()) {
            if (entry.getKey().match(holder.getItemViewType())) {
                entry.getValue().onCreatedViewHolder(holder);
            }
        }
    }


    @Override
    public void onViewAttachedToWindow(@NonNull VH holder) {
        for (Map.Entry<ItemBindingMatchPolicy, OnViewAttachedToWindowListener<VH>> entry : this.viewAttachedToWindowListeners.entrySet()) {
            if (entry.getKey().match(holder.getItemViewType())) {
                entry.getValue().onViewAttachedToWindow(holder);
            }
        }
    }

    @Override
    public boolean onFailedToRecycleView(@NonNull VH holder) {
        if (ObjectUtils.nonNull(failedToRecycleViewListener)) {
            return failedToRecycleViewListener.onFailedToRecycleView(holder);
        }
        return false;
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        CollectionUtils.each(this.detachedFromRecyclerViewListeners, target -> target.onDetachedFromRecyclerView(recyclerView));
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull VH holder) {
        CollectionUtils.each(this.viewDetachedFromWindowListeners, target -> {
            if (target.one.match(holder.getItemViewType())) {
                target.two.onViewDetachedFromWindow(holder);
            }
        });
    }

    @Override
    public void onViewRecycled(@NonNull VH holder) {

    }

    public void addObjectBinder(ObjectBinder<CommonlyAdapter<T, VH>> binder) {
        this.objectBinders.add(binder);
    }

    public void addRecyclerViewObjectBinder(ObjectBinder<RecyclerView> binder) {
        this.recyclerViewObjectBinders.add(binder);
    }

    public void addOnAttachedToRecyclerViewListener(OnAttachedToRecyclerViewListener<T, VH> attachedToRecyclerViewListener) {
        if (ObjectUtils.nonNull(attachedToRecyclerViewListener)) {
            this.attachedToRecyclerViewListeners.add(attachedToRecyclerViewListener);
        }
    }

    public void addOnCreatedViewHolderListener(OnCreatedViewHolderListener<VH> createdViewHolderListener, ItemBindingMatchPolicy matchPolicy) {
        if (ObjectUtils.nonNull(createdViewHolderListener) && ObjectUtils.nonNull(matchPolicy)) {
            this.createdViewHolderListeners.put(matchPolicy, createdViewHolderListener);
        }
    }

    public void addOnViewAttachedToWindowListener(OnViewAttachedToWindowListener<VH> viewAttachedToWindowListener, ItemBindingMatchPolicy matchPolicy) {
        if (ObjectUtils.nonNull(viewAttachedToWindowListener) && ObjectUtils.nonNull(matchPolicy)) {
            this.viewAttachedToWindowListeners.put(matchPolicy, viewAttachedToWindowListener);
        }
    }

    public void addOnDetachedFromRecyclerViewListener(OnDetachedFromRecyclerViewListener detachedFromRecyclerViewListener) {
        if (ObjectUtils.nonNull(detachedFromRecyclerViewListener)) {
            this.detachedFromRecyclerViewListeners.add(detachedFromRecyclerViewListener);
        }
    }

    public void addOnViewDetachedFromWindowListener(OnViewDetachedFromWindowListener<VH> viewDetachedFromWindowListener, ItemBindingMatchPolicy matchPolicy) {
        if (ObjectUtils.nonNull(viewDetachedFromWindowListener) && ObjectUtils.nonNull(matchPolicy)) {
            this.viewDetachedFromWindowListeners.add(Binary.of(matchPolicy, viewDetachedFromWindowListener));
        }
    }

    public void addOnDataUpdateListener(DataManager.OnDataUpdateListener<T> dataUpdateListener){
        this.dataManager.addDataUpdateListener(dataUpdateListener);
    }

    public void setFailedToRecycleViewListener(OnFailedToRecycleViewListener<VH> failedToRecycleViewListener) {
        this.failedToRecycleViewListener = failedToRecycleViewListener;
    }

}
