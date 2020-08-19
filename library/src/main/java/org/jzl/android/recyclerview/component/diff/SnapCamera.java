package org.jzl.android.recyclerview.component.diff;

import org.jzl.android.recyclerview.core.DataProvider;

import java.util.List;

public interface SnapCamera<T> {
    List<T> snapshot(DataProvider<T> dataProvider);
}
