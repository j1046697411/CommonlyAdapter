package org.jzl.android.recyclerview.data.model;

public class CyModel implements Typeable {

    private int type;
    private Object data;


    public CyModel(Object data, int type) {
        this.data = data;
        this.type = type;
    }

    public Object getData() {
        return data;
    }

    @Override
    public int getType() {
        if (data instanceof Typeable) {
            return ((Typeable) data).getType();
        }
        return type;
    }
}
