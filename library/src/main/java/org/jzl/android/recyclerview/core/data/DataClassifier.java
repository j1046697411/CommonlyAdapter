package org.jzl.android.recyclerview.core.data;


import org.jzl.android.recyclerview.core.DataProvider;

public interface DataClassifier<T> {

    int getDataType(DataProvider<T> dataProvider, int position);
}
