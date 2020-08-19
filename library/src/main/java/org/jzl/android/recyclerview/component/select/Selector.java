package org.jzl.android.recyclerview.component.select;

import org.jzl.android.recyclerview.data.Selectable;

public interface Selector<T extends Selectable>{

    void checked(T data, boolean checked);

    void checkedAll();

    void uncheckedAll();
}
