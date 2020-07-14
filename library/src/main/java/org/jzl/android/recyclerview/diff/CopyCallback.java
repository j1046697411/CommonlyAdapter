package org.jzl.android.recyclerview.diff;

public interface CopyCallback<T> {
    T copy(T oldData);
}
