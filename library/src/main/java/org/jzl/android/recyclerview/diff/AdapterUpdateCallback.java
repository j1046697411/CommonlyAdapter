package org.jzl.android.recyclerview.diff;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ListUpdateCallback;
import androidx.recyclerview.widget.RecyclerView;

import org.jzl.android.recyclerview.CommonlyAdapter;
import org.jzl.lang.util.ObjectUtils;

public class AdapterUpdateCallback<T, VH extends RecyclerView.ViewHolder> implements ListUpdateCallback {

    private CommonlyAdapter<T, VH> adapter;
    private Handler mainHandler;

    public AdapterUpdateCallback(Handler mainHandler, CommonlyAdapter<T, VH> adapter) {
        this.mainHandler = mainHandler;
        this.adapter = adapter;
    }

    public AdapterUpdateCallback(CommonlyAdapter<T, VH> adapter) {
        this(new Handler(Looper.getMainLooper()), adapter);
    }

    @Override
    public void onInserted(int position, int count) {
        if (ObjectUtils.nonNull(adapter)) {
            post(() -> adapter.notifyItemRangeInserted(position, count));
        }
    }

    @Override
    public void onRemoved(int position, int count) {
        if (ObjectUtils.nonNull(adapter)) {
            post(() -> adapter.notifyItemRangeRemoved(position, count));
        }
    }

    @Override
    public void onMoved(int fromPosition, int toPosition) {
        if (ObjectUtils.nonNull(adapter)) {
            post(() -> adapter.notifyItemMoved(fromPosition, toPosition));
        }

    }

    @Override
    public void onChanged(int position, int count, @Nullable Object payload) {
        if (ObjectUtils.nonNull(adapter)) {
            post(() -> adapter.notifyItemRangeChanged(position, count, payload));
        }
    }

    private void post(Runnable run) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            run.run();
        } else {
            mainHandler.post(run);
        }
    }

}
