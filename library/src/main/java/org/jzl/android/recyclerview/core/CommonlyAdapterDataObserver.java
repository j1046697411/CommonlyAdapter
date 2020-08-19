package org.jzl.android.recyclerview.core;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import org.jzl.lang.util.ObjectUtils;

public class CommonlyAdapterDataObserver<T, VH extends RecyclerView.ViewHolder> implements AdapterDataObserver {

    private RecyclerView.Adapter<VH> adapter;

    private CommonlyAdapterDataObserver(RecyclerView.Adapter<VH> adapter) {
        this.adapter = ObjectUtils.requireNonNull(adapter, "adapter");
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
        if (ObjectUtils.nonNull(payload)) {
            adapter.notifyItemChanged(position, payload);
        } else {
            adapter.notifyItemChanged(position);
        }
    }

    @Override
    public void notifyItemRangeChanged(int positionStart, int itemCount) {
        adapter.notifyItemRangeChanged(positionStart, itemCount);
    }

    @Override
    public void notifyItemRangeChanged(int positionStart, int itemCount, @Nullable Object payload) {
        if (ObjectUtils.nonNull(payload)) {
            adapter.notifyItemRangeChanged(positionStart, itemCount, payload);
        } else {
            adapter.notifyItemRangeChanged(positionStart, itemCount);
        }
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
        adapter.notifyItemRangeInserted(positionStart, itemCount);
    }

    @Override
    public void notifyItemRemoved(int position) {
        adapter.notifyItemRemoved(position);
    }

    @Override
    public void notifyItemRangeRemoved(int positionStart, int itemCount) {
        adapter.notifyItemRangeRemoved(positionStart, itemCount);
    }

    public static <T, VH extends RecyclerView.ViewHolder> CommonlyAdapterDataObserver<T, VH> of(RecyclerView.Adapter<VH> adapter) {
        return new CommonlyAdapterDataObserver<>(adapter);
    }

}
