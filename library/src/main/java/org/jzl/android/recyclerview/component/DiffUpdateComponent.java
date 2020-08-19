package org.jzl.android.recyclerview.component;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import org.jzl.android.recyclerview.component.diff.DiffDataObserver;
import org.jzl.android.recyclerview.component.diff.DiffDispatcher;
import org.jzl.android.recyclerview.component.diff.DiffDispatcherFactory;
import org.jzl.android.recyclerview.component.diff.SnapCamera;
import org.jzl.android.recyclerview.config.AdapterConfigurator;
import org.jzl.android.recyclerview.core.AdapterDataObserver;
import org.jzl.android.recyclerview.core.AdapterDataObserverWrap;
import org.jzl.android.recyclerview.core.DataProvider;
import org.jzl.android.recyclerview.core.item.ItemBindingMatchPolicy;
import org.jzl.lang.util.CollectionUtils;
import org.jzl.lang.util.ObjectUtils;

import java.util.List;

public class DiffUpdateComponent<T, VH extends RecyclerView.ViewHolder> implements Component<T, VH>, DiffDataObserver {

    private DiffDispatcherFactory<T> diffDispatcherFactory;
    private DiffUtil.ItemCallback<T> itemCallback;
    private SnapCamera<T> snapCamera;
    private DiffDispatcher<T> diffDispatcher;
    private DataProvider<T> dataProvider;
    private List<T> oldData;
    private boolean detectMoves = false;
    private boolean enable = true;

    public DiffUpdateComponent(DiffDispatcherFactory<T> diffDispatcherFactory, DiffUtil.ItemCallback<T> itemCallback) {
        this.diffDispatcherFactory = ObjectUtils.requireNonNull(diffDispatcherFactory, "diffDispatcherFactory");
        this.itemCallback = ObjectUtils.requireNonNull(itemCallback, "itemCallback");
    }

    @Override
    public void apply(AdapterConfigurator<T, VH> configurator, ItemBindingMatchPolicy matchPolicy) {
        configurator.wrapAdapterDataObserver(target -> new DiffAdapterDataObserver(target, this))
                .adapter(target -> this.dataProvider = target.getDataProvider())
                .adapterDataObserver(target -> this.diffDispatcher = diffDispatcherFactory.createDiffDispatcher(target, itemCallback));
    }

    public void setDetectMoves(boolean detectMoves) {
        this.detectMoves = detectMoves;
    }

    public void setSnapCamera(SnapCamera<T> snapCamera) {
        this.snapCamera = snapCamera;
    }

    public void enable() {
        enable = true;
    }

    public void disable() {
        enable = false;
    }

    public void setDiffDispatcher(DiffDispatcher<T> diffDispatcher) {
        this.diffDispatcher = ObjectUtils.get(diffDispatcher, this.diffDispatcher);
    }

    @Override
    public void notifyObtainSnapshot() {
        if (ObjectUtils.nonNull(dataProvider) && isEnable()) {
            if (ObjectUtils.nonNull(snapCamera)) {
                this.oldData = snapCamera.snapshot(dataProvider);
            } else {
                this.oldData = dataProvider.snapshot();
            }
        }
    }

    @Override
    public void onDataSetChanged(AdapterDataObserver adapterDataObserver) {
        if (CollectionUtils.nonEmpty(oldData) && isEnable() && ObjectUtils.nonNull(diffDispatcher)) {
            diffDispatcher.update(dataProvider, oldData, detectMoves);
        } else {
            adapterDataObserver.notifyDataSetChanged();
        }
    }

    private boolean isEnable() {
        return enable;
    }

    private static class DiffAdapterDataObserver extends AdapterDataObserverWrap implements AdapterDataObserver {

        private DiffDataObserver dataSetChangedListener;

        private DiffAdapterDataObserver(AdapterDataObserver adapterDataObserver, DiffDataObserver dataSetChangedListener) {
            super(adapterDataObserver);
            this.dataSetChangedListener = ObjectUtils.requireNonNull(dataSetChangedListener, "dataSetChangedListener");
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            dataSetChangedListener.onDataSetChanged(this.adapterDataObserver);
        }

        @Override
        public void notifyObtainSnapshot() {
            dataSetChangedListener.notifyObtainSnapshot();
        }
    }

}
