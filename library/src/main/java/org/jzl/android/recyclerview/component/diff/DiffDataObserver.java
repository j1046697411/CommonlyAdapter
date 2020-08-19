package org.jzl.android.recyclerview.component.diff;

import org.jzl.android.recyclerview.core.AdapterDataObserver;
import org.jzl.android.recyclerview.core.DataProvider;

public interface DiffDataObserver {

    void notifyObtainSnapshot();

    void onDataSetChanged(AdapterDataObserver adapterDataObserver);

    static <T> DiffDataObserver of(DataProvider<T> dataProvider, DiffDispatcher<T> diffDispatcher, SnapCamera<T> snapCamera) {
        return new DiffDataObserverImpl<>(dataProvider, diffDispatcher, snapCamera);
    }
}
