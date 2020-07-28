package org.jzl.android.recyclerview.diff;

import org.jzl.android.recyclerview.data.model.CAModel;
import org.jzl.lang.util.ObjectUtils;

public class CAModelDiffCallback implements DiffCallback<CAModel> {

    private Callback callback;

    public CAModelDiffCallback() {
        this(new DefaultCallback());
    }

    public CAModelDiffCallback(Callback callback) {
        this.callback = ObjectUtils.requireNonNull(callback, "callback");
    }


    @Override
    public boolean areItemsTheSame(CAModel oldItemData, CAModel newItemData) {
        if (oldItemData == newItemData){
            return true;
        }
        return oldItemData.getItemId() == newItemData.getItemId();
    }

    @Override
    public boolean areContentsTheSame(CAModel oldItemData, CAModel newItemData) {
        return callback.areContentsTheSame(oldItemData.getData(), newItemData.getData());
    }

    @Override
    public Object getChangePayload(CAModel oldItemData, CAModel newItemData) {
        return callback.getChangePayload(oldItemData.getData(), newItemData.getData());
    }

    public interface Callback {

        boolean areContentsTheSame(Object oldItemData, Object newItemData);

        Object getChangePayload(Object oldItemData, Object newItemData);
    }

    public static class DefaultCallback implements Callback {

        @Override
        public boolean areContentsTheSame(Object oldItemData, Object newItemData) {
            return false;
        }

        @Override
        public Object getChangePayload(Object oldItemData, Object newItemData) {
            return null;
        }
    }

}
