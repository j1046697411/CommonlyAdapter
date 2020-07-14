package org.jzl.android.recyclerview.data;

import java.util.Arrays;
import java.util.Collection;

public interface DataSource<T> {

    void addAll(int itemType, Collection<T> data);

    default void addAll(int itemType, T... data){
        addAll(itemType, Arrays.asList(data));
    }
}
