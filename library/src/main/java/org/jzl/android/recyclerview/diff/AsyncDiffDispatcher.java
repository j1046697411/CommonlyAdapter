package org.jzl.android.recyclerview.diff;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListUpdateCallback;

import java.util.List;

public class AsyncDiffDispatcher<T> implements DiffDispatcher<T> {

    private ListUpdateCallback updateCallback;
    private DiffCallback<T> diffCallback;
    private boolean detectMoves;

    public AsyncDiffDispatcher(ListUpdateCallback updateCallback, DiffCallback<T> diffCallback, boolean detectMoves) {
        this.updateCallback = updateCallback;
        this.diffCallback = diffCallback;
        this.detectMoves = detectMoves;
    }

    @Override
    public void update(List<T> oldData, List<T> newData) {
        DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return oldData.size();
            }

            @Override
            public int getNewListSize() {
                return newData.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return diffCallback.areItemsTheSame(oldData.get(oldItemPosition), newData.get(newItemPosition));
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return diffCallback.areContentsTheSame(oldData.get(oldItemPosition), newData.get(newItemPosition));
            }

            @Nullable
            @Override
            public Object getChangePayload(int oldItemPosition, int newItemPosition) {
                return diffCallback.getChangePayload(oldData.get(oldItemPosition), newData.get(newItemPosition));
            }
        }, detectMoves).dispatchUpdatesTo(updateCallback);
    }
}
