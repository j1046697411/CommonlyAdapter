package org.jzl.android.recyclerview.data;

import org.jzl.android.recyclerview.core.PositionType;

public interface DataProvider<T> {

    int getDataCount();

    int getItemViewType(int position);

    T getData(int position);

    long getItemId(int position);

    DataBlock findDataBlock(int position);

    interface DataBlock {

        PositionType getPositionType();

        int getBlockId();
    }
}
