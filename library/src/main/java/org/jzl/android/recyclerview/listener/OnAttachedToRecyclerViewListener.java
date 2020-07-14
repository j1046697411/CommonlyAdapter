package org.jzl.android.recyclerview.listener;

import androidx.recyclerview.widget.RecyclerView;

import org.jzl.android.provider.ContextProvider;
import org.jzl.android.recyclerview.CommonlyAdapter;

public interface OnAttachedToRecyclerViewListener<T, VH extends RecyclerView.ViewHolder> {

    void onAttachedToRecyclerView(ContextProvider contextProvider, RecyclerView recyclerView, CommonlyAdapter<T, VH> adapter);

}
