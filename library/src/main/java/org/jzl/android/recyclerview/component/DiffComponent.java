package org.jzl.android.recyclerview.component;

import android.os.Handler;
import android.os.Looper;

import androidx.recyclerview.widget.RecyclerView;

import org.jzl.android.recyclerview.CommonlyAdapter;
import org.jzl.android.recyclerview.config.AdapterConfigurator;
import org.jzl.android.recyclerview.core.item.ItemBindingMatchPolicy;
import org.jzl.android.recyclerview.diff.AdapterUpdateCallback;
import org.jzl.android.recyclerview.diff.AsyncDiffDispatcher;
import org.jzl.android.recyclerview.diff.DiffCallback;
import org.jzl.android.recyclerview.diff.DiffDispatcher;
import org.jzl.lang.util.ObjectUtils;

import java.util.concurrent.Executor;

public class DiffComponent<T, VH extends RecyclerView.ViewHolder> implements Component<T, VH> {

    private DiffCallback<T> diffCallback;
    private boolean detectMoves = false;
    private DiffDispatcher<T> diffDispatcher;
    private CommonlyAdapter<T, VH> adapter;
    private Executor executorService;
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    public void apply(AdapterConfigurator<T, VH> configurator, ItemBindingMatchPolicy matchPolicy) {
        configurator
                .adapter(adapter -> {
                    diffDispatcher = new AsyncDiffDispatcher<>(new AdapterUpdateCallback<>(mainHandler, adapter), diffCallback, detectMoves);
                    this.adapter = adapter;
                })
                .addOnDataUpdateListener((oldData, newData) -> {
                    if (ObjectUtils.nonNull(diffDispatcher)) {
                        post(() -> diffDispatcher.update(oldData, newData));
                    } else if (ObjectUtils.nonNull(adapter)) {
                        postMain(() -> adapter.notifyDataSetChanged());
                    }
                });
    }

    private void post(Runnable run) {
        if (ObjectUtils.nonNull(executorService)) {
            executorService.execute(run);
        } else {
            run.run();
        }
    }

    private void postMain(Runnable run) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            run.run();
        } else {
            mainHandler.post(run);
        }
    }

    public DiffComponent<T, VH> setDetectMoves(boolean detectMoves) {
        this.detectMoves = detectMoves;
        return this;
    }

    public DiffComponent<T, VH> setDiffCallback(DiffCallback<T> diffCallback) {
        this.diffCallback = diffCallback;
        return this;
    }

    public DiffComponent<T, VH> setExecutor(Executor executorService) {
        this.executorService = executorService;
        return this;
    }

    public static <T, VH extends RecyclerView.ViewHolder> DiffComponent<T, VH> of() {
        return new DiffComponent<>();
    }
}
