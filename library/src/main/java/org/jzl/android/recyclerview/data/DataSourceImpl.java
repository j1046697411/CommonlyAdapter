package org.jzl.android.recyclerview.data;

import org.jzl.lang.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DataSourceImpl<T, E> implements DataSource<T> {

    private DataManager<E> dataManager;
    private List<E> data;

    public DataSourceImpl(DataManager<E> dataManager) {
        this.dataManager = ObjectUtils.requireNonNull(dataManager);
        this.data = new ArrayList<>();
    }

    public int size() {
        return data.size();
    }

    @Override
    public void addAll(int itemType, Collection<T> data) {
        List<E> newData = new ArrayList<>(data.size());
        for (T entity : data) {
            newData.add(dataManager.createEntity(itemType, entity));
        }
        this.data.addAll(newData);
    }

    public E getEntity(int index) {
        return data.get(index);
    }
}
