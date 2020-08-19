package org.jzl.android.recyclerview.component.diff;

import org.jzl.android.recyclerview.core.AdapterDataObserver;
import org.jzl.android.recyclerview.core.DataProvider;
import org.jzl.lang.util.CollectionUtils;
import org.jzl.lang.util.ObjectUtils;

import java.util.List;

public final class DiffDataObserverImpl<T> implements DiffDataObserver {

    private List<T> oldData;
    private DataProvider<T> dataProvider;
    private DiffDispatcher<T> diffDispatcher;
    private SnapCamera<T> snapCamera;
    private boolean detectMoves = false;

    public DiffDataObserverImpl(DataProvider<T> dataProvider, DiffDispatcher<T> diffDispatcher, SnapCamera<T> snapCamera) {
        this.dataProvider = ObjectUtils.requireNonNull(dataProvider, "dataProvider");
        this.diffDispatcher = ObjectUtils.requireNonNull(diffDispatcher, "diffDispatcher");
        this.snapCamera = ObjectUtils.requireNonNull(snapCamera, "snapCamera");
    }

    public void setSnapCamera(SnapCamera<T> snapCamera) {
        this.snapCamera = ObjectUtils.get(snapCamera, this.snapCamera);
    }

    public void setDetectMoves(boolean detectMoves) {
        this.detectMoves = detectMoves;
    }

    @Override
    public void notifyObtainSnapshot() {
        oldData = snapCamera.snapshot(dataProvider);
    }

    @Override
    public void onDataSetChanged(AdapterDataObserver adapterDataObserver) {
        if (CollectionUtils.nonEmpty(oldData)) {
            diffDispatcher.update(dataProvider, oldData, detectMoves);
        } else {
            adapterDataObserver.notifyDataSetChanged();
        }
    }
}
