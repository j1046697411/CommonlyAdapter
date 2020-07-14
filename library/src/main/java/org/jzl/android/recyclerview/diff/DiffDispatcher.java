package org.jzl.android.recyclerview.diff;

import java.util.List;

public interface DiffDispatcher<T> {

    void update(List<T> oldData, List<T> newData);

}
