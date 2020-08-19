package org.jzl.android.recyclerview.core.listener;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jzl.android.provider.ContextProvider;
import org.jzl.android.recyclerview.core.CommonlyAdapter;
import org.jzl.android.recyclerview.core.DataProvider;
import org.jzl.android.recyclerview.core.context.ExtraItemContext;
import org.jzl.android.recyclerview.core.context.ItemContext;
import org.jzl.android.recyclerview.core.ListenerManager;
import org.jzl.android.recyclerview.core.item.ItemBindingMatchPolicy;
import org.jzl.lang.util.CollectionUtils;
import org.jzl.lang.util.ForeachUtils;
import org.jzl.lang.util.ObjectUtils;
import org.jzl.lang.util.holder.BinaryHolder;

import java.util.List;

public class ListenerManagerImpl<T, VH extends RecyclerView.ViewHolder> implements ListenerManager<T, VH> {

    private OnFailedToRecycleViewListener<VH> failedToRecycleViewListener;

    private List<OnUpdatedItemContextListener<T, VH>> updatedItemContextListeners = CollectionUtils.newArrayList();
    private List<OnDetachedFromRecyclerViewListener> detachedFromRecyclerViewListeners = CollectionUtils.newArrayList();
    private List<OnAttachedToRecyclerViewListener<T, VH>> attachedToRecyclerViewListeners = CollectionUtils.newArrayList();

    private List<BinaryHolder<ItemBindingMatchPolicy, OnCreatedViewHolderListener<VH>>> createdViewHolderListeners = CollectionUtils.newArrayList();
    private List<BinaryHolder<ItemBindingMatchPolicy, OnViewAttachedToWindowListener<VH>>> viewAttachedToWindowListeners = CollectionUtils.newArrayList();
    private List<BinaryHolder<ItemBindingMatchPolicy, OnViewDetachedFromWindowListener<VH>>> viewDetachedFromWindowListeners = CollectionUtils.newArrayList();
    private List<BinaryHolder<ItemBindingMatchPolicy, OnViewRecycledListener<VH>>> viewRecycledListeners = CollectionUtils.newArrayList();

    @Override
    public void onAttachedToRecyclerView(@NonNull CommonlyAdapter<T, VH> adapter, @NonNull RecyclerView recyclerView) {
        ContextProvider contextProvider = ContextProvider.of(recyclerView);
        ForeachUtils.each(this.attachedToRecyclerViewListeners, target -> target.onAttachedToRecyclerView(contextProvider, recyclerView, adapter));
    }

    @Override
    public void onCreatedViewHolder(@NonNull VH holder) {
        ForeachUtils.each(this.createdViewHolderListeners, target -> {
            if (target.one.match(holder.getItemViewType())) {
                target.two.onCreatedViewHolder(holder);
            }
        });
    }

    @Override
    public void onViewAttachedToWindow(@NonNull VH holder) {
        ForeachUtils.each(this.viewAttachedToWindowListeners, target -> {
            if (target.one.match(holder.getItemViewType())) {
                target.two.onViewAttachedToWindow(holder);
            }
        });
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
        ForeachUtils.each(this.detachedFromRecyclerViewListeners, target -> target.onDetachedFromRecyclerView(recyclerView));
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull VH holder) {
        ForeachUtils.each(this.viewDetachedFromWindowListeners, target -> {
            if (target.one.match(holder.getItemViewType())) {
                target.two.onViewDetachedFromWindow(holder);
            }
        });
    }

    @Override
    public void onViewRecycled(@NonNull VH holder) {

    }

    @Override
    public void onUpdatedItemContext(ExtraItemContext context, DataProvider<T> dataProvider, VH holder, T data) {
        ForeachUtils.each(this.updatedItemContextListeners, target -> target.onUpdatedItemContext(context, dataProvider, holder, data));
    }

    @Override
    public void setFailedToRecycleViewListener(OnFailedToRecycleViewListener<VH> failedToRecycleViewListener) {
        this.failedToRecycleViewListener = failedToRecycleViewListener;
    }

    @Override
    public void addOnUpdatedItemContextListener(OnUpdatedItemContextListener<T, VH> updatedItemContextListener) {
        if (ObjectUtils.nonNull(updatedItemContextListener)) {
            this.updatedItemContextListeners.add(updatedItemContextListener);
        }
    }

    @Override
    public void addOnDetachedFromRecyclerViewListener(OnDetachedFromRecyclerViewListener detachedFromRecyclerViewListener) {
        if (ObjectUtils.nonNull(detachedFromRecyclerViewListener)) {
            this.detachedFromRecyclerViewListeners.add(detachedFromRecyclerViewListener);
        }
    }

    @Override
    public void addOnAttachedToRecyclerViewListener(OnAttachedToRecyclerViewListener<T, VH> attachedToRecyclerViewListener) {
        if (ObjectUtils.nonNull(attachedToRecyclerViewListener)) {
            this.attachedToRecyclerViewListeners.add(attachedToRecyclerViewListener);
        }
    }

    @Override
    public void addOnCreatedViewHolderListener(OnCreatedViewHolderListener<VH> createdViewHolderListener, ItemBindingMatchPolicy matchPolicy) {
        if (ObjectUtils.nonNull(createdViewHolderListener)) {
            this.createdViewHolderListeners.add(BinaryHolder.of(ObjectUtils.get(matchPolicy, ItemBindingMatchPolicy.MATCH_POLICY_ALL), createdViewHolderListener));
        }
    }

    @Override
    public void addOnViewAttachedToWindowListener(OnViewAttachedToWindowListener<VH> viewAttachedToWindowListener, ItemBindingMatchPolicy matchPolicy) {
        if (ObjectUtils.nonNull(viewAttachedToWindowListener)) {
            this.viewAttachedToWindowListeners.add(BinaryHolder.of(ObjectUtils.get(matchPolicy, ItemBindingMatchPolicy.MATCH_POLICY_ALL), viewAttachedToWindowListener));
        }
    }

    @Override
    public void addOnViewDetachedFromWindowListener(OnViewDetachedFromWindowListener<VH> viewDetachedFromWindowListener, ItemBindingMatchPolicy matchPolicy) {
        if (ObjectUtils.nonNull(viewDetachedFromWindowListener)) {
            this.viewDetachedFromWindowListeners.add(BinaryHolder.of(ObjectUtils.get(matchPolicy, ItemBindingMatchPolicy.MATCH_POLICY_ALL), viewDetachedFromWindowListener));
        }
    }

    @Override
    public void addOnViewRecycledListener(OnViewRecycledListener<VH> viewRecycledListener, ItemBindingMatchPolicy matchPolicy) {
        if (ObjectUtils.nonNull(viewRecycledListener)) {
            this.viewRecycledListeners.add(BinaryHolder.of(ObjectUtils.get(matchPolicy, ItemBindingMatchPolicy.MATCH_POLICY_ALL), viewRecycledListener));
        }
    }
}