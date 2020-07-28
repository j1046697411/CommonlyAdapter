package org.jzl.android.recyclerview.diff;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListUpdateCallback;

import org.jzl.android.recyclerview.util.Logger;

import java.util.List;

public class AsyncDiffDispatcher<T> implements DiffDispatcher<T> {
    private static Logger log = Logger.logger(AsyncDiffDispatcher.class);

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
        log.d("oldSize => " + oldData.size() + "|" + "newSize => " + newData.size());
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
                T oldModel = oldData.get(oldItemPosition);
                T newModel = newData.get(newItemPosition);

                boolean areItemsTheSame = diffCallback.areItemsTheSame(oldModel, newModel);
                log.d("areItemsTheSame=>" + areItemsTheSame + "{" + oldItemPosition + "-" + newItemPosition + "}");
                return areItemsTheSame;
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                T oldModel = oldData.get(oldItemPosition);
                T newModel = newData.get(newItemPosition);

                boolean areContentsTheSame = diffCallback.areContentsTheSame(oldModel, newModel);
                log.d("areContentsTheSame=>" + areContentsTheSame + "{" + oldItemPosition + "-" + newItemPosition + "}");
                return areContentsTheSame;
            }

            @Nullable
            @Override
            public Object getChangePayload(int oldItemPosition, int newItemPosition) {
                return diffCallback.getChangePayload(oldData.get(oldItemPosition), newData.get(newItemPosition));
            }
        }, detectMoves).dispatchUpdatesTo(updateCallback);

    }

}
