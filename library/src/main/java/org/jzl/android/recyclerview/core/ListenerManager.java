package org.jzl.android.recyclerview.core;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jzl.android.recyclerview.core.context.ExtraItemContext;
import org.jzl.android.recyclerview.core.item.ItemBindingMatchPolicy;
import org.jzl.android.recyclerview.core.listener.OnAttachedToRecyclerViewListener;
import org.jzl.android.recyclerview.core.listener.OnCreatedViewHolderListener;
import org.jzl.android.recyclerview.core.listener.OnDetachedFromRecyclerViewListener;
import org.jzl.android.recyclerview.core.listener.OnFailedToRecycleViewListener;
import org.jzl.android.recyclerview.core.listener.OnUpdatedItemContextListener;
import org.jzl.android.recyclerview.core.listener.OnViewAttachedToWindowListener;
import org.jzl.android.recyclerview.core.listener.OnViewDetachedFromWindowListener;
import org.jzl.android.recyclerview.core.listener.OnViewRecycledListener;

public interface ListenerManager<T, VH extends RecyclerView.ViewHolder> {

    void onAttachedToRecyclerView(@NonNull CommonlyAdapter<T, VH> adapter, @NonNull RecyclerView recyclerView);

    void onCreatedViewHolder(@NonNull VH holder);

    void onViewAttachedToWindow(@NonNull VH holder);

    boolean onFailedToRecycleView(@NonNull VH holder);

    void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView);

    void onViewDetachedFromWindow(@NonNull VH holder);

    void onViewRecycled(@NonNull VH holder);

    void onUpdatedItemContext(ExtraItemContext context, DataProvider<T> dataProvider, VH holder, T data);

    void setFailedToRecycleViewListener(OnFailedToRecycleViewListener<VH> failedToRecycleViewListener);

    void addOnUpdatedItemContextListener(OnUpdatedItemContextListener<T, VH> updatedItemContextListener);

    void addOnDetachedFromRecyclerViewListener(OnDetachedFromRecyclerViewListener detachedFromRecyclerViewListener);

    void addOnAttachedToRecyclerViewListener(OnAttachedToRecyclerViewListener<T, VH> attachedToRecyclerViewListener);

    void addOnCreatedViewHolderListener(OnCreatedViewHolderListener<VH> createdViewHolderListener, ItemBindingMatchPolicy matchPolicy);

    void addOnViewAttachedToWindowListener(OnViewAttachedToWindowListener<VH> viewAttachedToWindowListener, ItemBindingMatchPolicy matchPolicy);

    void addOnViewDetachedFromWindowListener(OnViewDetachedFromWindowListener<VH> viewDetachedFromWindowListener, ItemBindingMatchPolicy matchPolicy);

    void addOnViewRecycledListener(OnViewRecycledListener<VH> viewRecycledListener, ItemBindingMatchPolicy matchPolicy);

}