package org.jzl.android.recyclerview.data;

import org.jzl.android.recyclerview.CommonlyAdapter;
import org.jzl.android.recyclerview.core.EntityFactory;
import org.jzl.android.recyclerview.core.ObjectBinder;
import org.jzl.android.recyclerview.core.PositionType;

import java.util.List;

public interface DataManager<T> extends ObjectBinder<CommonlyAdapter<T, ?>>, DataProvider<T>, EntityFactory<T> {

    @Override
    void bind(CommonlyAdapter<T, ?> target);

    @Override
    T getData(int position);

    @Override
    int getDataCount();

    @Override
    int getItemViewType(int position);

    @Override
    long getItemId(int position);

    @Override
    T createEntity(int itemType, Object data);

    <E> DataSource<E> dataSource(PositionType positionType, int block);

    @Override
    DataBlock findDataBlock(int position);

    void update();

    List<T> snapshot();

    void addDataUpdateListener(OnDataUpdateListener<T> dataUpdateListener);

    interface OnDataUpdateListener<T> {
        void onDataUpdate(List<T> oldData, List<T> newData);
    }

}
