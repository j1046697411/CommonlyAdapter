package org.jzl.android.recyclerview.data.model;

import java.util.Objects;

public class CAModel implements TypeAble, IdAble {

    private long id;
    private int type;
    private Object data;

    public CAModel(Object data, int type, long id) {
        this.data = data;
        this.type = type;
        this.id = id;
    }

    public Object getData() {
        return data;
    }

    @Override
    public int getType() {
        if (data instanceof TypeAble) {
            return ((TypeAble) data).getType();
        }
        return type;
    }

    @Override
    public long getItemId() {
        if (data instanceof IdAble){
            return ((IdAble) data).getItemId();
        }
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CAModel caModel = (CAModel) o;
        return id == caModel.id &&
                type == caModel.type &&
                Objects.equals(data, caModel.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, data);
    }
}
