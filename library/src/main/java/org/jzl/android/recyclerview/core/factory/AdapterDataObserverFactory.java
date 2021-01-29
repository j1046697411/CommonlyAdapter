package org.jzl.android.recyclerview.core.factory;

import androidx.recyclerview.widget.RecyclerView;

import org.jzl.android.recyclerview.core.configuration.Configuration;
import org.jzl.android.recyclerview.core.observer.AdapterDataObserver;

public interface AdapterDataObserverFactory<T, VH extends RecyclerView.ViewHolder> {

    AdapterDataObserver createAdapterDataObserver(Configuration<T, VH> configuration, RecyclerView.Adapter<VH> adapter);

}
