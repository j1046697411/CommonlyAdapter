package org.jzl.android.recyclerview.core.observer;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class DefaultAdapterDataObserver implements AdapterDataObserver {

    private final RecyclerView.Adapter<?> adapter;

    public DefaultAdapterDataObserver(RecyclerView.Adapter<?> adapter) {
        this.adapter = adapter;
    }

    @Override
    public void notifyObtainSnapshot() {
    }

    @Override
    public void notifyDataSetChanged() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void notifyItemChanged(int position) {
        adapter.notifyItemChanged(position);
    }

    @Override
    public void notifyItemChanged(int position, @Nullable Object payload) {
            adapter.notifyItemChanged(position, payload);
    }

    @Override
    public void notifyItemRangeChanged(int positionStart, int itemCount) {
        adapter.notifyItemRangeChanged(positionStart, itemCount);
    }

    @Override
    public void notifyItemRangeChanged(int positionStart, int itemCount, @Nullable Object payload) {
        adapter.notifyItemRangeChanged(positionStart, itemCount, payload);
    }

    @Override
    public void notifyItemInserted(int position) {
        adapter.notifyItemInserted(position);
    }

    @Override
    public void notifyItemMoved(int fromPosition, int toPosition) {
        adapter.notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void notifyItemRangeInserted(int positionStart, int itemCount) {
        adapter.notifyItemRangeChanged(positionStart, itemCount);
    }

    @Override
    public void notifyItemRemoved(int position) {
        adapter.notifyItemRemoved(position);
    }

    @Override
    public void notifyItemRangeRemoved(int positionStart, int itemCount) {
        adapter.notifyItemRangeRemoved(positionStart, itemCount);
    }
}
