package org.jzl.android.recyclerview.data;

import org.jzl.lang.util.CollectionUtils;
import org.jzl.lang.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class DataSourceImpl<T, E> implements DataSource<T> {

    private DataManager<E> dataManager;

    private List<E> data;
    private final List<T> source = new ArrayList<>();

    public DataSourceImpl(DataManager<E> dataManager) {
        this.dataManager = ObjectUtils.requireNonNull(dataManager);
        this.data = new ArrayList<>();
    }

    @Override
    public boolean isEmpty() {
        return CollectionUtils.isEmpty(data);
    }

    public int size() {
        return data.size();
    }

    @Override
    public void addAll(int itemType, Collection<T> data) {
        ArrayList<E> entities = new ArrayList<>(data.size());
        for (T t : data) {
            entities.add(dataManager.createEntity(itemType, t));
        }
        this.data.addAll(entities);
        this.source.addAll(data);
        dataManager.update();//更新数据
    }

    @Override
    public void add(int itemType, int index, T data) {
        if (index < size() && index >= 0) {
            E e = dataManager.createEntity(itemType, data);
            this.data.add(index, e);
            this.source.add(index, data);
            dataManager.update();
        } else {
            addAll(itemType, data);
        }
    }

    @Override
    public void remove(int index) {
        data.remove(index);
        source.remove(index);
        dataManager.update();
    }

    @Override
    public void remove(T data) {
        remove(source.indexOf(data));
    }

    @Override
    public void removeAll(Collection<T> data) {
        for (T t : data) {
            int index = source.indexOf(t);
            this.data.remove(index);
            this.source.remove(index);
        }
        dataManager.update();
    }

    @Override
    public void move(int formPosition, int toPosition) {
        CollectionUtils.move(this.data, formPosition, toPosition);
        CollectionUtils.move(this.source, formPosition, toPosition);
        dataManager.update();
    }

    public E getEntity(int index) {
        return data.get(index);
    }

    public List<E> getData() {
        return Collections.unmodifiableList(data);
    }
}
