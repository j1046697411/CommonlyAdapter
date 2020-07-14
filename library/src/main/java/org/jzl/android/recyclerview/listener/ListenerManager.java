package org.jzl.android.recyclerview.listener;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jzl.android.recyclerview.CommonlyAdapter;

public interface ListenerManager<T, VH extends RecyclerView.ViewHolder> {

    void onAttachedToRecyclerView(@NonNull CommonlyAdapter<T, VH> adapter, @NonNull RecyclerView recyclerView);

    void onCreatedViewHolder(@NonNull VH holder);

    void onViewAttachedToWindow(@NonNull VH holder);

    boolean onFailedToRecycleView(@NonNull VH holder);

    void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView);

    void onViewDetachedFromWindow(@NonNull VH holder);

    void onViewRecycled(@NonNull VH holder);
}
