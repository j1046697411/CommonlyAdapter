package org.jzl.android.recyclerview.core;

public interface EntityFactory<T>{
    T createEntity(int itemType, Object data);
}
