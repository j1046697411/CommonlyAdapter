package org.jzl.android.recyclerview.component.diff;

import androidx.recyclerview.widget.DiffUtil;

import org.jzl.android.recyclerview.core.AdapterDataObserver;

public interface DiffDispatcherFactory<T> {
    DiffDispatcher<T> createDiffDispatcher(AdapterDataObserver adapterDataObserver, DiffUtil.ItemCallback<T> itemCallback);
}
