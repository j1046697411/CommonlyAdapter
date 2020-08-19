package org.jzl.android.recyclerview.component.diff;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListUpdateCallback;

import org.jzl.android.recyclerview.core.AdapterDataObserver;
import org.jzl.android.recyclerview.core.DataProvider;
import org.jzl.lang.util.ObjectUtils;

import java.util.List;

public class AsyncDiffDispatcher<T> implements DiffDispatcher<T>, ListUpdateCallback {

    private AdapterDataObserver adapterDataObserver;
    private DiffUtil.ItemCallback<T> itemCallback;

    public AsyncDiffDispatcher(AdapterDataObserver adapterDataObserver, DiffUtil.ItemCallback<T> itemCallback) {
        this.adapterDataObserver = ObjectUtils.requireNonNull(adapterDataObserver, "adapterDataObserver");
        this.itemCallback = ObjectUtils.requireNonNull(itemCallback, "itemCallback");
    }

    @Override
    public void update(DataProvider<T> dataProvider, List<T> oldData, boolean detectMoves) {
        DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return oldData.size();
            }

            @Override
            public int getNewListSize() {
                return dataProvider.getDataCount();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                T oldItemData = oldData.get(oldItemPosition);
                T newItemData = dataProvider.getData(newItemPosition);
                return itemCallback.areItemsTheSame(oldItemData, newItemData);
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                T oldItemData = oldData.get(oldItemPosition);
                T newItemData = dataProvider.getData(newItemPosition);
                return itemCallback.areContentsTheSame(oldItemData, newItemData);
            }

            @Nullable
            @Override
            public Object getChangePayload(int oldItemPosition, int newItemPosition) {
                T oldItemData = oldData.get(oldItemPosition);
                T newItemData = dataProvider.getData(newItemPosition);
                return itemCallback.getChangePayload(oldItemData, newItemData);
            }
        }, detectMoves).dispatchUpdatesTo(this);
    }

    @Override
    public void onInserted(int position, int count) {
        adapterDataObserver.notifyItemRangeInserted(position, count);
    }

    @Override
    public void onRemoved(int position, int count) {
        adapterDataObserver.notifyItemRangeRemoved(position, count);
    }

    @Override
    public void onMoved(int fromPosition, int toPosition) {
        adapterDataObserver.notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onChanged(int position, int count, @Nullable Object payload) {
        adapterDataObserver.notifyItemRangeChanged(position, count, payload);
    }

}
