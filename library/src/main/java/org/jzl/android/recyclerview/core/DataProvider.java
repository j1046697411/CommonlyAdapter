package org.jzl.android.recyclerview.core;

import org.jzl.lang.util.datablcok.DataBlock;

import java.util.List;

public interface DataProvider<T> {

    int getDataCount();

    int getDataType(int position);

    T getData(int position);

    long getDataId(int position);

    DataBlock<T> findDataBlockByPosition(int position);

    List<T> snapshot();
}
