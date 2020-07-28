package org.jzl.android.recyclerview.data;

import java.util.Arrays;
import java.util.Collection;

public interface DataSource<T> {

    boolean isEmpty();

    int size();

    void addAll(int itemType, Collection<T> data);

    default void addAll(int itemType, T... data){
        addAll(itemType, Arrays.asList(data));
    }

    void add(int itemType, int index, T data);

    void remove(int index);

    void remove(T data);

    void removeAll(Collection<T> data);

    void move(int formPosition, int toPosition);

    default void removeAll(T ... data){
        removeAll(Arrays.asList(data));
    }
}
