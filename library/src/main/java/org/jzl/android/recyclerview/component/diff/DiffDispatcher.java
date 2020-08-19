package org.jzl.android.recyclerview.component.diff;

import org.jzl.android.recyclerview.core.DataProvider;

import java.util.List;

public interface DiffDispatcher<T> {
    void update(DataProvider<T> dataProvider, List<T> oldData, boolean detectMoves);
}
