package org.jzl.android.recyclerview.core;

import androidx.recyclerview.widget.RecyclerView;

@FunctionalInterface
public interface AdapterDataObserverFactory<VH extends RecyclerView.ViewHolder> {

    AdapterDataObserver createAdapterDataObserver(RecyclerView.Adapter<VH> adapter);

}
