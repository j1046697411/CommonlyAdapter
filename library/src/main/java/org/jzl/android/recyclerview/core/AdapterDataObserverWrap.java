package org.jzl.android.recyclerview.core;

import androidx.annotation.Nullable;

import org.jzl.lang.util.ObjectUtils;

public abstract class AdapterDataObserverWrap implements AdapterDataObserver{

    protected AdapterDataObserver adapterDataObserver;

    protected AdapterDataObserverWrap(AdapterDataObserver adapterDataObserver) {
        this.adapterDataObserver = ObjectUtils.requireNonNull(adapterDataObserver, "adapterDataObserver");
    }

    @Override
    public void notifyObtainSnapshot() {
        adapterDataObserver.notifyObtainSnapshot();
    }

    @Override
    public void notifyDataSetChanged() {
        adapterDataObserver.notifyDataSetChanged();
    }

    @Override
    public void notifyItemChanged(int position) {
        adapterDataObserver.notifyItemChanged(position);
    }

    @Override
    public void notifyItemChanged(int position, @Nullable Object payload) {
        adapterDataObserver.notifyItemChanged(position, payload);
    }

    @Override
    public void notifyItemRangeChanged(int positionStart, int itemCount) {
        adapterDataObserver.notifyItemRangeChanged(positionStart, itemCount);
    }

    @Override
    public void notifyItemRangeChanged(int positionStart, int itemCount, @Nullable Object payload) {
        adapterDataObserver.notifyItemRangeChanged(positionStart, itemCount, payload);
    }

    @Override
    public void notifyItemInserted(int position) {
        adapterDataObserver.notifyItemInserted(position);
    }

    @Override
    public void notifyItemMoved(int fromPosition, int toPosition) {
        adapterDataObserver.notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void notifyItemRangeInserted(int positionStart, int itemCount) {
        adapterDataObserver.notifyItemRangeInserted(positionStart, itemCount);
    }

    @Override
    public void notifyItemRemoved(int position) {
        adapterDataObserver.notifyItemRemoved(position);
    }

    @Override
    public void notifyItemRangeRemoved(int positionStart, int itemCount) {
        adapterDataObserver.notifyItemRangeRemoved(positionStart, itemCount);
    }
}
