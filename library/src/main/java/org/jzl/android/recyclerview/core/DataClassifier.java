package org.jzl.android.recyclerview.core;

public interface DataClassifier<T> {
    int getDataType(T data, int position);
}
